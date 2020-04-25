package com.itheima.mm.service;

import com.itheima.mm.dao.CompanyDao;
import com.itheima.mm.pojo.Company;
import com.itheima.mm.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;

/**
 * 快捷键：
 *      F2：快速定位到下一个异常
 * @author liuyp
 * @date 2020/03/07
 */
public class CompanyService {
    public List<Company> complexList() throws IOException {
        SqlSession session = SqlSessionFactoryUtils.openSqlSession();
        CompanyDao companyDao = session.getMapper(CompanyDao.class);
        List<Company> companyList = companyDao.complexList();
        SqlSessionFactoryUtils.commitAndClose(session);
        return companyList;
    }
}
