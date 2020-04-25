package com.itheima.mm.dao;

import com.itheima.mm.pojo.Permission;

import java.util.List;

/**
 * @author liuyp
 * @date 2020/03/08
 */
public interface PermissionDao {
    List<Permission> findPermissionByRoleId(Integer id);
}
