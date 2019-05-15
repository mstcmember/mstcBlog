package com.seu.mstc.dao;

import com.seu.mstc.model.MyFans;
import com.seu.mstc.model.MyFollower;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Mapper
public interface MyFansDao {
    String TABLE_NAME=" my_fans ";
    String INSERT_FIELDS="  user_id, entity_id, create_time ";
    String SELECT_FIELDS=" id, " + INSERT_FIELDS;


    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{userId},#{entityId},#{createTime})"})
    int addMyFans(MyFans myFans);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where (entity_id=#{entityId} and user_id=#{userId})"})
    MyFollower selectMyFansByUserIdEntityId(@Param("userId") int userId,
                                    @Param("entityId") int entityId);


    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where (entity_type=#{entityType} and user_id=#{userId})"})
    List<MyFans> selectMyFansByUserId(@Param("userId") int userId,
                                                @Param("start") int start,
                                                @Param("num") int num);


    @Select({"select count(id) ", SELECT_FIELDS, " from ", TABLE_NAME, " where (user_id=#{userId})"})
    int getMyFansCountByUserIdEntityType(@Param("userId") int userId);


    @Delete({"delete from ", TABLE_NAME, " where user_id=#{userId}"})
    int deleteMyFansById(@Param("userId") int userId);

    @Delete({"delete from ", TABLE_NAME, " where (entity_id=#{entityId}  and user_id=#{userId})"})
    int deleteMyFansByUserIdEntityId(@Param("userId") int userId,
                                               @Param("entityId") int entityId);

}
