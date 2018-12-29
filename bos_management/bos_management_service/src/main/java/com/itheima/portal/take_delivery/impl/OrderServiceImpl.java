package com.itheima.portal.take_delivery.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.AreaRepository;
import com.itheima.bos.dao.base.FixedAreaRepository;
import com.itheima.bos.dao.take_delivery.OrderRepository;
import com.itheima.bos.dao.take_delivery.WorkbillRepository;
import com.itheima.bos.domain.ake_delivery.Order;
import com.itheima.bos.domain.ake_delivery.WorkBill;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.portal.take_delivery.OrderService;

/**  
 * ClassName:OrderServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年12月21日 下午5:17:33 <br/>       
 * 根据发件地址完全匹配(发件地址数据来自CRM系统中customer表,而且这个客户一定要和某一个定区关联)
 * 根据发件地址模糊匹配(发件地址数据来自后台系统中subArea表,而且这个分区一定要和某一个定区关联,这个分区所属的区域数据一定要对得上号)
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private FixedAreaRepository fixedAreaRepository;
    @Autowired
    private WorkbillRepository workbillRepository;
    
    @Override
    public void saveOrder(Order order) {
          
        Area sendArea = order.getSendArea();
        if (sendArea!=null) {
           Area sendAreaDB = areaRepository.findByProvinceAndCityAndDistrict(
                    sendArea.getProvince(),
                    sendArea.getCity(),
                    sendArea.getDistrict()
                    );
           
           //把瞬时态转换为持久态
           order.setSendArea(sendAreaDB);
        }
        
        Area recArea = order.getRecArea();
        if (recArea!=null) {
            Area recAreaDB = areaRepository.findByProvinceAndCityAndDistrict(
                    recArea.getProvince(),
                    recArea.getCity(),
                    recArea.getDistrict());
            
            order.setRecArea(recAreaDB);
            
        }
        
        //实现保存订单
        order.setOrderNum(UUID.randomUUID().toString().replaceAll("-", ""));
        order.setOrderTime(new Date());
        orderRepository.save(order);
        
        //自动根据分区来分单
        String sendAddress = order.getSendAddress();
        if (StringUtils.isNotEmpty(sendAddress)) {
            String fixedAreaId = WebClient.create("http://localhost:8088/crm/webService/customerService/findFixedAreaIdByAddress")
            .type(MediaType.APPLICATION_JSON)
            .query("address", sendAddress)
            .accept(MediaType.APPLICATION_JSON)
            .get(String.class);
            
            if (StringUtils.isNotEmpty(fixedAreaId)) {
                //根据ID查询分区
                FixedArea fixedArea = fixedAreaRepository.findOne(Long.parseLong(fixedAreaId));
                if (fixedArea != null) {
                    //查询快递员
                    Set<Courier> couriers = fixedArea.getCouriers();
                    if (!couriers.isEmpty()) {
                        Iterator<Courier> iterator = couriers.iterator();
                        Courier courier = iterator.next();
                        
                        //指派快递员
                        order.setCourier(courier);
                        
                        //创建工单
                        WorkBill workBill = new WorkBill();
                        workBill.setAttachbilltimes(0);
                        workBill.setBuildtime(new Date());
                        workBill.setCourier(courier);
                        workBill.setOrder(order);
                        
                        workBill.setPickstate("新单");
                        
                        workBill.setRemark(order.getRemark());
                        workBill.setType("新");
                        workBill.setSmsNumber("998");
                        
                        workbillRepository.save(workBill);
                        
                        // 发送短信,推送一个通知
                        // 中断代码的执行

                        order.setOrderType("自动分单");
                        
                        return;
                        
                    }
                }
            }
            
        }
        order.setOrderType("人工分单");
    }
    
}
  
