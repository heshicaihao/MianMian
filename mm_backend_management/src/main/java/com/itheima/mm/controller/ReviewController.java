package com.itheima.mm.controller;

import com.itheima.mm.entity.Result;
import com.itheima.mm.framework.annotation.Controller;
import com.itheima.mm.framework.annotation.RequestMapping;
import com.itheima.mm.pojo.ReviewLog;
import com.itheima.mm.pojo.User;
import com.itheima.mm.security.PreAurhorize;
import com.itheima.mm.service.ReviewLogService;
import com.itheima.mm.utils.DateUtils;
import com.itheima.mm.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author liuyp
 * @date 2020/03/07
 */
@Controller
public class ReviewController {
    private ReviewLogService reviewLogService = new ReviewLogService();

    /**
     * 表示/review/reviewQuestion，需要有QUESTION_REVIEW_UPDATE权限 才可以访问
     */
    @PreAurhorize("QUESTION_REVIEW_UPDATE")
    @RequestMapping("/review/reviewQuestion")
    public void reviewQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //接收参数，封装实体
            ReviewLog reviewLog = JsonUtils.parseJSON2Object(request, ReviewLog.class);
            //补全数据：审核人是谁（当前用户审核）， 审查时间是什么时候（当前时间）
            reviewLog.setCreateDate(DateUtils.parseDate2String(new Date()));
            User user = (User) request.getSession().getAttribute("user");
            if (user != null) {
                reviewLog.setUserId(user.getId());
            }

            //调用service，审核题目
            reviewLogService.reviewQuestion(reviewLog);
            JsonUtils.printResult(response, new Result(true, "审核操作完成"));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response, new Result(false, "审核操作失败"));
        }
    }
}
