package com.itheima.bos.web.action.take_delivery;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.ake_delivery.WayBill;
import com.itheima.bos.web.action.CommonAction;
import com.itheima.portal.take_delivery.WaybillService;

/**  
 * ClassName:WaybillAction <br/>  
 * Function:  <br/>  
 * Date:     2018年12月30日 下午5:11:25 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class WaybillAction extends CommonAction<WayBill>{

    public WaybillAction() {
        super(WayBill.class);  
    }
    
    @Autowired
    private WaybillService waybillService;
    
    @Action("waybillAction_save")
    public String save() throws IOException{
        
        String msg = "0";
        
        try {
//            int i = 1/0;
            waybillService.save(getModel());
        } catch (Exception e) {
            e.printStackTrace();  
            msg = "1";
        }
        
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(msg);
        
        return NONE;
    }
}
  
