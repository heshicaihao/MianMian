package com.itheima.mm.dao;

import com.itheima.mm.pojo.WxMember;

import java.util.HashMap;

/**
 * @author liuyp
 * @date 2020/03/11
 */
public interface MemberDao {
    WxMember findByOpenId(String openId);

    void save(WxMember member);

    void setCityAndCourse(HashMap hashMap);
}
