package com.seu.mstc.dao;

import com.seu.mstc.model.SystemMessage;
import com.seu.mstc.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lk on 2018/5/3.
 */
@Mapper
@Component
public interface SystemMessageDao {
    String TABLE_NAME=" system_message ";
    String INSERT_FIELDS="  from_id, to_id, content, has_read, create_time, entity_id, entity_type ";
    String SELECT_FIELDS=" id, " + INSERT_FIELDS;


    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{fromId},#{toId},#{content},#{hasRead},#{createTime},#{entityId},#{entityType})"})
    int addSystemMessage(SystemMessage systemMessage);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    SystemMessage selectSystemMessageById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where to_id=#{toid} order by create_time desc limit #{limit},#{offset}"})
    List<SystemMessage> selectSystemMessageByToId(@Param("toid") int toid,
                                                  @Param("limit") int limit,
                                                  @Param("offset") int offset);

    @Select({"select count(*) from ", TABLE_NAME, " where to_id=#{toid}"})
    int systemMessageAmount(int toid);

    @Update({"update ",TABLE_NAME, " set has_read = 1 where id=#{id}"})
    void setMessagesRead(SystemMessage systemMessage);

    @Update({"update ",TABLE_NAME, " set head_url=#{headUrl} where id=#{id}"})
    int updateHeadUrl(User user);
}
