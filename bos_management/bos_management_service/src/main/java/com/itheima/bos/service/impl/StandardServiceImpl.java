package com.itheima.bos.service.impl;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.StandardRepository;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.StandardService;

/**  
 * ClassName:StandardServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年11月5日 上午11:44:48 <br/>       
 */
@Transactional
@Service
public class StandardServiceImpl implements StandardService {

    @Autowired
    private StandardRepository standardRepository;
    
    //保存
    @Override
    public void save(Standard standard) {
        standardRepository.save(standard);
    }

    //分页查询
    @Override
    public Page<Standard> findAll(Pageable pageable) {
          
        return standardRepository.findAll(pageable);
    }

    //批量删除
    @Override
    public void batchDel(String ids) {
          
        if (StringUtils.isNotEmpty(ids)) {
            String[] split = ids.split(",");
            for (String id : split) {
                standardRepository.updateDelTagById(Long.parseLong(id));
            }
        }
        
    }

    @Override
    public void restore(String ids) {
          
        if(StringUtils.isNotEmpty(ids)){
            String[] split = ids.split(",");
            for (String id : split) {
                standardRepository.uprestoreTagById(Long.parseLong(id));
            }
        }
        
    }

}
  
