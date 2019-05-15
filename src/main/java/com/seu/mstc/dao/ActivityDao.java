package com.seu.mstc.dao;

import com.seu.mstc.model.Activity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lk on 2018/5/3.
 */
@Component
@Mapper
public interface ActivityDao {
    String TABLE_NAME=" activity ";
    String INSERT_FIELDS="  user_id, title, location, time, create_time, status, is_top, sponsor," +
            " image_url, content, like_count, dislike_count  ";
    String SELECT_FIELDS=" id, " + INSERT_FIELDS;


    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{userId},#{title},#{location},#{time},#{createTime},#{status}," +
                    "#{isTop},#{sponsor},#{imageUrl},#{content},#{likeCount},#{dislikeCount})"})
    int addActivity(Activity activity);


    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    Activity selectActivityById(int id);

    @Select({"select count(*) ", SELECT_FIELDS, " from ", TABLE_NAME," where status=0"})
    int selectActivityNum();

    //选出活动内容的前n条
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " order by is_top desc,create_time desc limit #{start}, #{num}"})
    List<Activity> selectPageActivity(@Param("start") int start,
                                      @Param("num")int num);

    //更新活动置顶情况
    @Update({"update ",TABLE_NAME, " set is_top=#{isTop} where id=#{id}"})
    void updateIsTop(@Param("isTop") int status,
                     @Param("id")int id);
    //更新活动状态,status为0表示可见，1表示删除。
//    @Update({"update ",TABLE_NAME, " set status=1 where id=#{id}"})
//    void deleteActivity(int id);

    //删除活动内容
    @Delete({"delete from ",TABLE_NAME, " where id=#{id}"})
    void deleteActivity(int id);

    //在标题和内容中按照关键词搜索并返回分页查询的搜索活动
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where title like CONCAT('%',#{keyword},'%') or content like CONCAT('%',#{keyword},'%') order by create_time desc limit #{start}, #{num}"})
    List<Activity> selectActivityByKeyword(@Param("keyword") String keyword, @Param("start") int start, @Param("num") int num);

    //在标题和内容中按照关键词搜索并返回所有的搜索活动
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where title like CONCAT('%',#{keyword},'%') or content like CONCAT('%',#{keyword},'%')"})
    List<Activity> selectActivityByKeywordAll(String keyword);


    //根据用户的ID返回分页查询的活动
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where user_id=#{userId} order by create_time desc limit #{start}, #{num}"})
    List<Activity> selectActivityByUserId(@Param("userId") int userId,@Param("start") int start,@Param("num") int num);

    //根据用户的ID返回查询的活动数量
    @Select({"select count(id) from " , TABLE_NAME, " where user_id=#{userId} "})
    int getActivityCountByUserId(@Param("userId") int userId);

}
