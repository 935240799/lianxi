package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.dom4j.CDATA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.CourierService;
import com.itheima.bos.web.action.CommonAction;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;


/**  
 * ClassName:CourierAction <br/>  
 * Function:  <br/>  
 * Date:     2018年12月16日 下午2:51:31 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class CourierAction extends CommonAction<Courier> implements ModelDriven<Courier> {

    public CourierAction() {
          
        super(Courier.class);  
        
    }

    
    @Autowired
    private CourierService courierService;
    
    @Action(value="courierAction_save",results={@Result(name="success",location="/pages/base/courier.html",type="redirect")})
    public String save(){
        courierService.save(getModel());
        return SUCCESS;
    }
    
    private int page;
    private int rows;
    
    public void setPage(int page) {
        this.page = page;
    }



    public void setRows(int rows) {
        this.rows = rows;
    }

    @Action("courierAction_pageQuery")
    public String pageQuery() throws IOException{
        
        //构造查询条件
        Specification<Courier> specification = new Specification<Courier>() {

            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query,
                    CriteriaBuilder cb) {
                  
                String courierNum = getModel().getCourierNum();
                String company = getModel().getCompany();
                String type = getModel().getType();
                Standard standard = getModel().getStandard();
                
                ArrayList<Predicate> list = new ArrayList<>();
                
                //快递员工号不为空,构建一个等值查询
                if (StringUtils.isNotEmpty(courierNum)) {
                    Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courierNum);
                    list.add(p1);
                }
                //公司不为空,构建一个模糊查询
                if (StringUtils.isNotEmpty(company)) {
                    Predicate p2 = cb.like(root.get("company").as(String.class), "%"+company+"%");
                    list.add(p2);
                }
                //类型不为空,构建一个等值查询
                if (StringUtils.isNotEmpty(type)) {
                    Predicate p3 = cb.like(root.get("type").as(String.class), "%"+type+"%");
                    list.add(p3);
                }
                
                
                //取派标准
                if (standard != null) {
                    String name = standard.getName();
                    System.out.println("name="+name);
                    System.out.println("standard="+standard);
                    if (StringUtils.isNotEmpty(name)) {
                        //连表查询
                        Join<Object, Object> join = root.join("standard");
                        Predicate p4 = cb.like(join.get("name").as(String.class), "%"+name+"%");
                        list.add(p4);
                    }
                }
                
                //用户没有输入查询条件
                if (list.size()==0) {
                    return null;
                }
                
                Predicate[] arr = new Predicate[list.size()];
                list.toArray(arr);
                
                return cb.and(arr);
            }};
        
        Pageable pageable = new PageRequest(page-1, rows);
        Page<Courier> page = courierService.findAll(specification,pageable);
        
        long total = page.getTotalElements();
        List<Courier> content = page.getContent();
        
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);  
        map.put("rows", content);  
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"fixedAreas","takeTime"});
        
        String json = JSONObject.fromObject(map,jsonConfig).toString();
        
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
        
        return NONE;
    }
    
    @Action("courierAction_listajax")
    public String listajax() throws IOException {
        // 查询所有的在职的快递员

        Specification<Courier> specification = new Specification<Courier>() {

            @Override
            public Predicate toPredicate(Root<Courier> root,
                    CriteriaQuery<?> query, CriteriaBuilder cb) {
                // 比较空值
                Predicate predicate =
                        cb.isNull(root.get("deltag").as(Character.class));

                return predicate;
            }
        };
        Page<Courier> p = courierService.findAll(specification, null);
        List<Courier> list = p.getContent();

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"fixedAreas", "takeTime"});
        list2json(list, jsonConfig);
        return NONE;
    }
    
    private String ids;
    
    public void setIds(String ids) {
        this.ids = ids;
    }
    
    //批量删除
    @Action(value="courierAction_batchDel",
            results = {@Result(name = "success",
            location = "/pages/base/courier.html", type = "redirect")})
    public String batchDel() {
        System.out.println(ids);
    courierService.batchDel(ids);
    return SUCCESS;
    }
    
    //批量删除
    @Action(value="courierAction_batchRestore",
            results = {@Result(name = "success",
            location = "/pages/base/courier.html", type = "redirect")})
    public String batchRestore() {
        System.out.println(ids);
        courierService.batchRestore(ids);
        return SUCCESS;
    }
        
}
  
