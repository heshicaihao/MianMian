package com.itheima.mm.service;

import com.itheima.mm.dao.CategoryDao;
import com.itheima.mm.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuyp
 * @date 2020/03/11
 */
public class CategoryService {
    public List<Map<String, Object>> list(HashMap hashMap) throws IOException {

        //获取到categoryKind的值。值的类型，客户端传过来的有时候是整数，有时候是字符串
        Object kind = hashMap.get("categoryKind");
        String categoryKind = null;
        if (kind instanceof Integer) {
            categoryKind = kind + "";
        } else if (kind instanceof String) {
            categoryKind = (String) kind;
        }

        List<Map<String, Object>> list = null;

        SqlSession session = SqlSessionFactoryUtils.openSqlSession();
        CategoryDao categoryDao = session.getMapper(CategoryDao.class);
        if ("1".equals(categoryKind)) {
            //按TAG（按学科目录）
            list = categoryDao.listByCatalog(hashMap);
        }else if("2".equals(categoryKind)){
            //按企业
            //list = categoryDao.listByCompany(hashMap);
        }else if("3".equals(categoryKind)){
            //按行业方向
        }

        SqlSessionFactoryUtils.commitAndClose(session);

        return list;
    }
}
