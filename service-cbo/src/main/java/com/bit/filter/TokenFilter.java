package com.bit.filter;

import com.bit.common.BaseConst;
import com.bit.utils.StringUtil;
import com.bit.utils.thread.RequestThreadBinder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;

@WebFilter(urlPatterns = {"/*"}, filterName = "tokenAuthorFilter")
public class TokenFilter implements Filter {

    private static Log logger = LogFactory.getLog(TokenFilter.class);

    @Override
    public void init(FilterConfig filterConfig)  {
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if(!request.getRequestURI().contains("/druid")){
            String requestType = request.getHeader(BaseConst.FLITER_FLAG);
            String userInfo = request.getHeader(BaseConst.USERTOKEN_PREFIX);
            if (StringUtil.isNotEmpty(requestType)){
                if (requestType.equals(BaseConst.FILTER)){
                    RequestThreadBinder.bindUser( URLDecoder.decode(userInfo, "UTF-8"));
                }
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }



}
