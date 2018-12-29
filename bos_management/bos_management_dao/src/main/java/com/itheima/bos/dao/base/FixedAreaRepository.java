package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;

/**  
 * ClassName:FixedAreaRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年12月19日 下午4:20:22 <br/>       
 */
public interface FixedAreaRepository extends JpaRepository<FixedArea, Long> {

}
  
