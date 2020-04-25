package com.itheima.mm.controller;

import com.alibaba.fastjson.JSONObject;
import com.itheima.mm.framework.annotation.Controller;
import com.itheima.mm.framework.annotation.RequestMapping;
import com.itheima.mm.pojo.WxMember;
import com.itheima.mm.service.MemberService;
import com.itheima.mm.utils.DateUtils;
import com.itheima.mm.utils.JsonUtils;
import com.itheima.mm.utils.WxUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuyp
 * @date 2020/03/11
 */
@Controller
public class MemberController {
    private MemberService memberService = new MemberService();

    /**
     * 保存微信帐号数据
     */
    @RequestMapping("/member/login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //1.接收参数，封装实体
            HashMap hashMap = JsonUtils.parseJSON2Object(request, HashMap.class);

            //2.根据code+AppID+AppSecret，从微信公众平台换取 session_key和openid
            String code = (String) hashMap.get("code");
            JSONObject jsonObject = WxUtil.get(code);

            String openId = jsonObject.getString("openid");

            //3.根据openId，从数据库t_wx_member表里查询帐号数据
            WxMember member = memberService.findByOpenId(openId);

            //4.如果数据库里没有对应的微信帐号，就要把微信帐号数据解密，然后保存到数据库里
            if (member == null) {
                String encryptedData = (String) hashMap.get("encryptedData");
                String sessionKey = (String) jsonObject.get("session_key");
                String iv = (String) hashMap.get("iv");
                JSONObject userInfo = WxUtil.getUserInfo(encryptedData, sessionKey, iv);

                //如果微信帐号里有特殊符号，比如emoji表情。数据库使用utf8存储不了，存储时会报错
                //解决方案：设置MySql使用utf8mb4字符集，可以存储emoji表情
                member = userInfo.toJavaObject(WxMember.class);
                member.setCreateTime(DateUtils.parseDate2String(new Date()));
                memberService.save(member);
            }

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("token", openId);
            resultMap.put("userInfo", member);

            JsonUtils.printResult(response, resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response, null);
        }
    }

    @RequestMapping("/member/setCityAndCourse")
    public void setCityAndCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //1. 接收参数，封装实体
            HashMap hashMap = JsonUtils.parseJSON2Object(request, HashMap.class);

            //2. 接收请求头Authorization: Bearer otIbn5SclH6wr_pOilXPmdUqvDw0  ，从中得到openId
            String openId = request.getHeader("Authorization");
            openId = openId.substring(7);
            hashMap.put("openId", openId);

            //3. 调用service，完成功能
            memberService.setCityAndCourse(hashMap);
            JsonUtils.printResult(response, "设置城市和学科完成");
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response, "设置城市和学科失败");
        }
    }
}
