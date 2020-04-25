package com.itheima.mm.dao;

import com.itheima.mm.pojo.Role;

import java.util.List;

/**
 * @author liuyp
 * @date 2020/03/08
 */
public interface RoleDao {
    List<Role> findRolesByUid(Integer id);
}
