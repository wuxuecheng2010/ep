package com.enze.ep.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.enze.ep.entity.TbStockProductInfo;

@Repository
@Mapper
public interface EpStockProductInfoDAO {
	String TABLE_NAME = "tb_StockProductInfo";
    String SELECT_FIELDS ="isid, idepartid, icounterid, iproductid, numstocks, numinprice, vcbatchnumber, dtenter, dtlasttime, dtusefulllife, iproviderid, ibilldetailid, dtlastconserve, isales, vcproductunit, vcconfirmfile, flagstop, makedate, ichailing, numbili, flagcgrk ";
    

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where iproductid = #{iproductid} and icounterid in (#{counterids}) and  NUMSTOCKS>0 and isnull(FlagStop,'N')='N' order by dtusefulllife "})
    List<TbStockProductInfo> selectStockProductInfoListByProductIDAndCounterIDS(int iproductid,String counterids);
    
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where isid=#{isid} "})
    TbStockProductInfo selectStockProductInfoByISID(int isid); 
}
