package com.enze.ep.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.enze.ep.entity.Xxxx;

@Repository
@Mapper
public interface XxxxDAO {
	String TABLE_NAME = "xxxx";
    String SELECT_FIELDS = "name,encodename,resultname";

   
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,"where resultname is null and encodename is not null order by name desc"})
    List<Xxxx> selectXxxxAll();
    
    
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where name = #{name}"})
    Xxxx selectXxxxname(String name);
    
    @Update({"update xxxx set encodename=#{encodename} where name=#{name}"})
    void  updateXxxx(String name,String encodename);
    
    
    @Update({"update xxxx set resultname=#{resultname} where name=#{name}"})
    void  updateXxxxres(String name,String resultname);
    
    
    @Update({"update xxxx set flag=#{flag} where name=#{name}"})
    void  updateXxxxflag(String name,int flag);
    

}
