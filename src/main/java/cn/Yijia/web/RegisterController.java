package cn.Yijia.web;

import cn.Yijia.domain.User;
import cn.Yijia.exception.UserExistException;
import cn.Yijia.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/register")
public class RegisterController extends BaseController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView register(HttpServletRequest request, User user) {
        User u = new User();
        String errorMsg = "";
        ModelAndView mav = new ModelAndView();

        try {
            userService.register(user);
        } catch(UserExistException e) {
            mav.addObject(ERROP_MSG_KEY, "用户已存在");
            mav.setViewName("forward: /register.jsp");
        }

        setSessionUser(request, user);
        mav.setViewName("success" );

        return mav;
    }

    public String register(HttpServletRequest request) {
        return "register/jsp";
    }
}
