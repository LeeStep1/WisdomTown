package com.bit.officialdoc.filter;

import com.bit.common.BaseConst;
import com.bit.core.utils.CacheUtil;
import com.bit.utils.StringUtil;
import com.bit.utils.thread.RequestThreadBinder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

@WebFilter(urlPatterns = {"/*"}, filterName = "tokenAuthorFilter")
public class TokenFilter implements Filter {

    private static Log logger = LogFactory.getLog(TokenFilter.class);

    @Autowired
    private CacheUtil cacheUtil;

    private final String tokenName = "at";

    private final String terminalName = "tid";



    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        logger.info("----------------过滤器初始化------------------------");
        ServletContext sc = filterConfig.getServletContext();
        WebApplicationContext cxt = WebApplicationContextUtils.getWebApplicationContext(sc);
        if (cxt != null && cxt.getBean(CacheUtil.class) != null && cacheUtil == null) {
            cacheUtil = (CacheUtil) cxt.getBean(CacheUtil.class);
        }
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestType = request.getHeader(BaseConst.FLITER_FLAG);
        String userInfo = request.getHeader(BaseConst.USERTOKEN_PREFIX);
        if (StringUtil.isNotEmpty(requestType)){
            if (requestType.equals(BaseConst.FILTER)){
                RequestThreadBinder.bindUser( URLDecoder.decode(userInfo, "UTF-8"));
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {
        logger.info("--------------过滤器销毁--------------");
    }



}
