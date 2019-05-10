package com.seu.mstc.dao;

import com.seu.mstc.model.BlogComment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * Created by lk on 2018/5/3.
 */
@Component
@Mapper
public interface BlogCommentDao {
    String TABLE_NAME=" blog_comment ";
    String INSERT_FIELDS="  entity_id, entity_type, user_id, to_user_id, create_time, content, status, image_url, like_count, dislike_count  ";
    String SELECT_FIELDS=" id, " + INSERT_FIELDS;


    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{entityId},#{entityType},#{userId},#{toUserId},#{createTime},#{content}," +
                    "#{status},#{imageUrl},#{likeCount},#{dislikeCount})"})
    int addBlogComment(BlogComment blogComment);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    BlogComment selectBlogCommentById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where (entity_id=#{entityId} and entity_type=#{entityType}) "})
    List<BlogComment> selectBlogCommentByIdType(@Param("entityId") int entityId,
                                                @Param("entityType")int entityType);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where (entity_id=#{entityId} and entity_type=#{entityType}) limit #{start},#{num}"})
    List<BlogComment> selectBlogCommentByIdTypeLimit(@Param("entityId") int entityId,
                                                             @Param("entityType")int entityType,
                                                             @Param("start") int start,
                                                             @Param("num") int num);


    @Select({"select count(*) ", SELECT_FIELDS, " from ", TABLE_NAME, " where (entity_id=#{entityId} and entity_type=#{entityType}) "})
    int selectBlogCommentNumByIdType(@Param("entityId") int entityId,
                                     @Param("entityType")int entityType);

    @Update({"update ",TABLE_NAME, " set status=#{status} where id=#{id}"})
    void updateStatus(BlogComment blogComment);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    int deleteBlogComment(int id);
}
