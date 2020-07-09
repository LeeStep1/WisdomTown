package com.bit.soft.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.bit.base.dto.UserInfo;
import com.bit.base.vo.BaseVo;
import com.bit.common.BaseConst;
import com.bit.common.ResultCode;
import com.bit.soft.gateway.model.permisson.bean.Permission;
import com.bit.soft.gateway.model.permisson.dao.PermissionDao;
import com.bit.soft.gateway.model.permisson.service.impl.PermissionServiceImpl;
import com.bit.core.utils.CacheUtil;
import com.bit.utils.thread.RequestThreadBinder;
import com.netflix.zuul.context.RequestContext;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bit.soft.gateway.common.Const.TOKEN_EXPIRE_SECONDS;


/**
 * Created by lyj on 2018/9/4.
 */
@WebFilter(urlPatterns = {"/*"}, filterName = "tokenAuthorFilter")
public class TokenFilter implements Filter {

	private static Log logger = LogFactory.getLog(TokenFilter.class);

	private static String[] noFilterUrl = {"sys/user/login",
			"sys/user/registerVol",
			"sys/user/verifyUser",//验证用户是否是党员
			"sys/user/verifyVaildCode",//验证码
			"sys/user/registerUser",//注册
			"sys/user/updatePwdByVCode",//忘记密码
			"sys/sendSMS/sendRegSMS",
			"sys/sendSMS/sendRePwdSMS",
			"sys/sendSMS/sendCboAppSMS",
			"sys/user/appPbLogin",
			"sys/user/appPbVerifyMobile",
			"sys/user/appPbVerifyIdCard",
			"sys/user/appPbVerifySmsCode",
			"sys/user/appPbRegister",
			"sys/user/updatePwdByVCode",
			"sys/user/fastRegisterUser",
			"sys/user/checkIdCardOrMobileUnique",
			"sys/cbo/cboOrgAppLogin",
			"sys/cbo/cboOrgResetPassword",
			"vol/news/getNewsDetailForApp",
			"vol/news/getNewsForAppShow",
			"vol/shopMobile/addShop",
			"vol/voInfo/board",
			"vol/partner/add",
			"vol/shopMobile/getProduct",
			"vol/shopMobile/queryProduct",
			"vol/news/getNewsDetailForApp",
			"cms/pageDisplay/",
			"cms/specialPageDisplay/",
			"cms/manager/login",
			"cms/manager/portalContent/query",
			"cbo/resident/appLogin",
			"cbo/resident/appRegister",
			"cbo/resident/resetPassword",
			"cbo/pmc/appLogin",
			"cbo/pmc/resetPassword",
			"cbo/resident/checkMobile",
			"cbo/pmc/checkMobile",
			"sys/cbo/checkMobile",
			"cbo/web/WebCommunityNews/detail"
	};
	private static String[] noJsonUrls = {"pb/meeting/genQrCode"};

	@Autowired
	private CacheUtil cacheUtil;
	@Autowired
	private PermissionServiceImpl permissionService;

  /*  @Autowired
    private RestTemplate restTemplate;*/

	private final String tokenName = "at";

	private final String terminalName = "tid";

	private final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";

	public void init(FilterConfig filterConfig) throws ServletException {

		System.out.println("----------------过滤器初始化------------------------");
	}


	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;

