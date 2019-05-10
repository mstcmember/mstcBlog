package com.seu.mstc.dao;

import com.seu.mstc.model.MyCollection;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Mapper
public interface MyCollectionDao {
    String TABLE_NAME=" my_collection ";
    String INSERT_FIELDS="  user_id, entity_id, entity_type, create_time, title  ";
    String SELECT_FIELDS=" id, " + INSERT_FIELDS;


    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{userId},#{entityId},#{entityType},#{createTime},#{title})"})
    int addMyCollection(MyCollection myCollection);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where (entity_id=#{entityId} and entity_type=#{entityType} and user_id=#{userId})"})
    MyCollection selectMyCollectionByUserIdEntityTypeEntityId(@Param("userId") int userId,
                                                              @Param("entityType") int entityType,
                                                              @Param("entityId")int entityId);


    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where (entity_type=#{entityType} and user_id=#{userId})"})
    List<MyCollection> selectMyCollectionByUserIdEntityType(@Param("userId") int userId,
                                                            @Param("entityType") int entityType,
                                                            @Param("start") int start,
                                                            @Param("num") int num);


    @Select({"select count(id) ", SELECT_FIELDS, " from ", TABLE_NAME, " where (entity_type=#{entityType} and user_id=#{userId})"})
    int getMyCollectionCountByUserIdEntityType(@Param("userId") int userId,
                                               @Param("entityType") int entityType);

    @Select({"select count(*) ", SELECT_FIELDS, " from ", TABLE_NAME, " where (entity_type=#{entityType} and entity_id=#{entityId})"})
    int selectMyCollectionCountByEntityTypeEntityId(@Param("entityType") int entityType,
                                                    @Param("entityId")int entityId);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    int deleteMyCollectionById(int id);

    @Delete({"delete from ", TABLE_NAME, " where (entity_id=#{entityId} and entity_type=#{entityType} and user_id=#{userId})"})
    int deleteMyCollectionByUserIdEntityTypeEntityId(@Param("userId") int userId,
                                                     @Param("entityType") int entityType,
                                                     @Param("entityId")int entityId);






}
