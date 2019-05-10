package com.seu.mstc.dao;

import com.seu.mstc.model.Programming;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface ProgrammingDao {
    String TABLE_NAME=" programming ";
    String INSERT_FIELDS="  user_id, title, ideas, answer, image_url, create_time, status," +
            " flag, like_count, dislike_count, is_top  ";
    String SELECT_FIELDS=" id, " + INSERT_FIELDS;


    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{userId},#{title},#{ideas},#{answer},#{imageUrl},#{createTime}," +
                    "#{status},#{flag},#{likeCount},#{dislikeCount},#{isTop})"})
    int addProgramming(Programming programming);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    Programming selectProgrammingById(int id);

    @Select({"select * from ",TABLE_NAME, " limit #{start}, #{num}"})
    List<Programming> selectLatestProgramming(@Param("start")int start,
                                        @Param("num") int num);

    //20表示获取前20个热门编程题
    @Select({"select * from ", TABLE_NAME," order by hot_score desc limit 20"})
    List<Programming> selectHotProgramming();

    @Select({"select * from ",TABLE_NAME, " order by is_top desc,create_time desc limit #{start}, #{num}"})
    List<Programming> selectLatestProgrammingByOrder(@Param("start")int start,
                                              @Param("num") int num);

    @Select({"select * from ",TABLE_NAME, " where flag=#{flag} limit #{start}, #{num}"})
    List<Programming> selectLatestProgrammingByFlag(@Param("start")int start,
                                              @Param("num") int num,
                                              @Param("flag") int flag);

    @Select({"select * from ",TABLE_NAME, " where flag=#{flag} order by is_top desc,create_time desc limit #{start}, #{num}"})
    List<Programming> selectLatestProgrammingByFlagByOrder(@Param("start")int start,
                                                    @Param("num") int num,
                                                    @Param("flag") int flag);

    @Select({"select count(*) ", SELECT_FIELDS, " from ", TABLE_NAME})
    int selectProgrammingNum();

    @Select({"select count(*) ", SELECT_FIELDS, " from ", TABLE_NAME, "where flag=#{flag}"})
    int selectProgrammingNumByFlag(int flag);

    @Update({"update ",TABLE_NAME, " set is_top=#{isTop} where id=#{id}"})
    void updateIsTop(@Param("isTop") int status,
                     @Param("id")int id);

    @Update({"update ",TABLE_NAME, " set hot_score=#{hotScore} where id=#{id}"})
    void updateHotScore(@Param("hotScore") double hotScore,
                        @Param("id")int id);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    int deleteProgramming(int id);


    //在标题和内容中按照关键词搜索并返回分页查询的搜索编程题
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where title like CONCAT('%',#{keyword},'%') or ideas like CONCAT('%',#{keyword},'%') order by create_time desc limit #{start}, #{num}"})
    List<Programming> selectProgrammingByKeyword(@Param("keyword") String keyword,@Param("start") int start,@Param("num") int num);

    //在标题和内容中按照关键词搜索并返回所有的搜索编程题
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where title like CONCAT('%',#{keyword},'%') or ideas like CONCAT('%',#{keyword},'%')"})
    List<Programming> selectProgrammingByKeywordAll(String keyword);


    //根据用户的ID返回分页查询的编程题
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where user_id=#{userId} order by create_time desc limit #{start}, #{num}"})
    List<Programming> selectProgrammingByUserId(@Param("userId") int userId,@Param("start") int start,@Param("num") int num);

    //根据用户的ID返回查询的编程题数量
    @Select({"select count(id) from " , TABLE_NAME, " where user_id=#{userId} "})
    int getProgrammingCountByUserId(@Param("userId") int userId);


    //根据编程题id更新编程题测试代码
    @Update({"update ",TABLE_NAME, " set answer=#{answer} where id=#{id}"})
    void updateAnswer(@Param("answer") String answer,
                     @Param("id")int id);
}
