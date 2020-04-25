package com.itheima.mm.service;

import com.itheima.mm.dao.MemberDao;
import com.itheima.mm.pojo.WxMember;
import com.itheima.mm.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author liuyp
 * @date 2020/03/11
 */
public class MemberService {
    public WxMember findByOpenId(String openId) throws IOException {
        SqlSession session = SqlSessionFactoryUtils.openSqlSession();
        MemberDao memberDao = session.getMapper(MemberDao.class);
        WxMember member = memberDao.findByOpenId(openId);
        SqlSessionFactoryUtils.commitAndClose(session);
        return member;
    }

    public void save(WxMember member) throws IOException {
        SqlSession session = SqlSessionFactoryUtils.openSqlSession();
        MemberDao memberDao = session.getMapper(MemberDao.class);
        memberDao.save(member);
        SqlSessionFactoryUtils.commitAndClose(session);
    }

    public void setCityAndCourse(HashMap hashMap) throws IOException {
        SqlSession session = SqlSessionFactoryUtils.openSqlSession();
        MemberDao memberDao = session.getMapper(MemberDao.class);
        memberDao.setCityAndCourse(hashMap);
        SqlSessionFactoryUtils.commitAndClose(session);
    }
}
