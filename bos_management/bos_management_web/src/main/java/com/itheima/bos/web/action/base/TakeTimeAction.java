package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.service.TakeTimeService;
import com.itheima.bos.web.action.CommonAction;

/**  
 * ClassName:TakeTimeAction <br/>  
 * Function:  <br/>  
 * Date:     2018年12月20日 上午10:33:56 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class TakeTimeAction extends CommonAction<TakeTime>{

    public TakeTimeAction() {
        super(TakeTime.class);  
    }
    
    @Autowired
    private TakeTimeService takeTimeService;
    
    @Action("takeTimeAction_listajax")
    public String listajax() throws IOException{
        
        List<TakeTime> list = takeTimeService.findAll();
        
        list2json(list, null);
        
        return NONE;
    }
}
  
