package com.seu.mstc.dao;

import com.seu.mstc.model.ProgrammingLike;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by lk on 2018/5/3.
 */
@Mapper
public interface ProgrammingLikeDao {
    String TABLE_NAME=" programming_like_dislike ";
    String INSERT_FIELDS="  user_id, entity_id, entity_type,  is_like, create_time  ";
    String SELECT_FIELDS=" id, " + INSERT_FIELDS;


    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{userId},#{entityId},#{entityType},#{isLike},#{createTime})"})
    int addProgrammingLike(ProgrammingLike programmingLike);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    ProgrammingLike selectProgrammingLikeById(int id);

    @Update({"update ",TABLE_NAME, " set is_like=#{isLike} where id=#{id}"})
    void updateIsLike(ProgrammingLike programmingLike);


}
