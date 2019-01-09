package com.enze.ep.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.enze.ep.entity.EpOrder;
import com.enze.ep.entity.EpOrders;

@Repository
@Mapper
public interface EpOrdersDAO {
	String TABLE_NAME = "ep_orders";
    String INSET_FIELDS = "numno,orderid,iproductid,vcproductcode,vcuniversalname,vcmanufacturer,vcstandard,iunitid,vcunitname,totalcounts,numprice,nummoney,dosis,frequency,usage,sourceordersid ";
    String SELECT_FIELDS = "numno,orderid,iproductid,vcproductcode,vcuniversalname,vcmanufacturer,vcstandard,iunitid,vcunitname,totalcounts,numprice,nummoney,dosis,frequency,usage,ordersid,backcounts,sourceordersid";

    @Insert({"insert into ", TABLE_NAME, "(", INSET_FIELDS, ") values (#{numno},#{orderid},#{iproductid},#{vcproductcode},#{vcuniversalname},#{vcmanufacturer},#{vcstandard},#{iunitid},#{vcunitname},#{totalcounts},#{numprice},#{nummoney},#{dosis},#{frequency},#{usage},#{sourceordersid} )"})
    int addOrders(EpOrders epOrders);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where orderid = #{orderid}"})
    List<EpOrders> selectOrdersByOrderid(int orderid);
    
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where ordersid = #{ordersid}"})
    EpOrders selectOrdersByOrdersid(int ordersid);
    
    @Update({"update",TABLE_NAME,"set backcounts=backcounts+#{totalcounts} where ordersid=#{sourceordersid}"})
    void updateOrdersBackcounts(EpOrders epOrders);

}
