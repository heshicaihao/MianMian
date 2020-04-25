package com.itheima.mm.dao;

import com.itheima.mm.pojo.Tag;

import java.util.List;
import java.util.Map;

/**
 * @author liuyp
 * @date 2020/03/06
 */
public interface TagDao {
    List<Tag> listByCourseId(Integer courseId);

    void addTagAndQuestionRef(Map<String, Object> map);
}
