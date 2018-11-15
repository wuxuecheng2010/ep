package com.enze.ep.dao;

import java.util.Date;
import java.util.List;

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
    String INSET_FIELDS ="ordertype,ordercode,creuserid,credate,ordermoney,memo,sectionid,name,bedno,address,idcard,birthday,sex,micard,hicard,age,symptom,outpatientnumber";
    String SELECT_FIELDS ="ordertype,ordercode,creuserid,credate,ordermoney,memo,sectionid,name,bedno,address,idcard,birthday,sex,micard,hicard,age,symptom,outpatientnumber,orderid,usestatus,paydate,paytypeid,weixinnoncestr,flagsendstore";
    
    @Insert({"insert into ", TABLE_NAME, "(", INSET_FIELDS, ") values (#{ordertype},#{ordercode},#{creuserid},GETDATE(),#{ordermoney},#{memo},#{sectionid},#{name},#{bedno},#{address},#{idcard},#{birthday},#{sex},#{micard},#{hicard},#{age},#{symptom},#{outpatientnumber})"})
    @Options(useGeneratedKeys=true,keyProperty="orderid",keyColumn="orderid")
    int addOrder(EpOrder epOrder);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where orderid = #{orderid}"})
    EpOrder selectOrderByOrderid(int orderid);
    
    /**
     * 
    * @Title: selectOrderByOrderTypeAndUsestatus
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * @param @param ordertype
    * @param @param usestatus
    * @param @param minutes  例如：-10  表示当前时间的10分钟之前
    * @param @return    参数
    * @author wuxuecheng
    * @return List<EpOrder>    返回类型
    * @throws
     */
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where ordertype = #{ordertype} and usestatus=#{usestatus} and flagsendstore=#{flagsendstore} and credate>=dateadd(MINUTE,#{minutes},GETDATE())"})
    List<EpOrder> selectOrderByOrderTypeAndUsestatusAndMinutesAndFlagSendStoreAOB(int ordertype, int usestatus,int minutes,int flagsendstore);
    
    @Update({"update", TABLE_NAME ,"set paytypeid=#{paytypeid}",
    	",paydate=#{paydate}",",usestatus=1","where ordercode=#{ordercode}"})
    void updateOrderByPayInfo(EpPayInfo epPayInfo);
    
    @Update({"update", TABLE_NAME ,"set weixinnoncestr=#{weixinnoncestr}","where ordercode=#{ordercode}"})
    void updateOrderWeixinNonceStrByCode(String ordercode,String weixinnoncestr);

}
