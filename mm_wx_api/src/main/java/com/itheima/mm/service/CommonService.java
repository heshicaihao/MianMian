package com.itheima.mm.service;

import com.alibaba.fastjson.JSON;
import com.itheima.mm.dao.CourseDao;
import com.itheima.mm.dao.DictDao;
import com.itheima.mm.pojo.Course;
import com.itheima.mm.pojo.Dict;
import com.itheima.mm.utils.JedisUtils;
import com.itheima.mm.utils.LocationUtil;
import com.itheima.mm.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuyp
 * @date 2020/03/10
 */
public class CommonService {
    public Map<String, Object> loadCity(HashMap hashMap) throws IOException {

        //1. 根据参数location，从高德服务得到对应的城市名称
        String location = (String) hashMap.get("location");
        String cityName = LocationUtil.parseLocation(location);

        //微信小程序的定位，并非真实的环境，所以是根据IP模拟的定位点，所以定位点不准确
        //这个城市名称，就是定位点。大家把这个城市  和 所在的省，添加到t_dict数据库里
        System.out.println("GPS定位点的城市：" + cityName);

        //2. 调用dao，根据cityName加载Dict对象
        SqlSession session = SqlSessionFactoryUtils.openSqlSession();
        DictDao dictDao = session.getMapper(DictDao.class);
        Dict dict = dictDao.findByName(cityName);

        //3. 调用dao，根据参数fs（对应t_dict表里的data_tag字段）查询市的列表
        Integer dataTag = (Integer) hashMap.get("fs");
        List<Dict> dictList = dictDao.findByDataTag(dataTag);

        //4. 把结果封装成HashMap
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("location", dict);
        resultMap.put("citys", dictList);

        //5. 释放资源
        SqlSessionFactoryUtils.commitAndClose(session);
        return resultMap;
    }

    /*public List<Course> loadCourses() throws IOException {
        SqlSession session = SqlSessionFactoryUtils.openSqlSession();
        CourseDao courseDao = session.getMapper(CourseDao.class);
        List<Course> courseList = courseDao.loadCourses();
        SqlSessionFactoryUtils.commitAndClose(session);
        return courseList;
    }*/

    public String loadCourses() throws IOException {
            //优先从redis里加载缓存
            Jedis jedis = JedisUtils.getJedis();
            String coursesListStr = jedis.get("coursesListStr");
            if (coursesListStr == null || "".equals(coursesListStr)) {
                //redis里没有学科列表的缓存数据，需要从MySql里加载数据了
                SqlSession session = SqlSessionFactoryUtils.openSqlSession();
                CourseDao courseDao = session.getMapper(CourseDao.class);
                List<Course> courseList = courseDao.loadCourses();
                SqlSessionFactoryUtils.commitAndClose(session);

                //把MySql里查询的结果，转换成json格式的字符串，放到redis里缓存起来
                coursesListStr = JSON.toJSONString(courseList);
                jedis.set("coursesListStr", coursesListStr);
            }

            //释放资源jedis对象
            jedis.close();

            return coursesListStr;
    }
}
