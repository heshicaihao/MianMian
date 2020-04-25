package com.itheima.mm.controller;

import com.itheima.mm.entity.Result;
import com.itheima.mm.framework.annotation.Controller;
import com.itheima.mm.framework.annotation.RequestMapping;
import com.itheima.mm.pojo.Dict;
import com.itheima.mm.service.DictService;
import com.itheima.mm.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author liuyp
 * @date 2020/03/06
 */
@Controller
public class DictController {
    private DictService dictService = new DictService();

    @RequestMapping("/dict/complexList")
    public void complexList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<Dict> dictList = dictService.complexList();
            JsonUtils.printResult(response, new Result(true, "加载地区列表成功", dictList));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response, new Result(false, "加载地区列表失败"));
        }
    }
}
