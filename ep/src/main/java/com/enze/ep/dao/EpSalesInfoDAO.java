package com.enze.ep.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.stereotype.Repository;

import com.enze.ep.entity.EpInpatient;
import com.enze.ep.entity.TbSalesInfo;

@Repository
@Mapper
public interface EpSalesInfoDAO {
    
	@Select({ "exec sp_GetNo @docname=#{docname}, @departid=#{departid}"})
	@Options(statementType=StatementType.CALLABLE)
	String getSalesInfoDoc(String docname,int departid );

	
	@Select({"DECLARE @return_value int;", "exec @return_value =App_SalesInfo @keyvalue=#{keyvalue},@flagapp=#{flagapp},@appuser=#{appuser};","SELECT @return_value as N'@Return Value'"})
	@Options(statementType=StatementType.CALLABLE)
	int approvelSalesInfo(String keyvalue,String flagapp,String appuser );

	
	String TABLE_NAME = "tb_SalesInfo";
	String INSET_FIELDS = "vcbillno, idepartid, dtbilldate, createdby, creationdate, lastupdatedate, lastupdatedby, bstatus, imembercardid, iprescriptionid, vcmemo, nummoneyall, flagapp, salespreson, numdiscount, ipackage, vcpreson, numtime, numprint, cashier_account, appdate, dyqk, cfsb,paytypeid ";
	String SELECT_FIELDS = "vcbillno, idepartid, dtbilldate, createdby, creationdate, lastupdatedate, lastupdatedby, bstatus, imembercardid, iprescriptionid, vcmemo, nummoneyall, flagapp, salespreson, numdiscount, ipackage, vcpreson, numtime, numprint, cashier_account, appdate, dyqk, cfsb,paytypeid,ibillid ";


	
	@Insert({"insert into",TABLE_NAME,"(",INSET_FIELDS,")values (#{vcbillno}, #{idepartid}, getdate(), #{createdby}, getdate(), getdate(), #{lastupdatedby}, #{bstatus}, #{imembercardid}, #{iprescriptionid}, #{vcmemo}, #{nummoneyall}, #{flagapp}, #{salespreson}, #{numdiscount}, #{ipackage}, #{vcpreson}, #{numtime}, #{numprint}, #{cashier_account}, #{appdate}, #{dyqk}, #{cfsb},#{paytypeid} )"})
    void addSalesInfo(TbSalesInfo tbSalesInfo);
	
	@Select({"select ",SELECT_FIELDS," from ",TABLE_NAME ," where vcbillno=#{vcbillno} "})
	TbSalesInfo selectTbSalesInfoByVcbillno(String vcbillno);

}
