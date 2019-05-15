package com.seu.mstc.dao;

import com.seu.mstc.model.MyBLackList;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by lk on 2018/5/3.
 */
@Mapper
public interface MyBlackListDao {
    String TABLE_NAME=" my_blacklist ";
    String INSERT_FIELDS="  user_id, blacklist_id,createtime";
    String SELECT_FIELDS=" id, " + INSERT_FIELDS;


    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{userId},#{blacklistId},#{createTime}"})
    int addMyBlackList(MyBLackList myBLackList);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where (blacklist_id=#{blacklistId}  and user_id=#{userId})"})
      MyBLackList selectMyBlacklistByUserId(@Param("userId") int userId,
                                            @Param("blacklistId") int blacklistId);


    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where (user_id=#{userId})"})
    List<MyBLackList> selectMyBlackListByUserId(@Param("userId") int userId,
                                                @Param("start") int start,
                                                @Param("num") int num);


    @Select({"select count(id) ", SELECT_FIELDS, " from ", TABLE_NAME, " where (user_id=#{userId})"})
    int getMyBlackListCountByUserId(@Param("userId") int userId);


    @Delete({"delete from ", TABLE_NAME, " where (user_id =#{userId} and blacklist_id=#{blacklistId})"})
    int deleteMyBlackListByBlakListId(@Param("userId") int userId,
                                      @Param("blacklistId") int blacklistId);

    @Delete({"delete from ", TABLE_NAME, " where (user_id=#{userId})"})
    int deleteMyBlackListByUserId(@Param("userId") int userId);

}
