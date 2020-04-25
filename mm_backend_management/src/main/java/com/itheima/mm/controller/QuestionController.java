package com.itheima.mm.controller;

import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.entity.Result;
import com.itheima.mm.framework.annotation.Controller;
import com.itheima.mm.framework.annotation.RequestMapping;
import com.itheima.mm.pojo.Question;
import com.itheima.mm.pojo.User;
import com.itheima.mm.service.QuestionService;
import com.itheima.mm.utils.DateUtils;
import com.itheima.mm.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author liuyp
 * @date 2020/03/06
 */
@Controller
public class QuestionController {
    private QuestionService questionService = new QuestionService();

    @RequestMapping("/question/pageList")
    public void pageList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            QueryPageBean queryPageBean = JsonUtils.parseJSON2Object(request, QueryPageBean.class);
            if (queryPageBean == null) {
                queryPageBean = new QueryPageBean();
                queryPageBean.setCurrentPage(1);
                queryPageBean.setPageSize(10);
            }

            PageResult pageResult = questionService.pageList(queryPageBean);
            JsonUtils.printResult(response, new Result(true,"查询基础题目列表成功", pageResult));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response, new Result(false, "查询基础题目列表失败"));
        }
    }

    @RequestMapping("/question/save")
    public void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //接收参数，封装实体
            Question question = JsonUtils.parseJSON2Object(request, Question.class);
            //补全数据：createDate，创建时间
            question.setCreateDate(DateUtils.parseDate2String(new Date()));
            //补全数据：userId，录入人，是当前用户
            User user = (User) request.getSession().getAttribute("user");
            if (user != null) {
                question.setUserId(user.getId());
            }
            System.out.println(question);

            questionService.save(question);
            JsonUtils.printResult(response, new Result(true, "保存基础题目成功"));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response, new Result(false, "保存基础题目失败"));
        }

    }
}
