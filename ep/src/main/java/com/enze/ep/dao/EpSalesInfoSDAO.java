package com.enze.ep.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.enze.ep.entity.TbSalesInfoS;

@Repository
@Mapper
public interface EpSalesInfoSDAO {
    
	String TABLE_NAME = "tb_SalesInfoS";
	String INSET_FIELDS = "vcbillno, iproductid, numsales, numprice, vcbatchnumber,vcconfirmfile, vcproductunit, numinprice, dtusefulllife, iproviderid,numcountrywideretailprice, istockinforid, nummoney, queue,createdby, creationdate, lastupdatedate, lastupdatedby,ichailing, numpricezd, ipackage, icounterid, iflagaccpayed ";
	String SELECT_FIELDS = "vcbillno, iproductid, numsales, numprice, vcbatchnumber,vcconfirmfile, vcproductunit, numinprice, dtusefulllife, iproviderid,numcountrywideretailprice, istockinforid, nummoney, queue,createdby, creationdate, lastupdatedate, lastupdatedby,ichailing, numpricezd, ipackage, icounterid, iflagaccpayed ,isid";

	@Insert({"insert into",TABLE_NAME,"(",INSET_FIELDS,")values (#{vcbillno},#{iproductid},#{numsales},#{numprice},#{vcbatchnumber},#{vcconfirmfile},#{vcproductunit},#{numinprice},#{dtusefulllife},#{iproviderid},#{numcountrywideretailprice},#{istockinforid},#{nummoney},#{queue},#{createdby},#{creationdate},#{lastupdatedate},#{lastupdatedby},#{ichailing},#{numpricezd},#{ipackage},#{icounterid},#{iflagaccpayed})"})
    void addSalesInfoS(TbSalesInfoS tbSalesInfos);

}
