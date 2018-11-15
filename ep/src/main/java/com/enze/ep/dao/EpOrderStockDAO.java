package com.enze.ep.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.enze.ep.entity.EpOrderStock;
import com.enze.ep.entity.EpUser;

@Repository
@Mapper
public interface EpOrderStockDAO {
	String TABLE_NAME = "ep_order_stock";
    String INSET_FIELDS = " orderid,ordersid,idepartid,stockisid,qty";
    String SELECT_FIELDS = " orderid,ordersid,idepartid,stockisid,qty,osid";

    @Insert({"insert into ", TABLE_NAME, "(", INSET_FIELDS, ") values (#{orderid},#{ordersid},#{idepartid},#{stockisid},#{qty})"})
    int addOrderStock(EpOrderStock epOrderStock);
}
