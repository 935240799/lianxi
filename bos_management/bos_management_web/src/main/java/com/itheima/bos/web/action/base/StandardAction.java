package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.StandardService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * ClassName:StandardAction <br/>
 * Function: <br/>
 * Date: 2018年11月5日 上午11:25:54 <br/>
 */
@Namespace("/") // 等价于struts.xml文件中package节点namespace属性
@ParentPackage("struts-default") // 等价于struts.xml文件中package节点extends属性
@Controller // spring 的注解,控制层代码
@Scope("prototype") // spring 的注解,多例
public class StandardAction extends ActionSupport implements ModelDriven<Standard> {

    private Standard model = new Standard();

    @Override
    public Standard getModel() {

        return model;
    }

    @Autowired
    private StandardService standardService;

    // value : // 等价于struts.xml文件中action节点中的name属性
    // 多个结果就使用@Result注解标识
    // name : 结果
    // location: 跳转页面路径
    // type : 使用的方式,重定向或转发
    @Action(value = "standardAction_save", results = {
            @Result(name = "success", location = "/pages/base/standard.html", type = "redirect")})
    public String save() {

        standardService.save(model);
        return SUCCESS;
    }

    // 使用属性驱动获取数据
    private int page;// 第几页
    private int rows;// 每一页显示多少条数据

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    // AJAX请求不需要跳转页面
    @Action(value = "standardAction_pageQuery")
    public String pageQuery() throws IOException {

        // EasyUI的页码是从1开始的
        // SPringDataJPA的页码是从0开始的
        // 所以要-1
        PageRequest pageable = new PageRequest(page - 1, rows);
        Page<Standard> page = standardService.findAll(pageable);

        // 总数据条数
        long total = page.getTotalElements();
        // 当前页要实现的内容
        List<Standard> list = page.getContent();
        // 封装数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("rows", list);

        // JSONObject : 封装对象或map集合
        // JSONArray : 数组,list集合
        // 把对象转化为json字符串
        String json = JSONObject.fromObject(map).toString();

        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);

        return NONE;
    }

    // 查询所有的派送标准
    @Action(value = "standard_findAll")
    public String findAll() throws IOException {
        // 查询数据
        Page<Standard> page = standardService.findAll(null);
        // 获取页面的数据
        List<Standard> list = page.getContent();
//        list.toArray();
//        int a = list.size();
//        for (int i = a-1; i >= 0; i--) {
//            Standard standard = list.get(i);
//            String operator = standard.getOperator();
//            if ((""+operator).equals("作废")) {
//                System.out.println("不显示: " + standard.getName());
//                list.remove(i);
//            }
//        }
        // 转换数据为json并传回页面
        String json = JSONArray.fromObject(list).toString();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
        return NONE;

    }

    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }

    // 批量删除
    @Action(value = "standardAction_batchDel", results = {
            @Result(name = "success", location = "/pages/base/standard.html", type = "redirect")})
    public String batchDel() {
        standardService.batchDel(ids);
        return SUCCESS;
    }

    // 批量删除
    @Action(value = "standardAction_restore", results = {
            @Result(name = "success", location = "/pages/base/standard.html", type = "redirect")})
    public String restore() {
        standardService.restore(ids);
        return SUCCESS;
    }
}
