package com.itheima.bos.service.impl;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.SubAreaRepository;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.SubAreaService;

/**  
 * ClassName:SubAreaServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年12月18日 下午10:17:23 <br/>       
 */
@Service
@Transactional
public class SubAreaServiceImpl implements SubAreaService {

    @Autowired
    private SubAreaRepository subAreaRepository;
    
    @Override
    public void save(SubArea subArea) {
          subAreaRepository.save(subArea);
    }

    @Override
    public Page<SubArea> fandAll(Pageable pageable) {
          
        return subAreaRepository.findAll(pageable);
    }
}
  
