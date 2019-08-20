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
    String INSET_FIELDS ="paytypeid,ordertype,ordercode,creuserid,credate,ordermoney,memo,sectionid,name,bedno,address,idcard,birthday,sex,micard,hicard,age,symptom,outpatientnumber,sourceorderid,refundnum";
    String SELECT_FIELDS ="ordertype,ordercode,creuserid,credate,ordermoney,memo,sectionid,name,bedno,address,idcard,birthday,sex,micard,hicard,age,symptom,outpatientnumber,orderid,usestatus,paydate,paytypeid,weixinnoncestr,flagsendstore,flagclosed,sourceorderid";
    
    @Insert({"insert into ", TABLE_NAME, "(", INSET_FIELDS, ") values (#{paytypeid},#{ordertype},#{ordercode},#{creuserid},GETDATE(),#{ordermoney},#{memo},#{sectionid},#{name},#{bedno},#{address},#{idcard},#{birthday},#{sex},#{micard},#{hicard},#{age},#{symptom},#{outpatientnumber},#{sourceorderid},#{refundnum})"})
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
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where ordertype = #{ordertype} and usestatus=#{usestatus} and flagsendstore=#{flagsendstore} and credate<=dateadd(MINUTE,#{minutes},GETDATE())"})
    List<EpOrder> selectOrderByOrderTypeAndUsestatusAndMinutesAndFlagSendStoreAOB(int ordertype, int usestatus,int minutes,int flagsendstore);
    
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where ordertype = #{ordertype} and usestatus=#{usestatus} and weixinrefundappflag=#{weixinrefundappflag} and credate>=dateadd(MINUTE,#{minutes},GETDATE())"})
    List<EpOrder> selectOrderByOrderTypeAndUsestatusAndMinutesAndWeixinRefundAppFlag(int ordertype, int usestatus,int minutes,int weixinrefundappflag);
    
    
    @Update({"update", TABLE_NAME ,"set paytypeid=#{paytypeid}",
    	",paydate=#{paydate}",",usestatus=1","where ordercode=#{ordercode}"})
    void updateOrderByPayInfo(EpPayInfo epPayInfo);
    
   
    
    @Update({"update", TABLE_NAME ,"set weixinnoncestr=#{weixinnoncestr}","where ordercode=#{ordercode}"})
    void updateOrderWeixinNonceStrByCode(String ordercode,String weixinnoncestr);
    
    
    @Update({"update", TABLE_NAME ,"set flagsendstore=1","where orderid=#{orderid}"})
    void updateOrderFlagSendStore(int orderid);
    
    @Update({"update", TABLE_NAME ,"set flagclosed=1","where orderid=#{orderid}"})
    void updateOrderFlagClosed(int orderid);
    
    @Update({"update", TABLE_NAME ,"set weixinrefundappflag=1","where orderid=#{orderid}"})
    void updateOrderWeiXinRefundAppFlag(int orderid);
    
    @Update({"update", TABLE_NAME ,"set usestatus=#{usestatus},paydate=getdate(),memo=#{memo}","where orderid=#{orderid}"})
    void updateOrderUsestatue(int orderid,int usestatus,String memo);
    
    @Update({"update", TABLE_NAME ,"set usestatus=#{usestatus}","where orderid=#{orderid}"})
    void updateOrderUsestatueSimple(int orderid,int usestatus);
    
    
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where sectionid = #{sectionid} and credate>=#{startdate} and credate<#{enddate} and name like '%${name}%' and ordertype=#{ordertype} and idcard=#{idcard} and usestatus in (0,1) order by credate desc" })
    List<EpOrder> selectOrderListBySectionidAndCredateAndNameAndIDcard(int sectionid,String startdate,String enddate,String name,String idcard,int ordertype);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where creuserid = #{userid} and credate>=#{startdate} and credate<#{enddate} and name like '%${name}%' and ordertype=2 order by credate desc" })
    List<EpOrder> selectOrderListByUseridAndCredateAndName(int userid,String startdate,String enddate,String name);
    
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where sourceorderid = #{sourceorderid} and usestatus!=-1 " })
    List<EpOrder> selectOrderListBySourceOrderid(int sourceorderid);
    
}
