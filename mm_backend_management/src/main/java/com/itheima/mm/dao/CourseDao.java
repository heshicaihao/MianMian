package com.itheima.mm.dao;

import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.pojo.Course;

import java.util.List;

/**
 * @author liuyp
 * @date 2020/03/04
 */
public interface CourseDao {
    /**
     * 根据搜索条件，查询总数量
     * @param queryPageBean 搜索条件
     * @return 总数量
     */
    Long total(QueryPageBean queryPageBean);

    /**
     * 根据搜索条件，查询学科列表
     * @param queryPageBean 搜索条件
     * @return 学科列表
     */
    List<Course> pageList(QueryPageBean queryPageBean);

    void add(Course course);

    void update(Course course);

    List<Course> simpleList();

    /**
     * 查询学科列表。要关联加载 目录列表、标签列表
     * @return
     */
    List<Course> complexList();
}
