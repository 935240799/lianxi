package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.FixedAreaService;
import com.itheima.bos.web.action.CommonAction;
import com.itheima.crm.domain.Customer;

import net.sf.json.JsonConfig;

/**
 * ClassName:FixedAreaAction <br/>
 * Function: <br/>
 * Date: 2018年12月19日 下午4:01:45 <br/>
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class FixedAreaAction extends CommonAction<FixedArea> {

    public FixedAreaAction() {
        super(FixedArea.class);
    }

    @Autowired
    private FixedAreaService fixedAreaService;

    @Action(value = "fixedAreaAction_save", results = {
            @Result(name = "success", location = "/pages/base/fixed_area.html", type = "redirect")})
    public String save() {

        fixedAreaService.save(getModel());
        return SUCCESS;
    }

    // AJAX请求不需要跳转页面
    @Action("fixedAreaAction_pageQuery")
    public String pageQuery() throws IOException {

        // EasyUI的页码是从1开始的
        // SPringDataJPA的页码是从0开始的
        // 所以要-1

        Pageable pageable = new PageRequest(page - 1, rows);

        Page<FixedArea> page = fixedAreaService.findAll(pageable);

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas", "couriers"});

        page2json(page, jsonConfig);
        return NONE;
    }

    // 向CRM系统发起请求,查询未关联定区的客户
    @Action(value = "fixedAreaAction_findUnAssociatedCustomers")
    public String findCustomersUnAssociated() throws IOException {

        List<Customer> list = (List<Customer>) WebClient.create(
                "http://localhost:8088/crm/webService/customerService/findCustomersUnAssociated")//
                .type(MediaType.APPLICATION_JSON)//
                .accept(MediaType.APPLICATION_JSON)//
                .getCollection(Customer.class);

        list2json(list, null);

        return NONE;
    }

    // 向crm系统发送请求,查询已关联定区的客户
    @Action(value = "fixedAreaAction_findAssociatedCustomers")
    public String findAssociatedCustomers() throws IOException {
        List<Customer> list = (List<Customer>) WebClient
                .create("http://localhost:8088/crm/webService/customerService/findCustomersAssociated2FixedArea")
                .query("fixedAreaId", getModel().getId()).type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);

        list2json(list, null);
        return NONE;

    }

    // 使用属性驱动获取要关联到指定定区的客户ID
    private Long[] CustomerIds;

    public void setCustomerIds(Long[] customerIds) {
        this.CustomerIds = customerIds;
    }

    // 向CRM系统发起请求,关联客户
    @Action(value = "fixedAreaAction_assignCustomers2FixedArea", results = {
            @Result(name = "success", location = "/pages/base/fixed_area.html", type = "redirect")})
    public String assignCustomers2FixedArea() throws IOException {
        WebClient
                .create("http://localhost:8088/crm/webService/customerService/assignCustomers2FixedArea")
                .query("fixedAreaId", getModel().getId()).query("customerIds", CustomerIds)
                .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(null);
        return SUCCESS;
    }

    // 使用属性驱动获取快递员和时间的ID
    private Long courierId;
    private Long takeTimeId;

    public void setCourierId(Long courierId) {
        this.courierId = courierId;
    }

    public void setTakeTimeId(Long takeTimeId) {
        this.takeTimeId = takeTimeId;
    }

    // 关联快递员
    @Action(value = "fixedAreaAction_associationCourierToFixedArea", results = {
            @Result(name = "success", location = "/pages/base/fixed_area.html", type = "redirect")})
    public String associationCourierToFixedArea() throws IOException {

        fixedAreaService.associationCourierToFixedArea(getModel().getId(), courierId, takeTimeId);
        return SUCCESS;
    }

    // 使用属性驱动来获取分区id
    private Long[] subAreaIds;

    public void setSubAreaIds(Long[] subAreaIds) {
        this.subAreaIds = subAreaIds;
    }

    // 关联分区
    @Action(value = "fixedAreaAction_assignSubAreas2FixedArea", results = {
            @Result(name = "success", location = "/pages/base/fixed_area.html", type = "redirect")})
    public String assignSubAreas2FixedArea() {
        fixedAreaService.assignSubAreas2FixedArea(getModel().getId(), subAreaIds);
        return SUCCESS;
    }

    // 查询未关联定区的分区
    @Action("fixedArea_findUnAssociatedSubArea")
    public String findUnAssociatedFiedArea() throws IOException {

        List<SubArea> list = fixedAreaService.findUnAssociatedFiedArea();

        JsonConfig jsonConfig = new JsonConfig();

        jsonConfig.setExcludes(new String[] {"fixedArea", "area"});

        list2json(list, jsonConfig);

        return NONE;
    }

    // 查询已关联此定区的分区
    @Action("fixedArea_findAssociatedSubArea")
    public String findAssociatedFiedArea() throws IOException {
        List<SubArea> list = fixedAreaService.findAssociatedFiedArea(getModel().getId());
        JsonConfig jsonConfig = new JsonConfig();

        jsonConfig.setExcludes(new String[] {"fixedArea", "area"});

        list2json(list, jsonConfig);

        return NONE;
    }

//    //关联分区
//    @Action(value = "fixedAreaAction_assignSubAreas2FixedArea", 
//            results={@Result(name="success",location = "/pages/base/fixed_area.html",type="redirect")})
//    public String assignSubAreas2FixedArea(){
//        
//        return SUCCESS;
//    }
    
}
