package com.itheima.mm.dao;

import com.itheima.mm.pojo.User;
import org.apache.ibatis.annotations.Select;

/**
 * @author liuyp
 * @date 2020/03/03
 */
public interface UserDao {
    /**
     * 登录校验。
     * @param loginUser 登录信息，其中有username和password
     */
    @Select("select * from t_user where username = #{username} and password = #{password}")
    User login(User loginUser);
}
