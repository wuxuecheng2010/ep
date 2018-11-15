package com.enze.ep.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.enze.ep.entity.TbProduct;
import com.enze.ep.entity.TbProductPrice;

@Repository
@Mapper
public interface EpProductDAO {
    String SELECT_FIELDS = " a.iproductid,a.vcuniversalname title,a.vcproductcode,a.vcuniversalname,a.vcproductname,a.vceasycode,a.vcstandard,a.vcmanufacturer,a.vcproductunit,c.unitname,d.numprice";

    @Select({"select distinct", SELECT_FIELDS, " from tb_Productinfo a left join tb_StockProductInfo b ",
    	                                         "on a.IPRODUCTID = b.IPRODUCTID",
    	                                         "left join tb_UnitInfo c on a.VCPRODUCTUNIT=c.ISID",
    	                                         "left join (select * from tb_ProductPrice where ipricetypeid=3 )d on a.IPRODUCTID = d.IPRODUCTID ",
    	                                         " where  b.ICOUNTERID in (#{counterids}) and b.NUMSTOCKS>0 and (a.VCUNIVERSALNAME like '%${productName}%' or a.VCPRODUCTNAME like '%${productName}%' or a.VCEASYCODE like '%${productName}%' or a.VCPRODUCTCODE like '%${productName}%' or a.VCSTANDARD like '%${productName}%')"})
    List<TbProduct> selectProductByProductName(String counterids,  String productName);
    
    
    @Select({"select ", "iproductid,numprice", " from tb_ProductPrice  where ipricetypeid=3 "," and iproductid=#{iproductid}"})
    TbProductPrice selectProductByProductid(int iproductid);


}
