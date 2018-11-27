package cn.csdb.controller;

import cn.csdb.model.User;
import cn.csdb.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pirate on 2018/5/7.
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    /*@RequestMapping("/login")
    public String login(User user, Model model){

        user = userService.getUserById();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new
                UsernamePasswordToken(user.getLoginId(),user.getPassword());
        try {
            subject.login(usernamePasswordToken);
            return "index";
        } catch (Exception e) {
            //model.addAttribute("msg", "用户名或者密码错误,登陆失败");
            e.printStackTrace();
            return "500";
        }
    }*/
    @RequestMapping(value = "logout")
    public String logout(HttpServletRequest request) {
        return "redirect:/";
    }

}
