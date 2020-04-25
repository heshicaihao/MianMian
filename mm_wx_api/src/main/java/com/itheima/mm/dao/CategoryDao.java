package com.itheima.mm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuyp
 * @date 2020/03/11
 */
public interface CategoryDao {
    List<Map<String, Object>> listByCatalog(HashMap hashMap);
}
