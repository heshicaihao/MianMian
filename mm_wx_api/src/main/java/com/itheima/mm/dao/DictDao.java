package com.itheima.mm.dao;

import com.itheima.mm.pojo.Dict;

import java.util.List;

/**
 * @author liuyp
 * @date 2020/03/10
 */
public interface DictDao {
    Dict findByName(String cityName);

    List<Dict> findByDataTag(Integer dataTag);
}
