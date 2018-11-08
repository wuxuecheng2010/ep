package com.enze.ep.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.enze.ep.entity.EpOrder;
import com.enze.ep.entity.EpPayInfo;

@Repository
@Mapper
public interface EpOrderDAO {
	String TABLE_NAME = "ep_order";
    String INSET_FIELDS = "ordercode,creuserid,credate,ordermoney,memo,sectionid,name,bedno,address,idcard,birthday,sex,micard,hicard ";
    String SELECT_FIELDS = "ordercode,creuserid,credate,ordermoney,paytypeid,memo,sectionid,name,bedno,address,idcard,birthday,sex,micard,hicard,orderid,usestatus,paydate";

    @Insert({"insert into ", TABLE_NAME, "(", INSET_FIELDS, ") values (#{ordercode},#{creuserid},GETDATE(),#{ordermoney},#{memo},#{sectionid},#{name},#{bedno},#{address},#{idcard},#{birthday},#{sex},#{micard},#{hicard})"})
    @Options(useGeneratedKeys=true,keyProperty="orderid",keyColumn="orderid")
    int addOrder(EpOrder epOrder);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where orderid = #{orderid}"})
    EpOrder selectOrderByOrderid(int orderid);
    
    @Update({"update", TABLE_NAME ,"set paytypeid=#{paytypeid}",
    	",paydate=#{paydate}",",usestatus=1","where ordercode=#{ordercode}"})
    void updateOrderByPayInfo(EpPayInfo epPayInfo);

}
