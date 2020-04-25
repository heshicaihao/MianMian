package com.itheima.mm.dao;

import com.itheima.mm.pojo.Dict;

import java.util.List;

/**
 * @author liuyp
 * @date 2020/03/06
 */
public interface DictDao {
    List<Dict> complexList();

    /*List<Dict> subList(Integer pid);*/
}
