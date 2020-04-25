package com.itheima.mm.dao;

import com.itheima.mm.pojo.Industry;

import java.util.List;

/**
 * @author liuyp
 * @date 2020/03/07
 */
public interface IndustryDao {
    List<Industry> simpleList();

    List<Industry> listByCompanyId(Integer companyId);
}
