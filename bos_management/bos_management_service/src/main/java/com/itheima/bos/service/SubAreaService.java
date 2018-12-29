package com.itheima.bos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.SubArea;

/**  
 * ClassName:SubAreaService <br/>  
 * Function:  <br/>  
 * Date:     2018年12月18日 下午10:16:55 <br/>       
 */
public interface SubAreaService {

    void save(SubArea model);

    Page<SubArea> fandAll(Pageable pageable);

}
  
