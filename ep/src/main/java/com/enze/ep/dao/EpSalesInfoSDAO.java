package com.enze.ep.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.enze.ep.entity.TbSalesInfoS;

@Repository
@Mapper
public interface EpSalesInfoSDAO {
    
	String TABLE_NAME = "tb_SalesInfoS";
	String INSET_FIELDS = "vcbillno, iproductid, numsales, numprice, vcbatchnumber,vcconfirmfile, vcproductunit, numinprice, dtusefulllife, iproviderid,numcountrywideretailprice, istockinforid, nummoney, queue,createdby, creationdate, lastupdatedate, lastupdatedby,ichailing, numpricezd, ipackage, icounterid, iflagaccpayed,epordersid ";
	String SELECT_FIELDS = "vcbillno, iproductid, numsales, numprice, vcbatchnumber,vcconfirmfile, vcproductunit, numinprice, dtusefulllife, iproviderid,numcountrywideretailprice, istockinforid, nummoney, queue,createdby, creationdate, lastupdatedate, lastupdatedby,ichailing, numpricezd, ipackage, icounterid, iflagaccpayed ,epordersid,isid";

	@Insert({"insert into",TABLE_NAME,"(",INSET_FIELDS,")values (#{vcbillno},#{iproductid},#{numsales},#{numprice},#{vcbatchnumber},#{vcconfirmfile},#{vcproductunit},#{numinprice},#{dtusefulllife},#{iproviderid},#{numcountrywideretailprice},#{istockinforid},#{nummoney},#{queue},#{createdby},#{creationdate},#{lastupdatedate},#{lastupdatedby},#{ichailing},#{numpricezd},#{ipackage},#{icounterid},#{iflagaccpayed},#{epordersid})"})
    void addSalesInfoS(TbSalesInfoS tbSalesInfos);
	
	@Select({"select ",SELECT_FIELDS ," from ",TABLE_NAME," where  epordersid=#{epordersid} "})
	List<TbSalesInfoS> selectTbSalesInfoSListByEpordersid(int epordersid);

}
