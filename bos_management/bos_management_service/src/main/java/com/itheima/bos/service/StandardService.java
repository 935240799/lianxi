package com.itheima.bos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.Standard;

/**  
 * ClassName:StandardService <br/>  
 * Function:  <br/>  
 * Date:     2018年11月5日 上午11:39:20 <br/>       
 */
public interface StandardService {
    
    void save(Standard standard);

    Page<Standard> findAll(Pageable pageable);

    void batchDel(String ids);

    void restore(String ids);
    
    
}
  
