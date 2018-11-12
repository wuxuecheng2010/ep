package com.enze.ep.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.enze.ep.entity.EpConfig;

@Repository
@Mapper
public interface EpConfigDAO {
	String TABLE_NAME = "ep_config";
    String SELECT_FIELDS = "cfgid,cfgname,cfgvalue,cfgmemo";

   
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where cfgname = #{cfgname}"})
    EpConfig selectConfigByCfgname(String cfgname);
    
    

}
