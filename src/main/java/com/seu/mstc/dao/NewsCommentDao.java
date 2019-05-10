package com.seu.mstc.dao;

import com.seu.mstc.model.NewsComment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by lk on 2018/5/3.
 */
@Mapper
public interface NewsCommentDao {
    String TABLE_NAME=" news_comment ";
    String INSERT_FIELDS="  entity_id, entity_type, user_id, to_user_id, create_time, content, status, like_count, dislike_count  ";
    String SELECT_FIELDS=" id, " + INSERT_FIELDS;


    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{entityId},#{entityType},#{userId},#{toUserId},#{createTime},#{content}," +
                    "#{status},#{likeCount},#{dislikeCount})"})
    int addNewsComment(NewsComment newsComment);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    NewsComment selectNewsCommentById(int id);

    @Update({"update ",TABLE_NAME, " set status=#{status} where id=#{id}"})
    void updateStatus(NewsComment newsComment);


}
