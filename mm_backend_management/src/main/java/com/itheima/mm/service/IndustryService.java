package com.itheima.mm.service;

import com.itheima.mm.dao.IndustryDao;
import com.itheima.mm.pojo.Industry;
import com.itheima.mm.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;

/**
 * @author liuyp
 * @date 2020/03/07
 */
public class IndustryService {
    public List<Industry> simpleList() throws IOException {
        SqlSession session = SqlSessionFactoryUtils.openSqlSession();
        IndustryDao industryDao = session.getMapper(IndustryDao.class);
        List<Industry> industryList = industryDao.simpleList();
        SqlSessionFactoryUtils.commitAndClose(session);
        return industryList;
    }
}
