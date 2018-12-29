package com.itheima.bos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.itheima.bos.domain.base.Courier;

/**  
 * ClassName:CourierService <br/>  
 * Function:  <br/>  
 * Date:     2018年12月16日 下午2:58:53 <br/>       
 */
public interface CourierService {

    void save(Courier model);

    Page<Courier> findAll(Specification<Courier> specification,Pageable pageable);
    
    void batchDel (String ids);
    void batchRestore (String ids);
}
  
