package com.itheima.mm.service;

import com.itheima.mm.dao.CourseDao;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.pojo.Course;
import com.itheima.mm.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;

/**
 * @author liuyp
 * @date 2020/03/04
 */
public class CourseService {
    /**
     * 分页查询学科列表
     * @param queryPageBean 查询条件
     * @return PageResult对象
     */
    public PageResult pageList(QueryPageBean queryPageBean) throws IOException {
        SqlSession session = SqlSessionFactoryUtils.openSqlSession();
        CourseDao courseDao = session.getMapper(CourseDao.class);
        //1. 根据查询条件，查询的总数量
        Long total = courseDao.total(queryPageBean);
        //2. 根据查询条件，查询的学科列表
        List<Course> courseList = courseDao.pageList(queryPageBean);
        SqlSessionFactoryUtils.commitAndClose(session);
        return new PageResult(total, courseList);
    }

    public void add(Course course) throws IOException {
        SqlSession session = SqlSessionFactoryUtils.openSqlSession();
        CourseDao courseDao = session.getMapper(CourseDao.class);
        courseDao.add(course);
        SqlSessionFactoryUtils.commitAndClose(session);
    }

    public void update(Course course) throws IOException {
        SqlSession session = SqlSessionFactoryUtils.openSqlSession();
        CourseDao courseDao = session.getMapper(CourseDao.class);
        courseDao.update(course);
        SqlSessionFactoryUtils.commitAndClose(session);
    }

    public List<Course> simpleList() throws IOException {
        SqlSession session = SqlSessionFactoryUtils.openSqlSession();
        CourseDao courseDao = session.getMapper(CourseDao.class);
        List<Course> courseList = courseDao.simpleList();
        SqlSessionFactoryUtils.commitAndClose(session);
        return courseList;
    }

    public List<Course> complexList() throws IOException {
        SqlSession session = SqlSessionFactoryUtils.openSqlSession();
        CourseDao courseDao = session.getMapper(CourseDao.class);
        List<Course> courseList = courseDao.complexList();
        SqlSessionFactoryUtils.commitAndClose(session);
        return courseList;
    }
}
