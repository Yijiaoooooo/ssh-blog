package cn.Yijia.web;

import cn.Yijia.domain.User;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static cn.Yijia.cons.CommonConstant.LOGIN_TO_URL;
import static cn.Yijia.cons.CommonConstant.USER_CONTEXT;

public class ForumFilter implements Filter {
    private static final String FILTERED_REQUEST = "@@session_context_filtered_request";

    // ① 不需要登录即可访问的URI资源
    private static final String[] INHERENT_ESCAPE_URIS = { "/index.jsp",
            "/index.html", "/login.jsp", "/login/doLogin.html",
            "/register.jsp", "/register.html", "/board/listBoardTopics-",
            "/board/listTopicPosts-" };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(servletRequest != null && servletRequest.getAttribute(FILTERED_REQUEST) != null) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            assert servletRequest != null;
            servletRequest.setAttribute(FILTERED_REQUEST, true);

            HttpServletRequest request = (HttpServletRequest)servletRequest;
            User userContext = getSessionUser(request);
            String requestUri = request.getRequestURI();

            if(userContext == null && isURILogin(requestUri, request)) {
                if(StringUtils.isNotEmpty(request.getQueryString())) {
                    requestUri = requestUri + "?" + request.getQueryString();
                }

                request.getSession().setAttribute(LOGIN_TO_URL, requestUri);

                request.getRequestDispatcher("/login.jsp").forward(servletRequest, servletResponse);

                return;
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }

    private boolean isURILogin(String requestURI, HttpServletRequest request) {
        if(request.getContextPath().equalsIgnoreCase(requestURI) ||
            (request.getContextPath()+ "/").equalsIgnoreCase(requestURI)) {
            return false;
        }
        for(String uri : INHERENT_ESCAPE_URIS) {
            if(uri != null && uri.contains(uri)) {
                return false;
            }
        }
        return true;
    }

    public User getSessionUser(HttpServletRequest request) {
        return (User)request.getSession().getAttribute(USER_CONTEXT);
    }
}
