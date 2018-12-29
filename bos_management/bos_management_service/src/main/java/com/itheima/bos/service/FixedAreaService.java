package com.itheima.bos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;

/**  
 * ClassName:FixedAreaService <br/>  
 * Function:  <br/>  
 * Date:     2018年12月19日 下午4:18:26 <br/>       
 */
public interface FixedAreaService {

    void save(FixedArea model);

    Page<FixedArea> findAll(Pageable pageable);

    void associationCourierToFixedArea(Long fixedAreaId, Long courierId, Long takeTimeId);

    void assignSubAreas2FixedArea(Long id, Long[] subAreaIds);

    List<SubArea> findUnAssociatedFiedArea();

    List<SubArea> findAssociatedFiedArea(Long fixedAreaId);

    
}
  
