package com.itheima.mm.service;

import com.itheima.mm.constants.Constants;
import com.itheima.mm.dao.QuestionDao;
import com.itheima.mm.dao.ReviewLogDao;
import com.itheima.mm.pojo.ReviewLog;
import com.itheima.mm.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuyp
 * @date 2020/03/07
 */
public class ReviewLogService {
    /**
     * 审核题目
     */
    public void reviewQuestion(ReviewLog reviewLog) throws IOException {
        SqlSession session = SqlSessionFactoryUtils.openSqlSession();
        ReviewLogDao reviewLogDao = session.getMapper(ReviewLogDao.class);

        //1. 插入一条审核记录
        reviewLogDao.addLog(reviewLog);

        //2. 更新题目的审核状态和发布状态：执行SQL
        QuestionDao questionDao = session.getMapper(QuestionDao.class);
        Map<String, Object> params = new HashMap<>();
        params.put("questionId", reviewLog.getQuestionId());

        // update t_question set review_status = ?, status = ?, is_classic = ? where id = ?
        if (reviewLog.getStatus() == Constants.QUESTION_REVIEWED) {
            //如果审核通过：review_status状态值是1, status状态值是1，is_classic值是1（精选题目）
            params.put("reviewStatus", Constants.QUESTION_REVIEWED);
            params.put("status", Constants.QUESTION_PUBLISHED);
            params.put("isClassic", 1);
        }else if(reviewLog.getStatus() == Constants.QUESTION_REJECT_REVIEW){
            //如果审核拒绝：review_status状态值是2，status状态值是0，is_classic值是0（基础题目）
            params.put("reviewStatus", Constants.QUESTION_REJECT_REVIEW);
            params.put("status", Constants.QUESTION_PRE_PUBLISH);
            params.put("isClassic", 0);
        }

        questionDao.review(params);

        SqlSessionFactoryUtils.commitAndClose(session);
    }
}
