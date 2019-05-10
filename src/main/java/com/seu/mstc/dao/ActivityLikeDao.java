package com.seu.mstc.dao;

import com.seu.mstc.model.ActivityLike;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by lk on 2018/5/3.
 */
@Mapper
public interface ActivityLikeDao {
    String TABLE_NAME=" activity_like_dislike ";
    String INSERT_FIELDS="  user_id, entity_id, entity_type,  is_like, create_time  ";
    String SELECT_FIELDS=" id, " + INSERT_FIELDS;


    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{userId},#{entityId},#{entityType},#{isLike},#{createTime})"})
    int addActivityLike(ActivityLike activityLike);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    ActivityLike selectActivityLikeById(int id);

    @Update({"update ",TABLE_NAME, " set is_like=#{isLike} where id=#{id}"})
    void updateIsLike(ActivityLike activityLike);


}
