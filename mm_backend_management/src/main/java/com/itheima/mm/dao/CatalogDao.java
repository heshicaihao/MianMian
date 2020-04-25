package com.itheima.mm.dao;

import com.itheima.mm.pojo.Catalog;

import java.util.List;

/**
 * @author liuyp
 * @date 2020/03/06
 */
public interface CatalogDao {
    List<Catalog> listByCourseId(Integer courseId);
}
