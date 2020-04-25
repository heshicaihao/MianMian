package com.itheima.mm.controller;

import com.itheima.mm.entity.Result;
import com.itheima.mm.framework.annotation.Controller;
import com.itheima.mm.framework.annotation.RequestMapping;
import com.itheima.mm.pojo.Company;
import com.itheima.mm.service.CompanyService;
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
public class CompanyController {
    private CompanyService companyService = new CompanyService();

    @RequestMapping("/company/complexList")
    public void complexList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<Company> companyList = companyService.complexList();
            JsonUtils.printResult(response, new Result(true, "加载企业列表成功", companyList));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response, new Result(false, "加载企业列表失败"));
        }
    }
}
