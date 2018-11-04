package com.enze.ep.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.enze.ep.entity.EpUser;

@Repository
@Mapper
public interface EpUserDAO {
	String TABLE_NAME = "ep_user";
    String INSET_FIELDS = " hisuserid,username, password,usercode,usertype,sectionid,memo ";
    String SELECT_FIELDS = " userid,hisuserid,username, password,usercode,usertype,sectionid,memo";

    @Insert({"insert into ", TABLE_NAME, "(", INSET_FIELDS, ") values (#{hisuserid},#{username},#{password},#{usercode},#{usertype},#{sectionid},#{memo})"})
    int addUser(EpUser epuser);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where userName = #{username}"})
    EpUser selectUserByUserName(String username);

    @Update({"update ", TABLE_NAME, " set password = #{password} where username = #{username}"})
    void updateUserPsdByName(EpUser epuser);

}
