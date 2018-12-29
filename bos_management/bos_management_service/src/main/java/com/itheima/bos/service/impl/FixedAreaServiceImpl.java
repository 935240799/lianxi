package com.itheima.bos.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.type.ArrayType;
import com.itheima.bos.dao.base.CourierRepository;
import com.itheima.bos.dao.base.FixedAreaRepository;
import com.itheima.bos.dao.base.SubAreaRepository;
import com.itheima.bos.dao.base.TakeTimeRepository;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.service.FixedAreaService;

import net.sf.jasperreports.data.cache.LongArrayValues;

/**  
 * ClassName:FixedAreaServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年12月19日 下午4:19:01 <br/>       
 */
@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {
    
    @Autowired
    private FixedAreaRepository fixedAreaRepository;
    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private TakeTimeRepository takeTimeRepository;
    @Autowired
    private SubAreaRepository subAreaRepository;

    @Override
    public void save(FixedArea model) {
          
        fixedAreaRepository.save(model);
    }

    @Override
    public Page<FixedArea> findAll(Pageable page) {
          
        return fixedAreaRepository.findAll(page);
    }

    @Override
    public void associationCourierToFixedArea(Long fixedAreaId, Long courierId, Long takeTimeId) {
        
        //持久态的对象
        FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
        Courier courier = courierRepository.findOne(courierId);
        TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
        
        courier.setTakeTime(takeTime);
        
        fixedArea.getCouriers().add(courier);
        
    }

    @Override
    public void assignSubAreas2FixedArea(Long fixedAreaId, Long[] subAreaIds) {
          
        //关系是由分区在维护
        //先解绑,把当前定区绑定的所有分区解绑
        FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
        Set<SubArea> subareas = fixedArea.getSubareas();
        for (SubArea subArea : subareas) {
            subArea.setFixedArea(null);
        }
        //再绑定
        if (subAreaIds!=null && subAreaIds.length > 0) {
            for (Long subAreaId : subAreaIds) {
                SubArea subArea = subAreaRepository.findOne(subAreaId);
                subArea.setFixedArea(fixedArea);
            }
        }
    }

    @Override
    public List<SubArea> findUnAssociatedFiedArea() {
          
        return subAreaRepository.findUnAssociatedFiedArea();
    }

    @Override
    public List<SubArea> findAssociatedFiedArea(Long fixedAreaId) {
          
        return subAreaRepository.findAssociatedFiedArea(fixedAreaId);
    }
}
  
