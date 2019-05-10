package com.seu.mstc.dao;

import com.seu.mstc.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Mapper
public interface QuestionDao {
    String TABLE_NAME=" question ";
    String INSERT_FIELDS="  user_id, title, content, image_url, create_time, key_word, status," +
            " flag, like_count, dislike_count, is_top  ";
    String SELECT_FIELDS=" id, " + INSERT_FIELDS;


    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{userId},#{title},#{content},#{imageUrl},#{createTime},#{keyWord}," +
                    "#{status},#{flag},#{likeCount},#{dislikeCount},#{isTop})"})
    int addQuestion(Question question);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    Question selectQuestionById(int id);

    @Select({"select * from ",TABLE_NAME, " limit #{start}, #{num}"})
    List<Question> selectLatestQuestion(@Param("start")int start,
                                        @Param("num") int num);

    //含有附加字段的联合查询
    @Select({"select question.*, user.nickname, user.head_url"," from question left join user on question.user_id = user.id limit #{start}, #{num}"})
    List<Question> selectLatestQuestionExt(@Param("start")int start,
                                                 @Param("num") int num);

    //含有附加字段的排序联合查询
    @Select({"select question.*, user.nickname, user.head_url"," from question left join user on question.user_id = user.id order by question.is_top desc,question.create_time desc limit #{start}, #{num}"})
    List<Question> selectLatestQuestionExtByOrder(@Param("start")int start,
                                           @Param("num") int num);

    @Select({"select * from ",TABLE_NAME, " where flag=#{flag} limit #{start}, #{num}"})
    List<Question> selectLatestQuestionByFlag(@Param("start")int start,
                                              @Param("num") int num,
                                              @Param("flag") int flag);

    //含有附加字段的联合查询
    @Select({"select question.*, user.nickname, user.head_url"," from question left join user on question.user_id = user.id  where question.flag=#{flag} limit #{start}, #{num}"})
    List<Question> selectLatestQuestionExtByFlag(@Param("start")int start,
                                              @Param("num") int num,
                                              @Param("flag") int flag);

    //含有附加字段的排序联合查询
    @Select({"select question.*, user.nickname, user.head_url"," from question left join user on question.user_id = user.id  where question.flag=#{flag} order by question.is_top desc, question.create_time desc limit #{start}, #{num}"})
    List<Question> selectLatestQuestionExtByFlagByOrder(@Param("start")int start,
                                                 @Param("num") int num,
                                                 @Param("flag") int flag);

    @Select({"select * from question left join user on question.user_id = user.id"})
    List<Question> selectAllQuestion();

    @Select({"select * from question"})
    List<Question> selectQuestion();

    @Select({"select count(*) ", SELECT_FIELDS, " from ", TABLE_NAME})
    int selectQuestionNum();

    @Select({"select count(*) ", SELECT_FIELDS, " from ", TABLE_NAME, "where flag=#{flag}"})
    int selectQuestionNumByFlag(int flag);

    //20表示获取前20个热门博客
    @Select({"select * from ", TABLE_NAME," order by hot_score desc limit 20"})
    List<Question> selectHotQuestion();

    @Update({"update ",TABLE_NAME, " set is_top=#{isTop} where id=#{id}"})
    void updateIsTop(@Param("isTop") int status,
                     @Param("id")int id);


    @Update({"update ",TABLE_NAME, " set hot_score=#{hotScore} where id=#{id}"})
    void updateHotScore(@Param("hotScore") double hotScore,
                        @Param("id")int id);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    int deleteQuestion(int id);

    //在标题和内容中按照关键词搜索并返回分页查询的搜索技术讨论帖
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where title like CONCAT('%',#{keyword},'%') or content like CONCAT('%',#{keyword},'%') order by create_time desc limit #{start}, #{num}"})
    List<Question> selectQuestionByKeyword(@Param("keyword") String keyword,@Param("start") int start,@Param("num") int num);


    //在标题和内容中按照关键词搜索并返回所有的搜索技术讨论帖
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where title like CONCAT('%',#{keyword},'%') or content like CONCAT('%',#{keyword},'%')"})
    List<Question> selectQuestionByKeywordAll(String keyword);


    //根据用户的ID返回分页查询的技术讨论帖
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where user_id=#{userId} order by create_time desc limit #{start}, #{num}"})
    List<Question> selectQuestionByUserId(@Param("userId") int userId,@Param("start") int start,@Param("num") int num);

    //根据用户的ID返回查询的技术讨论帖数量
    @Select({"select count(id) from " , TABLE_NAME, " where user_id=#{userId} "})
    int getQuestionCountByUserId(@Param("userId") int userId);



}
