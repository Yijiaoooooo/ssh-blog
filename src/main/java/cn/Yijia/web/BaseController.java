package cn.Yijia.web;

import cn.Yijia.cons.CommonConstant;
import cn.Yijia.domain.User;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {
    protected static final String ERROP_MSG_KEY = "errorMsg";

    protected void setSessionUser(HttpServletRequest request, User user) {
        request.getSession().setAttribute(CommonConstant.USER_CONTEXT, user);
    }

    protected User getSessionUser(HttpServletRequest request) {
        return (User)request.getSession().getAttribute(CommonConstant.USER_CONTEXT);
    }

    protected String getAppBasePath(HttpServletRequest request, String url) {
        Assert.hasLength(url, "url不能为空");
        Assert.isTrue(url.startsWith("/"), "url必须以”/“开头");
        return request.getContextPath() + url;
    }
}
