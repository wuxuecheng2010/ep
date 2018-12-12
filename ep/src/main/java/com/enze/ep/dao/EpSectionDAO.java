package com.enze.ep.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.enze.ep.entity.EpSection;

@Repository
@Mapper
public interface EpSectionDAO {
	String TABLE_NAME = "ep_section";
    String SELECT_FIELDS = "sectionid,hissectionid,sectionname,usestatus";
    String INSET_FIELDS="hissectionid,sectionname,usestatus";

   
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where sectionid = #{sectionid}"})
    EpSection selectSectionByID(int sectionid);
    
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where hissectionid = #{hissectionid}"})
    EpSection selectSectionByHissectionid(String hissectionid);
    
	@Insert({ "insert into ", TABLE_NAME, "(", INSET_FIELDS,
		") values (#{hissectionid},#{sectionname},#{usestatus})" })
	@Options(useGeneratedKeys=true,keyProperty="sectionid",keyColumn="sectionid")
	int addSection(EpSection epSection);

}