		HttpServletResponse response = (HttpServletResponse) servletResponse;
		if (isIncludeNoJson(((HttpServletRequest) servletRequest).getRequestURI())) {
			response.setContentType("application/json;charset=UTF-8");
		}
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS, DELETE, PUT");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers","Content-Type, x-requested-with, X-Custom-Header,at,tid");

		//获取真实IP地址
		RequestContext ctx = RequestContext.getCurrentContext();
		String remoteAddr = request.getRemoteAddr();
		ctx.getZuulRequestHeaders().put(HTTP_X_FORWARDED_FOR, remoteAddr);

		//如果是OPTIONS请求就return 往后执行会到业务代码中 他不带参数会产生异常
		if (request.getMethod().equals("OPTIONS")) {
			return;
		}
		logger.info("url:"+((HttpServletRequest) servletRequest).getRequestURI());
		if (isInclude(((HttpServletRequest) servletRequest).getRequestURI())) {//白名单不拦截
			try {
				// 白名单启用限流
				if (!cacheUtil.acquire(BaseConst.LIMIT_REDIS_KEY_PREFIX,BaseConst.LIMIT_RATE)){
					responseOutWithJson(response, new BaseVo(ResultCode.PARAMETER_ERROR.getCode(), "操作过于频繁", null));
					return;
				}
				RequestContext ct = RequestContext.getCurrentContext();
				ct.addZuulRequestHeader(BaseConst.FLITER_FLAG, BaseConst.NO_FILTER);
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			} catch (Exception e) {
				logger.info(e.getMessage(),e);
			}

		} else {
			String token = "";
			String terminalId = getTerminalId(request);
			if (terminalId == null || "".equals(terminalId)) {//判断是否为空
				responseOutWithJson(response, new BaseVo(HttpStatus.UNAUTHORIZED.value(), "接入端为空", null));
				return;
			}
			if (request.getHeader(tokenName) != null && !"".equals((request.getHeader(tokenName)))) {//支持从header头中取值
				token = request.getHeader(tokenName);
			} else {
				token = request.getParameter(tokenName);//支持从url中获取
			}
			if (token == null || "".equals(token)) {//判断是否为空
				responseOutWithJson(response, new BaseVo(HttpStatus.UNAUTHORIZED.value(), "无token", null));
				return;
			} else {
				String user = (String) cacheUtil.get("token:"+terminalId+":"+token);
				//JsonResult rs = tokenFeign.verify(token);
				if (user != null) {
					String uri = request.getRequestURI();
					//checkPermission(uri,user);
					long l = cacheUtil.getExpire(token);
					if (l<300){
						// 更新过期时间
						cacheUtil.expire(token, TOKEN_EXPIRE_SECONDS);
					}
					RequestContext ct = RequestContext.getCurrentContext();
					ct.addZuulRequestHeader(BaseConst.FLITER_FLAG, BaseConst.FILTER);
					ct.addZuulRequestHeader(BaseConst.USERTOKEN_PREFIX, URLEncoder.encode(user, "UTF-8"));
					filterChain.doFilter(servletRequest, servletResponse);
					return;
				} else {
					responseOutWithJson(response, new BaseVo(HttpStatus.UNAUTHORIZED.value(), "无效token", null));
					return;
				}
			}
		}
	}

	/**
	 * 通过查询数据库验证权限
	 * @param url
	 * @param user
	 */
	private void checkPermission(String url, String user) {
		UserInfo userInfo = JSON.parseObject(user, UserInfo.class);
		if (CollectionUtils.isEmpty(userInfo.getRoleIds())){
			logger.info("没有接口级权限");// TODO 后期改成抛异常
			return;
		}
		List<Permission> permissions = permissionService.selectByRoleIds(userInfo.getRoleIds());
		Map res = permissions.stream().collect(Collectors.toMap(Permission::getUrl, Function.identity()));
		if (res.get(url) == null) {
			logger.info("没有接口级权限");// TODO 后期改成抛异常
		}
	}

	private String getTerminalId(HttpServletRequest request) {
		String terminalId = request.getHeader(terminalName);
		if (terminalId == null) {
			terminalId = request.getParameter(terminalName);
		}
		return terminalId;
	}


	public void destroy() {

		System.out.println("--------------过滤器销毁--------------");
	}

	/**
	 * 是否需要过滤
	 *
	 * @param url
	 * @return
	 */
	private boolean isInclude(String url) {
		for (String pattern : noFilterUrl) {
			if (url.indexOf(pattern) != -1) {
				return true;
			}
		}
		return false;
	}

	private boolean isIncludeNoJson(String url) {
		for (String noJsonUrl : noJsonUrls) {
			if (noJsonUrl.indexOf(url) != -1) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 以JSON格式输出
	 *
	 * @param response
	 */
	protected void responseOutWithJson(HttpServletResponse response,
	                                   Object responseObject) {
		//将实体对象转换为JSON Object转换
		JSONObject responseJSONObject = JSONObject.fromObject(responseObject);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.append(responseJSONObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}