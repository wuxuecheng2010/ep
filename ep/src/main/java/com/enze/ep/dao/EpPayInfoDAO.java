package com.enze.ep.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.enze.ep.entity.EpPayInfo;

@Repository
@Mapper
public interface EpPayInfoDAO {
	String TABLE_NAME = "ep_payinfo";
    String INSET_FIELDS = "paytypeid,busstypeid,ordercode,plordercode,baseinfo,fee,paydate,attach,tradestatus ";
    String SELECT_FIELDS = "paytypeid,busstypeid,ordercode,plordercode,baseinfo,fee,paydate,attach,tradestatus,payinfoid ";

    @Insert({"insert into ", TABLE_NAME, "(", INSET_FIELDS, ") values (#{paytypeid},#{busstypeid},#{ordercode},#{plordercode},#{baseinfo},#{fee},#{paydate},#{attach},#{tradestatus})"})
    int addPayInfo(EpPayInfo epPayInfo);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where ordercode = #{ordercode} or plordercode=#{ordercode}"})
    List<EpPayInfo> selectPayInfoByOrdercode(String ordercode);
    

}
