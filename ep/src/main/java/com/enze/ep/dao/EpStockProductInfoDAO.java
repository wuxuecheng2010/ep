package com.enze.ep.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.enze.ep.entity.EpStockProductInfo;

@Repository
@Mapper
public interface EpStockProductInfoDAO {
	String TABLE_NAME = "tb_StockProductInfo";
    String SELECT_FIELDS ="isid,idepartid,icounterid,iproductid,numstocks,numinprice,vcbatchnumber";
    

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where iproductid = #{iproductid} and counterids = #{counterids} and and NUMSTOCKS>0 and isnull(FlagStop,'N')='N' order by dtusefulllife "})
    List<EpStockProductInfo> selectStockProductInfoListByProductIDAndCounterIDS(int iproductid,String counterids);
    
   
}
