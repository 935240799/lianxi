package com.itheima.portal.take_delivery;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.itheima.bos.domain.ake_delivery.Order;

/**  
 * ClassName:OrderService <br/>  
 * Function:  <br/>  
 * Date:     2018年12月21日 下午5:14:08 <br/>       
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface OrderService {
    
    @POST
    @Path("/saveOrder")
    void saveOrder(Order order);
    
}
  
