package com.itheima.mm.service;

import com.itheima.mm.dao.DictDao;
import com.itheima.mm.pojo.Dict;
import com.itheima.mm.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;

/**
 * @author liuyp
 * @date 2020/03/06
 */
public class DictService {
    public List<Dict> complexList() throws IOException {
        SqlSession session = SqlSessionFactoryUtils.openSqlSession();
        DictDao dictDao = session.getMapper(DictDao.class);
        List<Dict> dictList = dictDao.complexList();
        SqlSessionFactoryUtils.commitAndClose(session);
        return dictList;
    }
}
