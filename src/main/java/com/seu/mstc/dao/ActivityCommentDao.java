package com.seu.mstc.dao;

import com.seu.mstc.model.ActivityComment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lk on 2018/5/3.
 */
@Mapper
@Component
public interface ActivityCommentDao {
    String TABLE_NAME=" activity_comment ";
    String INSERT_FIELDS="  entity_id, entity_type, user_id, to_user_id, create_time, content, status, like_count, dislike_count  ";
    String SELECT_FIELDS=" id, " + INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{entityId},#{entityType},#{userId},#{toUserId},#{createTime},#{content}," +
                    "#{status},#{likeCount},#{dislikeCount})"})
    int addActivityComment(ActivityComment activityComment);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    ActivityComment selectActivityCommentById(int id);

    @Select({"select count(*) ", SELECT_FIELDS, " from ", TABLE_NAME, " where (entity_id=#{entityId} and entity_type=#{entityType}) "})
    int selectActivityCommentNumByIdType(@Param("entityId") int entityId,
                                         @Param("entityType") int entityType);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where (entity_id=#{entityId} and entity_type=#{entityType}) "})
    List<ActivityComment> selectActivityCommentByIdType(@Param("entityId") int entityId,
                                                        @Param("entityType") int entityType);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where (entity_id=#{entityId} and entity_type=#{entityType}) limit #{start},#{num}"})
    List<ActivityComment> selectActivityCommentByIdTypeLimit(@Param("entityId") int entityId,
                                                     @Param("entityType")int entityType,
                                                     @Param("start") int start,
                                                     @Param("num") int num);
    @Update({"update ",TABLE_NAME, " set status=#{status} where id=#{id}"})
    void updateStatus(ActivityComment activityComment);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    int deleteActivityComment(int id);

}
