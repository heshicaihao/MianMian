package com.itheima.mm.controller;

import com.itheima.mm.entity.Result;
import com.itheima.mm.framework.annotation.Controller;
import com.itheima.mm.framework.annotation.RequestMapping;
import com.itheima.mm.pojo.User;
import com.itheima.mm.service.UserService;
import com.itheima.mm.utils.JsonUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liuyp
 * @date 2020/03/03
 */
@Controller
public class UserController {
    private UserService userService = new UserService();

    @RequestMapping("/user/login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //接收客户端提交的参数，封装成User对象（其中有两项数据：username、password）
            User loginUser = JsonUtils.parseJSON2Object(request, User.class);
            //完成功能：调用service进行登录校验
            User user = userService.login(loginUser);
            //处理结果：
            if (user != null) {
                //登录成功。要把登录的用户保存到session里，当其它功能需要获取当前用户时，直接从session里获取
                request.getSession().setAttribute("user", user);
                JsonUtils.printResult(response, new Result(true, "登录成功"));
            }else{
                //登录失败
                JsonUtils.printResult(response, new Result(false, "用户名或密码错误"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response, new Result(false, "登录失败"));
        }
    }

    @RequestMapping("/user/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //加上try...catch：1. 是为了跟其它方法保持一致； 2. 提高程序健壮性。  实际上不加try...catch也可以
        try {
            //销毁session对象
            request.getSession().invalidate();
            JsonUtils.printResult(response, new Result(true, "退出成功"));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response, new Result(false, "退出失败"));
        }
    }
}
