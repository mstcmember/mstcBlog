package com.seu.mstc.dao;

import com.seu.mstc.model.News;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by lk on 2018/5/3.
 */
@Mapper
public interface NewsDao {
    String TABLE_NAME=" news ";
    String INSERT_FIELDS="  user_id, title, news_abstract, content, create_time, image_url, is_top, status  ";
    String SELECT_FIELDS=" id, " + INSERT_FIELDS;


    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{userId},#{title},#{newsAbstract},#{content},#{createTime},#{imageUrl}," +
                    "#{isTop},#{status})"})
    int addNews(News news);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    News selectNewsById(int id);


    @Update({"update ",TABLE_NAME, " set is_top=#{isTop} where id=#{id}"})
    void updateIsTop(News news);




}
