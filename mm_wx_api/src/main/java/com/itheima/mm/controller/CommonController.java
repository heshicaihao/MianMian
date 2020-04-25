package com.itheima.mm.controller;

import com.alibaba.fastjson.JSON;
import com.itheima.mm.entity.Result;
import com.itheima.mm.framework.annotation.Controller;
import com.itheima.mm.framework.annotation.RequestMapping;
import com.itheima.mm.pojo.Course;
import com.itheima.mm.service.CommonService;
import com.itheima.mm.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author liuyp
 * @date 2020/03/10
 */
@Controller
public class CommonController {

    private CommonService commonService = new CommonService();

    @RequestMapping("/common/cities")
    public void loadCity(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //1.接收参数，封装实体
            HashMap hashMap = JsonUtils.parseJSON2Object(request, HashMap.class);
            //2.调用service，完成功能
            Map<String, Object> resultMap = commonService.loadCity(hashMap);
            //3.返回响应。因为小程序里这个功能，要求的返回数据，只要数据，没有要求flag、message，所以不需要再封装result对象
            JsonUtils.printResult(response, resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response, null);
        }
    }

    @RequestMapping("/common/courses")
    public void loadCourses(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            /*List<Course> courseList = commonService.loadCourses();
            JsonUtils.printResult(response, courseList);*/

            /**
             * 直接给客户端返回json格式的字符串，客户端小程序需要把字符串还原成js的json对象才可以使用
             * 但是：小程序里没有写还原的代码.
             *
             *
             * 我们服务端给客户端的数据，不能是字符串。还得 响应Java对象：List<Course>---不需要这样
             * fastjson提供了一些Java对象：
             *      JSONObject：本质是一个Map
             *      JSONArray：本质是一个List
             *
             * JSON.parse(coursesListStr)：把json格式的字符串，转换成JSONArray对象（List）
             */
            String coursesListStr = commonService.loadCourses();
            JsonUtils.printResult(response, JSON.parse(coursesListStr));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response, null);
        }
    }
}
