package com.itheima.bos.fore.web.action;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.ake_delivery.Order;
import com.itheima.bos.domain.base.Area;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**  
 * ClassName:OrderAction <br/>  
 * Function:  <br/>  
 * Date:     2018年12月21日 下午4:33:22 <br/>       
 */
@Namespace("/")
@Controller
@ParentPackage("struts-default")
@Scope("prototype")
public class OrderAction extends ActionSupport implements ModelDriven<Order>{

    private Order model = new Order();
    
    @Override
    public Order getModel() {
        return model;
    }
    
    private String sendAreaInfo;
    private String recAreaInfo;

    public void setSendAreaInfo(String sendAreaInfo) {
        this.sendAreaInfo = sendAreaInfo;
    }
    public void setRecAreaInfo(String recAreaInfo) {
        this.recAreaInfo = recAreaInfo;
    }
    
    @Action(value="orderAction_add",results={@Result(name="success",location="/index.html",type="redirect")})
    public String saveOrder(){
        
        if (StringUtils.isNotEmpty(sendAreaInfo)) {
            //切割数据
            String[] split = sendAreaInfo.split("/");
            //去掉省、市、区   province   city   district
            String province = split[0].substring(0, split[0].length()-1);
            String city = split[1].substring(0, split[1].length()-1);
            String district = split[2].substring(0, split[2].length()-1);
            
            //封装到Area 
            Area area = new Area();
            area.setProvince(province);
            area.setCity(city);
            area.setDistrict(district);
            
            //设置sendAreaInfo数据
            model.setSendArea(area);
        }
        
        if (StringUtils.isNotEmpty(recAreaInfo)) {
            //切割数据
            String[] split = recAreaInfo.split("/");
            
            String province = split[0].substring(0, split[0].length()-1);
            String city = split[1].substring(0, split[1].length()-1);
            String district = split[2].substring(0, split[2].length()-1);
            
            Area area = new Area();
            area.setProvince(province);
            area.setCity(city);
            area.setDistrict(district);
            
            model.setRecArea(area);
            
        }
        
        //调用Webservlet保存数据
        WebClient.create("http://localhost:8080/bos_management_web/webService/orderService/saveOrder")
                            .type(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .post(model);
        
        return SUCCESS;
    }
}
  
