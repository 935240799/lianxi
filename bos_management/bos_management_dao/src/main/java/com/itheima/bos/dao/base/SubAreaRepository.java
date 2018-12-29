package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.domain.base.SubArea;

/**  
 * ClassName:SubAreaRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年12月18日 下午10:18:07 <br/>       
 */
public interface SubAreaRepository extends JpaRepository<SubArea, Long> {

    @Query("from SubArea where fixedArea = null")
    List<SubArea> findUnAssociatedFiedArea();
    
    @Query(value = "select * from T_SUB_AREA where C_FIXEDAREA_ID = ?",
            nativeQuery = true)
    List<SubArea> findAssociatedFiedArea(Long fixedAreaId);
    
}
  
