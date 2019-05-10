package com.seu.mstc.dao;

import com.seu.mstc.model.UserFollower;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created by lk on 2018/5/3.
 */
@Mapper
public interface UserFollowerDao {
    String TABLE_NAME=" user_follower ";
    String INSERT_FIELDS="  from_id, to_id, relationship_id, is_friend, create_time  ";
    String SELECT_FIELDS=" id, " + INSERT_FIELDS;


    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{fromId},#{toId},#{relationshipId},#{isFriend},#{createTime})"})
    int addUserFollower(UserFollower userFollower);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    UserFollower selectUserFollowerById(int id);



}
