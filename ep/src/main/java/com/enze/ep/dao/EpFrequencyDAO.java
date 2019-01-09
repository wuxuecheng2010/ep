package com.enze.ep.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.enze.ep.entity.EpConfig;
import com.enze.ep.entity.EpFrequency;

@Repository
@Mapper
public interface EpFrequencyDAO {
	String TABLE_NAME = "ep_frequency";
    //String SELECT_FIELDS = "vccode+'|'+frequency title,frequencyid,frequency,vccode";
    String SELECT_FIELDS = "frequency title,frequencyid,frequency,vccode";

   
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where upper(frequency) like '%${frequencyInfo}%' or upper(vccode) like '%${frequencyInfo}%' "})
    List<EpFrequency> selectFrequencyListByFrequencyInfo(@Param(value = "frequencyInfo") String frequencyInfo);
    
    

}
