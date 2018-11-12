package com.enze.ep.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.enze.ep.entity.EpSection;

@Repository
@Mapper
public interface EpSectionDAO {
	String TABLE_NAME = "ep_section";
    String SELECT_FIELDS = "sectionid,hissectionid,sectionname,usestatus";

   
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where sectionid = #{sectionid}"})
    EpSection selectSectionByID(int sectionid);
    
    

}
