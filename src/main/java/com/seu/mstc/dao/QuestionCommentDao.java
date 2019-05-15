package com.seu.mstc.dao;

import com.seu.mstc.model.QuestionComment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Mapper
public interface QuestionCommentDao {
    String TABLE_NAME=" question_comment ";
    String INSERT_FIELDS="  entity_id, entity_type, user_id, to_user_id, create_time, content, status, image_url, like_count, dislike_count  ";
    String SELECT_FIELDS=" id, " + INSERT_FIELDS;


    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{entityId},#{entityType},#{userId},#{toUserId},#{createTime},#{content}," +
                    "#{status},#{imageUrl},#{likeCount},#{dislikeCount})"})
    int addQuestionComment(QuestionComment questionComment);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    QuestionComment selectQuestionCommentById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where (entity_id=#{entityId} and entity_type=#{entityType}) "})
    List<QuestionComment> selectQuestionCommentByIdType(@Param("entityId") int entityId,
                                                        @Param("entityType")int entityType);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where (entity_id=#{entityId} and entity_type=#{entityType}) limit #{start},#{num}"})
    List<QuestionComment> selectQuestionCommentByIdTypeLimit(@Param("entityId") int entityId,
                                                             @Param("entityType")int entityType,
                                                             @Param("start") int start,
                                                             @Param("num") int num);

    @Select({"select count(*) ", SELECT_FIELDS, " from ", TABLE_NAME, " where (entity_id=#{entityId} and entity_type=#{entityType}) "})
    int selectQuestionCommentNumByIdType(@Param("entityId") int entityId,
                                                        @Param("entityType")int entityType);

    @Update({"update ",TABLE_NAME, " set status=#{status} where id=#{id}"})
    void updateStatus(QuestionComment questionComment);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    int deleteQuestionComment(int id);

}
