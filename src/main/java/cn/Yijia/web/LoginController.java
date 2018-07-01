package cn.Yijia.web;

import bsh.StringUtil;
import cn.Yijia.cons.CommonConstant;
import cn.Yijia.domain.User;
import cn.Yijia.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping(value = "/login")
public class LoginController extends BaseController {
    @Autowired
    private UserService userService;

    @RequestMapping("/doLogin")
    public ModelAndView doLogin(HttpServletRequest request, User user) {
        User u = userService.getUserByUserName(user.getUserName());
        ModelAndView mav = new ModelAndView();
        String errorMsg = "";

        if(u == null) {
            if(user.getUserName() == null) {
                errorMsg = "用户名为空！";
            } else {
                errorMsg = "无该用户";
            }
        } else {
            if(!u.getPassword().equals(user.getPassword())) {
                errorMsg = "密码错误";
            }
            if(u.getLocked() == User.USER_LOCK) {
                errorMsg = "用户锁定";
            }

            u.setLastIp(request.getRemoteAddr());
            u.setLastVisit(new Date());

            userService.loginSuccess(u);
            setSessionUser(request, user);

            String toUrl = request.getSession().getAttribute(CommonConstant.LOGIN_TO_URL).toString();
            request.getSession().removeAttribute(CommonConstant.LOGIN_TO_URL);
            if(StringUtils.isEmpty(toUrl)) {
                toUrl = "/index.html";
            }

            mav.setViewName("redirect:" + toUrl);
        }
        mav.addObject(ERROP_MSG_KEY, errorMsg);

        return mav;
    }

    @RequestMapping(value = "/doLogout")
    public String doLogout(HttpSession session) {
        session.removeAttribute(CommonConstant.USER_CONTEXT);
        return "forward: index.jsp";
    }
}
