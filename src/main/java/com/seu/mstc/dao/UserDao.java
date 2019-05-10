package com.seu.mstc.dao;

import com.seu.mstc.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;


@Component
@Mapper
public interface UserDao {
    String TABLE_NAME=" user ";
    String INSERT_FIELDS="  nickname, email, password, salt, sex, head_url, phone, birthday," +
            " hometown, school, department, degree, hobby, qq, wechat, register_time, label, token, follown_count, flag, rank_score  ";
    String SELECT_FIELDS=" id, " + INSERT_FIELDS;


    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{nickname},#{email},#{password},#{salt},#{sex},#{headUrl}," +
                    "#{phone},#{birthday},#{hometown},#{school},#{department},#{degree},#{hobby}," +
                    "#{qq},#{wechat},#{registerTime},#{label},#{token},#{follownCount},#{flag},#{rankScore})"})
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    User selectById(int id);

    @Select({"select count(*) from ", TABLE_NAME, " where rank_score>=#{rankScore}"})
    int selectScoreRankingByScore(@Param("rankScore")double rankScore );

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where email=#{email}"})
    User selectByEmail(String email);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where token=#{token}"})
    User selectByToken(String token);


    @Update({"update ",TABLE_NAME, " set password=#{password},salt=#{salt} where id=#{id}"})
    void updatePassword(User user);

    @Update({"update ",TABLE_NAME, " set token=#{token} where id=#{id}"})
    void updateToken(User user);

    @Update({"update ",TABLE_NAME, " set email=#{email} where id=#{id}"})
    void updateEmail(User user);


    @Update({"update ",TABLE_NAME, " set head_url=#{headUrl} where id=#{id}"})
    int updateHeadUrl(User user);

    @Update({"update ",TABLE_NAME, " set nickname=#{nickname}, sex=#{sex},  " +
            "phone=#{phone}, birthday=#{birthday}, hometown=#{hometown}, school=#{school}, " +
            "department=#{department}, degree=#{degree}, hobby=#{hobby}, qq=#{qq}, wechat=#{wechat}, label=#{label} where id=#{id}"})
    void updateUserInfo(User user);

    @Update({"update ",TABLE_NAME, " set nickname=#{nickname}, sex=#{sex},  " +
            "phone=#{phone}, birthday=#{birthday}, hometown=#{hometown}, school=#{school}, " +
            "department=#{department}, degree=#{degree}, hobby=#{hobby}, qq=#{qq}, wechat=#{wechat}, label=#{label} where token=#{token}"})
    int updateUserInfoByToken(User user);

    @Update({"update ",TABLE_NAME, " set rank_score=#{rankScore} where id=#{id}"})
    int updateUserScore(User user);

}
