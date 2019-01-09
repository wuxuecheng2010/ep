package com.enze.ep.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.enze.ep.entity.EpUsage;

@Repository
@Mapper
public interface EpUsageDAO {
	String TABLE_NAME = "ep_usage";
    //String SELECT_FIELDS = "vccode+'|'+usage title,usageid,usage,vccode";
    String SELECT_FIELDS = "usage title,usageid,usage,vccode";

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where upper(usage) like '%${usage}%' or upper(vccode) like '%${usage}%' "})
    List<EpUsage> selectUsageListByUsageInfo(@Param("usage") String usage);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where usageid = #{usageid} "})
    EpUsage selectUsageListByUsageID(int usageid);
    

    
}
