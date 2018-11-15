package com.enze.ep.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.enze.ep.entity.TbCounter;
import com.enze.ep.entity.EpUser;

@Repository
@Mapper
public interface EpCounterDAO {
	String TABLE_NAME = "tb_Counter";
    String SELECT_FIELDS = " icounterid,vccountercode,vccountername,idepartid,flagapp,sectionid ";



    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where sectionid = #{sectionid}"})
    List<TbCounter> selectCounterBySectionid(int sectionid);

}
