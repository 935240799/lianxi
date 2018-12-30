package com.itheima.portal.take_delivery.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.take_delivery.WaybillRepository;
import com.itheima.bos.domain.ake_delivery.WayBill;
import com.itheima.portal.take_delivery.WaybillService;

/**  
 * ClassName:WaybillServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年12月30日 下午5:23:39 <br/>       
 */
@Service
@Transactional
public class WaybillServiceImpl implements WaybillService {
    
    @Autowired
    private WaybillRepository waybillRepository;

    @Override
    public void save(WayBill model) {
        waybillRepository.save(model);
    }

}
  
