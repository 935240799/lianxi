package com.itheima.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.base.Courier;

/**  
 * ClassName:CourierRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年12月16日 下午3:06:12 <br/>       
 */
public interface CourierRepository extends JpaRepository<Courier, Long>,JpaSpecificationExecutor<Courier>{
    
    @Modifying
    @Query("update Courier set deltag = 1 where id = ?")
    void updateDelTagById(long id);

    @Modifying
    @Query("update Courier set deltag = null where id = ?")
    void updateRestoreTagById(long id);
    
}
  
