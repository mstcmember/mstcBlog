package com.seu.mstc;

import com.seu.mstc.dao.QuestionDao;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class InitDatabaseTests {
    @Autowired
    QuestionDao questionDao;

    @Test
    public void contextLoads(){
//        Random random = new Random();
//        Date date = new Date();
//        for (int i = 0; i < 11; ++i){
//            Question question = new Question();
//            question.setContent(String.format("Balaababalalalal Content %d", i));
//            question.setCreateTime(date);
//            question.setDislikeCount(i);
//            question.setFlag(i);
//            question.setImageUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
//            question.setIsTop(0);
//            question.setKeyWord("yyy"+i);
//            question.setLikeCount(i);
//            question.setStatus(0);
//            question.setTitle("title"+i);
//            question.setUserId(i);
//            questionDao.addQuestion(question);
//        }
    }
}
