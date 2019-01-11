package com.enze.ep.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.enze.ep.entity.TbDepartMent;
import com.enze.ep.entity.EpOrderStock;

@Repository
@Mapper
public interface EpOrderStockDAO {
	String TABLE_NAME = "ep_order_stock";
    String INSET_FIELDS = " orderid,ordersid,idepartid,stockisid,qty,numprice";
    String SELECT_FIELDS = " orderid,ordersid,idepartid,stockisid,qty,numprice,osid";

    @Insert({"insert into ", TABLE_NAME, "(", INSET_FIELDS, ") values (#{orderid},#{ordersid},#{idepartid},#{stockisid},#{qty},#{numprice})"})
    int addOrderStock(EpOrderStock epOrderStock);
    
    @Select({"select idepartid ,vcdepartname from tb_DepartMent where idepartid in (select  idepartid from ",TABLE_NAME," where orderid=#{orderid})"})
    List<TbDepartMent> selectDepartsFromOrderStock(int orderid);
    
    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where orderid=#{orderid} and idepartid=#{idepartid}"})
    List<EpOrderStock> selectOrderStockByOrderIDAndDepartID(int orderid,int idepartid);
    
    /**
     * 
    * @Title: selectOrderStockByEpOrderid
    * @Description: 退货单明细数据 根据创建的退货订单
    * @param @param orderid
    * @param @return    参数
    * @author wuxuecheng
    * @return List<EpOrderStock>    返回类型
    * @throws
     */
    List<EpOrderStock> selectOrderStockByEpOrderid(int orderid);
}
