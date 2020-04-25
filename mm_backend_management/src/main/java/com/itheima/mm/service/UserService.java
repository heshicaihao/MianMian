package com.itheima.mm.service;

import com.itheima.mm.dao.PermissionDao;
import com.itheima.mm.dao.RoleDao;
import com.itheima.mm.dao.UserDao;
import com.itheima.mm.pojo.Permission;
import com.itheima.mm.pojo.Role;
import com.itheima.mm.pojo.User;
import com.itheima.mm.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;

import javax.management.relation.RoleStatus;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyp
 * @date 2020/03/03
 */
public class UserService {

    /**
     * 登录校验
     * @param loginUser 登录信息封装成的user对象。其中有username和password
     * @return
     */
    public User login(User loginUser) throws IOException {
        //注意：SqlSession使用原则是  随用随取，用完就关，一定要关（类似于Connection对象）
        SqlSession session = SqlSessionFactoryUtils.openSqlSession();
        UserDao userDao = session.getMapper(UserDao.class);
        User user = userDao.login(loginUser);

        //如果登录成功，就加载这个用户拥有的所有角色、所有权限。存储到user对象的authorityList里边
        if (user != null) {
            List<String> list = new ArrayList<>();

            //加载当前用户关联的角色
            RoleDao roleDao = session.getMapper(RoleDao.class);
            List<Role> roleList = roleDao.findRolesByUid(user.getId());
            if (roleList != null && roleList.size()>0) {

                PermissionDao permissionDao = session.getMapper(PermissionDao.class);

                for (Role role : roleList) {
                    //把用户 拥有的角色，保存到user的集合里
                    list.add(role.getKeyword());

                    //加载每个角色关联的权限
                    List<Permission> permissionList = permissionDao.findPermissionByRoleId(role.getId());
                    if (permissionList != null && permissionList.size()>0) {
                        for (Permission permission : permissionList) {
                            list.add(permission.getKeyword());
                        }
                    }
                }
            }

            //把加载的所有角色、授权 全部保存到user对象里
            user.setAuthorityList(list);
        }

        //注意：SqlSession对象使用完成之后，一定要关闭
        SqlSessionFactoryUtils.commitAndClose(session);
        return user;
    }
}
