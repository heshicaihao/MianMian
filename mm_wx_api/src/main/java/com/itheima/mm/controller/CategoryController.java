package com.itheima.mm.controller;

import com.itheima.mm.framework.annotation.Controller;
import com.itheima.mm.framework.annotation.RequestMapping;
import com.itheima.mm.pojo.WxMember;
import com.itheima.mm.service.CategoryService;
import com.itheima.mm.service.MemberService;
import com.itheima.mm.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuyp
 * @date 2020/03/11
 */
@Controller
public class CategoryController {

    private MemberService memberService = new MemberService();
    private CategoryService categoryService = new CategoryService();

    @RequestMapping("/category/list")
    public void list(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            //1. 接收参数，封装实体
            HashMap hashMap = JsonUtils.parseJSON2Object(request, HashMap.class);

            //2. 获取微信用户的openId： Authorization: Bearer otIbn5SclH6wr_pOilXPmdUqvDw0
            String openId = request.getHeader("Authorization");
            openId = openId.substring(7);
            hashMap.put("openId", openId);

            //3. 根据openId，加载微信用户数据：用户的id、用户选择的地区id、用户选择的学科id
            WxMember member = memberService.findByOpenId(openId);
            hashMap.put("memberId", member.getId());
            hashMap.put("cityId", member.getCityId());
            hashMap.put("courseId", member.getCourseId());

            //4. 调用service，加载题库列表
            List<Map<String,Object>> list = categoryService.list(hashMap);

            //5. 封装结果，返回结果
            resultMap.put("errmsg", "加载列表成功");
            resultMap.put("items", list);

            JsonUtils.printResult(response, resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("errmsg", "加载列表失败");
            JsonUtils.printResult(response, resultMap);
        }

    }
}
