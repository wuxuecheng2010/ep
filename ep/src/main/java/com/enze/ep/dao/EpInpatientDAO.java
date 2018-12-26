package com.enze.ep.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.enze.ep.entity.EpInpatient;

@Repository
@Mapper
public interface EpInpatientDAO {

	String TABLE_NAME = "ep_inpatient";
	String INSET_FIELDS = " hisinid,name, idcard,age,bedno,sectionid,describe,memo,usestatus,address,tel,type,socialstatus,room,dob,sex,regno ";
	String SELECT_FIELDS = " inid,hisinid,name, idcard,age,bedno,sectionid,describe,memo,usestatus,address,name+'|'+bedno+'|'+address title,tel,type,socialstatus,room,dob,sex,regno ";

	@Insert({ "insert into ", TABLE_NAME, "(", INSET_FIELDS,
			") values (#{hisinid},#{name},#{idcard},#{age},#{bedno},#{sectionid},#{describe},#{memo},#{usestatus},#{address},#{tel},#{type},#{socialstatus},#{room},#{dob},#{sex},#{regno})" })
	@Options(useGeneratedKeys=true,keyProperty="inid",keyColumn="inid")
	int addInpatient(EpInpatient epInpatient);
	
	@Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where hisinid=#{hisinid}"})
	EpInpatient selectInpatientByHisinid(String hisinid);

	@Select({ "select ", SELECT_FIELDS, " from ", TABLE_NAME,
			" where name like '%${inpatientName}%' or idcard like '%${inpatientName}%' or bedno like '%${inpatientName}%' or describe like '%${inpatientName}%' or memo like '%${inpatientName}%' or address like '%${inpatientName}%' " })
	List<EpInpatient> selectInpatientByInpatientName(@Param("inpatientName") String inpatientName);

}
