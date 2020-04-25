package com.itheima.mm.controller;

import com.itheima.mm.entity.Result;
import com.itheima.mm.framework.annotation.Controller;
import com.itheima.mm.framework.annotation.RequestMapping;
import com.itheima.mm.pojo.Industry;
import com.itheima.mm.service.IndustryService;
import com.itheima.mm.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author liuyp
 * @date 2020/03/07
 */
@Controller
public class IndustryController {
    private IndustryService industryService = new IndustryService();

    @RequestMapping("/industry/simpleList")
    public void simpleList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<Industry> industryList = industryService.simpleList();
            JsonUtils.printResult(response, new Result(true, "加载行业方向列表成功", industryList));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response, new Result(false, "加载行业方向列表失败"));
        }
    }
}
