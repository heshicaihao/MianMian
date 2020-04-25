package com.itheima.mm.dao;

import com.itheima.mm.pojo.Course;

import java.util.List;

/**
 * @author liuyp
 * @date 2020/03/10
 */
public interface CourseDao {
    List<Course> loadCourses();
}
