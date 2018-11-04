package com.enze.ep.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.enze.ep.entity.EpProduct;

@Repository
@Mapper
public interface EpProductDAO {
    String SELECT_FIELDS = " a.iproductid,a.vcproductcode,a.vcuniversalname,a.vcproductname,a.vceasycode,a.vcstandard,a.vcmanufacturer";

    @Select({"select distinct", SELECT_FIELDS, " from tb_Productinfo a left join tb_StockProductInfo b ", "on a.IPRODUCTID = b.IPRODUCTID"," where  b.ICOUNTERID in (#{counterids}) and b.NUMSTOCKS>0 and (a.VCUNIVERSALNAME like '%${productName}%' or a.VCPRODUCTNAME like '%${productName}%' or a.VCEASYCODE like '%${productName}%' or a.VCPRODUCTCODE like '%${productName}%' or a.VCSTANDARD like '%${productName}%')"})
    List<EpProduct> selectProductByProductName(String counterids,  String productName);


}
