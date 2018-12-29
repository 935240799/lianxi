package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.List;

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

import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.SubAreaService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;


/**  
 * ClassName:SubAreaAction <br/>  
 * Function:  <br/>  
 * Date:     2018年12月18日 下午10:15:46 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class SubAreaAction extends CommonAction<SubArea>{

    public SubAreaAction() {
        super(SubArea.class);  
    }
    @Autowired
    private SubAreaService subAreaService;

    @Action(value = "subareaAction_save", results = {@Result(name = "success",
            location = "/pages/base/sub_area.html", type = "redirect")})
    public String save() {

        subAreaService.save(getModel());
        return SUCCESS;
    }
    
    @Action("subAreaAction_pageQuery")
    public String pageQuery() throws IOException  {
        
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<SubArea> page = subAreaService.fandAll(pageable);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas","couriers"});
        
        
        page2json(page, jsonConfig);
        return NONE;
    }
    
}
  
