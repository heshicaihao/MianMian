package com.itheima.mm.dao;

import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.pojo.Question;

import java.util.List;
import java.util.Map;

/**
 * @author liuyp
 * @date 2020/03/06
 */
public interface QuestionDao {
    Long total(QueryPageBean queryPageBean);

    List<Question> pageList(QueryPageBean queryPageBean);

    void add(Question question);

    void review(Map<String, Object> params);
}
