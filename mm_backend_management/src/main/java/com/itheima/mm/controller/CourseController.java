package com.itheima.mm.controller;

import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.entity.Result;
import com.itheima.mm.framework.annotation.Controller;
import com.itheima.mm.framework.annotation.RequestMapping;
import com.itheima.mm.pojo.Course;
import com.itheima.mm.pojo.User;
import com.itheima.mm.service.CourseService;
import com.itheima.mm.utils.DateUtils;
import com.itheima.mm.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author liuyp
 * @date 2020/03/04
 */
@Controller
public class CourseController {
    private CourseService courseService = new CourseService();

    /**
     * 以前开发逻辑：以服务端为主导
     *  服务端完成功能，需要什么参数，我们就让客户端传递什么参数
     *  服务端返回什么结果，页面上获取并显示什么结果
     *
     * 现在开发逻辑：以客户端为主导
     *  客户端传递什么参数，我们就要接收什么参数
     *  客户端需要什么结果，我们就要准备什么结果
     */
    @RequestMapping("/course/pageList")
    public void pageList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //1. 接收参数，封装实体：客户端提交的参数，封装成什么样的对象？QueryPageBean
            QueryPageBean queryPageBean = JsonUtils.parseJSON2Object(request, QueryPageBean.class);
            if (queryPageBean == null) {
                //为了保证程序的健壮性：处理一下空参的问题
                queryPageBean = new QueryPageBean();
                queryPageBean.setPageSize(10);
                queryPageBean.setCurrentPage(1);
            }

            //2. 完成功能：调用service，准备客户端需要的数据。客户端需要什么样的数据？
            PageResult pageResult = courseService.pageList(queryPageBean);
            JsonUtils.printResult(response, new Result(true, "查询学科列表成功", pageResult));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response, new Result(false, "查询学科列表失败"));
        }
    }

    @RequestMapping("/course/add")
    public void add(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //接收参数，封装成Course对象
            Course course = JsonUtils.parseJSON2Object(request, Course.class);
            //补充数据：创建时间（当前时间）
            course.setCreateDate(DateUtils.parseDate2String(new Date()));
            //补充数据：创建人（当前登录的用户）
            User user = (User) request.getSession().getAttribute("user");
            if (user != null) {
                course.setUserId(user.getId());
            }

            //调用service，添加到数据库
            courseService.add(course);
            JsonUtils.printResult(response, new Result(true, "添加学科成功"));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response, new Result(false, "添加学科失败"));
        }
    }

    @RequestMapping("/course/update")
    public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //提交的数据：{"id":22,"name":"UI/UE","isShow":0}
            Course course = JsonUtils.parseJSON2Object(request, Course.class);
            //调用service，完成修改功能
            courseService.update(course);
            JsonUtils.printResult(response, new Result(true, "修改学科成功"));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response, new Result(false, "修改学科失败"));
        }
    }

    /**
     * 查询所有学科，用于页面上的学科下拉列表
     */
    @RequestMapping("/course/simpleList")
    public void simpleList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<Course> courseList = courseService.simpleList();
            JsonUtils.printResult(response, new Result(true, "加载学科列表成功", courseList));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response, new Result(false, "加载学科列表失败"));
        }
    }

    @RequestMapping("/course/complexList")
    public void complexList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<Course> courseList = courseService.complexList();
            JsonUtils.printResult(response, new Result(true, "加载学科列表成功",courseList));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response, new Result(false, "加载学科列表失败"));
        }
    }
}
