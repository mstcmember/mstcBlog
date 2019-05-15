package com.seu.mstc.dao;

import com.seu.mstc.model.MyFollower;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Mapper
public interface MyFollowerDao {
    String TABLE_NAME=" my_follower ";
    String INSERT_FIELDS="  user_id, entity_id, create_time,";
    String SELECT_FIELDS=" id, " + INSERT_FIELDS;


    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{userId},#{entityId},#{createTime})"})
    int addMyFollower(MyFollower myFollower);

//    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where (user_id=#{userId})"})
//    MyFollower selectMyFollowerByUserId(@Param("userId") int userId);


    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where (user_id=#{userId})"})
    List<MyFollower> selectMyFollowerByUserId(@Param("userId") int userId,
                                                        @Param("start") int start,
                                                        @Param("num") int num);


    @Select({"select count(id) ", SELECT_FIELDS, " from ", TABLE_NAME, " where (user_id=#{userId})"})
    int getMyFollowerCountByUserId(@Param("userId") int userId);


    @Delete({"delete from ", TABLE_NAME, " where user_id=#{userId}"})
    int deleteMyFollowerByUserId(int userId);

    @Delete({"delete from ", TABLE_NAME, " where (entity_id=#{entityId}  and user_id=#{userId})"})
    int deleteMyFollowerByUserIdEntityId(@Param("userId") int userId,
                                                   @Param("entityId") int entityId);

}
