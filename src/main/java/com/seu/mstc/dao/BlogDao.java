package com.seu.mstc.dao;

import com.seu.mstc.model.Blog;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lk on 2018/5/3.
 */
@Component
@Mapper
public interface BlogDao {
    String TABLE_NAME=" blog ";
    String INSERT_FIELDS="  user_id, title, blog_abstract, content, image_url, create_time, key_word, status," +
            " flag, like_count, dislike_count, is_top, hot_score";
    String SELECT_FIELDS=" id, " + INSERT_FIELDS;


    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{userId},#{title},#{blogAbstract},#{content},#{imageUrl},#{createTime}," +
                    "#{keyWord},#{status},#{flag},#{likeCount},#{dislikeCount},#{isTop},#{hotScore})"})
    int addBlog(Blog blog);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    Blog selectBlogById(int id);

    @Select({"select * from ",TABLE_NAME, " where status=0 order by is_top desc,create_time desc limit #{start}, #{num}"})
    List<Blog> selectPageBlogByOrder(@Param("start")int start,
                              @Param("num") int num);

    @Select({"select * from ", TABLE_NAME,"where status=0 and flag=#{flag} order by is_top desc,create_time desc limit #{start}, #{num}"})
    List<Blog> selectPageBlogByflagByOrder(@Param("start")int start,
                                       @Param("num") int num,
                                       @Param("flag") int flag);


    @Select({"select count(*) ", SELECT_FIELDS, " from ", TABLE_NAME," where status=0"})
    int selectBlogNum();

    @Select({"select count(*) ", SELECT_FIELDS, " from ", TABLE_NAME, "where flag=#{flag} and status = 0"})
    int selectBlogNumByFlag(int flag);

    @Update({"update ",TABLE_NAME, " set is_top=#{isTop} where id=#{id}"})
    void updateIsTop(@Param("isTop") int status,
                     @Param("id")int id);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    int deleteBlog(int id);

    @Update({"update ",TABLE_NAME, " set hot_score=#{hotScore} where id=#{id}"})
    void updateHotScore(@Param("hotScore") double hotScore,
                        @Param("id")int id);

    //20表示获取前20个热门博客
    @Select({"select * from ", TABLE_NAME," where status=0 order by hot_score desc limit 20"})
    List<Blog> selectHotBlog();

    //在标题和内容中按照关键词搜索并返回分页查询的搜索博客
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where title like CONCAT('%',#{keyword},'%') or content like CONCAT('%',#{keyword},'%') order by create_time desc limit #{start}, #{num}"})
    List<Blog> selectBlogByKeyword(@Param("keyword") String keyword, @Param("start") int start, @Param("num") int num);
    //在标题和内容中按照关键词搜索并返回所有的搜索博客
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where title like CONCAT('%',#{keyword},'%') or content like CONCAT('%',#{keyword},'%')"})
    List<Blog> selectBlogByKeywordAll(String keyword);


    //根据用户的ID返回分页查询的博客（所有的）
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where user_id=#{userId} order by create_time desc limit #{start}, #{num}"})
    List<Blog> selectBlogByUserId(@Param("userId") int userId,@Param("start") int start,@Param("num") int num);

    //根据用户的ID返回查询的编程博客（所有的）
    @Select({"select count(id) from " ,  TABLE_NAME, " where user_id=#{userId} "})
    int getBlogCountByUserId(@Param("userId") int userId);


    //根据用户的ID返回分页查询的博客（除去私密的）
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where user_id=#{userId} and status=#{status}  order by create_time desc limit #{start}, #{num}"})
    List<Blog> selectNoPrivateBlogByUserId(@Param("userId") int userId,@Param("start") int start,@Param("num") int num,@Param("status") int status);

    //根据用户的ID返回查询的博客数量（除去私密的）
    @Select({"select count(id) from " , TABLE_NAME, " where user_id=#{userId} and status=#{status} "})
    int getNoPrivateBlogCountByUserId(@Param("userId") int userId,@Param("status") int status);


}
