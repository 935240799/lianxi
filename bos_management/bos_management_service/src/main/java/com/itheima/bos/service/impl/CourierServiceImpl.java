package com.itheima.bos.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.CourierRepository;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.service.CourierService;

/**  
 * ClassName:CourierServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年12月16日 下午3:03:30 <br/>       
 */
@Service
@Transactional
public class CourierServiceImpl implements CourierService {

    @Autowired
    private CourierRepository courierRepository;
    
    @Override
    public void save(Courier courier) {
        courierRepository.save(courier);
    }


    @Override
    public void batchDel(String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            String[] split = ids.split(",");
            for (String id : split) {
                courierRepository.updateDelTagById(Long.parseLong(id));
            }
        }
    }

    @Override
    public void batchRestore(String ids) {
          
        if (StringUtils.isNotEmpty(ids)) {
            String[] split = ids.split(",");
            for (String id : split) {
                courierRepository.updateRestoreTagById(Long.parseLong(id));
            }
        }
        
    }

    @Override
    public Page<Courier> findAll(Specification<Courier> specification, Pageable pageable) {
        return courierRepository.findAll(specification,pageable);
    }

}
  
