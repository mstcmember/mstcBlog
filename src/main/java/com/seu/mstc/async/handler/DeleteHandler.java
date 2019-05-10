package com.seu.mstc.async.handler;

import com.seu.mstc.async.EventHandler;
import com.seu.mstc.async.EventModel;
import com.seu.mstc.async.EventType;
import com.seu.mstc.dao.*;
import com.seu.mstc.model.*;
import com.seu.mstc.utils.PicUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by XC on 2018/10/2
 */
@Component
public class DeleteHandler implements EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(DeleteHandler.class);

    @Autowired
    QuestionDao questionDao;

    @Autowired
    QuestionCommentDao questionCommentDao;
    @Autowired
    BlogDao blogDao;

    @Autowired
    ActivityDao activityDao;

    @Autowired
    ActivityCommentDao activityCommentDao;

    @Autowired
    BlogCommentDao blogCommentDao;

    @Autowired
    ProgrammingCommentDao programmingCommentDao;

    @Autowired
    ProgrammingDao programmingDao;

    @Value("${imageUpload.url}")
    private String url;

    @Override
    public void doHandler(EventModel eventModel) {

        try {
            //删除事件从下往上进行：图片->评论->帖子（博客……）
            if(eventModel.getEntityType()==1) {  //需要删除的是博客模块
                int EntityType = Integer.parseInt(eventModel.getExt("entityType"));  //0代表删问题，1代表删评论
                if (EntityType == 0) {     //删除问题
                    List<BlogComment> blogCommentList = blogCommentDao.selectBlogCommentByIdType(eventModel.getEntityId(), 0);//找出博客的所有评论
                    for (BlogComment blogComment : blogCommentList) {
                        List<BlogComment> blogCommentReplyList = blogCommentDao.selectBlogCommentByIdType(blogComment.getId(), 1);//找出评论的所有回复
                        for (BlogComment blogCommentReply : blogCommentReplyList) {
                            PicUtils.deletePic(blogCommentReply.getContent(),url+"/"); //删除所有回复中的图片
                            blogCommentDao.deleteBlogComment(blogCommentReply.getId());//删除所有的回复
                        }
                        PicUtils.deletePic(blogComment.getContent(),url+"/");//删除所有评论中的图片

                        blogCommentDao.deleteBlogComment(blogComment.getId());//删除所有的评论
                    }
                    Blog blog = blogDao.selectBlogById(eventModel.getEntityId());
                    PicUtils.deletePic(blog.getContent(), url + "/");//删除博客中的图片
                    blogDao.deleteBlog(eventModel.getEntityId());//删除博客
                }else if(EntityType==1){         //删除评论
                    List<BlogComment> blogCommentReplyList = blogCommentDao.selectBlogCommentByIdType(eventModel.getEntityId(),1);
                    for(BlogComment blogCommentReply:blogCommentReplyList) {
                        PicUtils.deletePic(blogCommentReply.getContent(),url+"/"); //删除所有回复中的图片
                        blogCommentDao.deleteBlogComment(blogCommentReply.getId());//删除所有的回复
                    }
                    BlogComment blogComment = blogCommentDao.selectBlogCommentById(eventModel.getEntityId());
                    PicUtils.deletePic(blogComment.getContent(),url+"/");//删除所有评论中的图片
                    blogCommentDao.deleteBlogComment(blogComment.getId());//删除所有的评论
                }
            }

            if(eventModel.getEntityType()==2) {  //需要删除的是技术帖模块
                int EntityType =Integer.parseInt(eventModel.getExt("entityType"));  //0代表删问题，1代表删评论
                if(EntityType==0) {     //删除问题
                    List<QuestionComment> questionCommentList = questionCommentDao.selectQuestionCommentByIdType(eventModel.getEntityId(),0);//找出问题的所有评论
                    for( QuestionComment questionComment : questionCommentList){
                        List<QuestionComment> questionCommentReplyList = questionCommentDao.selectQuestionCommentByIdType(questionComment.getId(),1);//找出评论的所有回复
                        for(QuestionComment questionCommentReply:questionCommentReplyList) {
                            PicUtils.deletePic(questionCommentReply.getContent(),url+"/"); //删除所有回复中的图片
                            questionCommentDao.deleteQuestionComment(questionCommentReply.getId());//删除所有的回复
                        }
                        PicUtils.deletePic(questionComment.getContent(),url+"/");//删除所有评论中的图片
                        questionCommentDao.deleteQuestionComment(questionComment.getId());//删除所有的评论
                    }
                    Question question = questionDao.selectQuestionById(eventModel.getEntityId());
                    PicUtils.deletePic(question.getContent(),url+"/");//删除技术帖中的图片
                    questionDao.deleteQuestion(eventModel.getEntityId());//删除技术帖
                }else if(EntityType==1){         //删除评论
                    List<QuestionComment> questionCommentReplyList = questionCommentDao.selectQuestionCommentByIdType(eventModel.getEntityId(),1);
                    for(QuestionComment questionCommentReply:questionCommentReplyList) {
                        PicUtils.deletePic(questionCommentReply.getContent(),url+"/"); //删除所有回复中的图片
                        questionCommentDao.deleteQuestionComment(questionCommentReply.getId());//删除所有的回复
                    }
                    QuestionComment questionComment = questionCommentDao.selectQuestionCommentById(eventModel.getEntityId());
                    PicUtils.deletePic(questionComment.getContent(),url+"/");//删除所有评论中的图片
                    questionCommentDao.deleteQuestionComment(questionComment.getId());//删除所有的评论
                }
            }

            if(eventModel.getEntityType()==3) {  //需要删除的是活动模块
                int EntityType = Integer.parseInt(eventModel.getExt("entityType"));  //0代表删问题，1代表删评论
                if (EntityType == 0) {     //删除问题
                    List<ActivityComment> activityCommentList = activityCommentDao.selectActivityCommentByIdType(eventModel.getEntityId(), 0);//找出博客的所有评论
                    for (ActivityComment activityComment : activityCommentList) {
                        List<ActivityComment> activityCommentReplyList = activityCommentDao.selectActivityCommentByIdType(activityComment.getId(), 1);//找出评论的所有回复
                        for (ActivityComment activityCommentReply : activityCommentReplyList) {
                            PicUtils.deletePic(activityCommentReply.getContent(), url + "/"); //删除所有回复中的图片
                            activityCommentDao.deleteActivityComment(activityCommentReply.getId());//删除所有的回复
                        }
                        PicUtils.deletePic(activityComment.getContent(), url + "/");//删除所有评论中的图片
                        activityCommentDao.deleteActivityComment(activityComment.getId());//删除所有的评论
                    }
                    Activity activity = activityDao.selectActivityById(eventModel.getEntityId());
                    PicUtils.deletePic(activity.getContent(), url + "/");//删除技术帖中的图片
                    activityDao.deleteActivity(eventModel.getEntityId());//删除技术帖
                }else if(EntityType==1){         //删除评论
                    List<ActivityComment> activityCommentReplyList = activityCommentDao.selectActivityCommentByIdType(eventModel.getEntityId(),1);
                    for(ActivityComment activityCommentReply:activityCommentReplyList) {
                        PicUtils.deletePic(activityCommentReply.getContent(),url+"/"); //删除所有回复中的图片
                        activityCommentDao.deleteActivityComment(activityCommentReply.getId());//删除所有的回复
                    }
                    ActivityComment activityComment = activityCommentDao.selectActivityCommentById(eventModel.getEntityId());
                    PicUtils.deletePic(activityComment.getContent(),url+"/");//删除所有评论中的图片
                    activityCommentDao.deleteActivityComment(activityComment.getId());//删除所有的评论
                }
            }

            if(eventModel.getEntityType()==0) {  //需要删除的是编程题模块
                int EntityType = Integer.parseInt(eventModel.getExt("entityType"));  //0代表删问题，1代表删评论
                if (EntityType == 0) {     //删除问题
                    List<ProgrammingComment> programmingCommentList = programmingCommentDao.selectProgrammingCommentByIdType(eventModel.getEntityId(), 0);//找出博客的所有评论
                    for (ProgrammingComment programmingComment : programmingCommentList) {
                        List<ProgrammingComment> programmingCommentReplyList = programmingCommentDao.selectProgrammingCommentByIdType(programmingComment.getId(), 1);//找出评论的所有回复
                        for (ProgrammingComment programmingCommentReply : programmingCommentReplyList) {
                            PicUtils.deletePic(programmingCommentReply.getContent(), url + "/"); //删除所有回复中的图片
                            programmingCommentDao.deleteProgrammingComment(programmingCommentReply.getId());//删除所有的回复
                        }
                        PicUtils.deletePic(programmingComment.getContent(), url + "/");//删除所有评论中的图片
                        programmingCommentDao.deleteProgrammingComment(programmingComment.getId());//删除所有的评论
                    }
                    Programming programming = programmingDao.selectProgrammingById(eventModel.getEntityId());
                    PicUtils.deletePic(programming.getIdeas(), url + "/");//删除博客中的图片
                    programmingDao.deleteProgramming(eventModel.getEntityId());//删除博客
                }else if(EntityType==1){         //删除评论
                    List<ProgrammingComment> programmingCommentReplyList = programmingCommentDao.selectProgrammingCommentByIdType(eventModel.getEntityId(),1);
                    for(ProgrammingComment programmingCommentReply:programmingCommentReplyList) {
                        PicUtils.deletePic(programmingCommentReply.getContent(),url+"/"); //删除所有回复中的图片
                        programmingCommentDao.deleteProgrammingComment(programmingCommentReply.getId());//删除所有的回复
                    }
                    ProgrammingComment programmingComment = programmingCommentDao.selectProgrammingCommentById(eventModel.getEntityId());
                    PicUtils.deletePic(programmingComment.getContent(),url+"/");//删除所有评论中的图片
                    programmingCommentDao.deleteProgrammingComment(programmingComment.getId());//删除所有的评论
                }
            }

        }catch (Exception e){
            logger.error("异步删除操作出错" + e.getMessage());
        }


    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.DELETE);
    }


//    private int deletePic(String content,String url){    //content为包含图片url的内容，url为图片在磁盘中存储的绝对路径
//        List<String> picUrlList = new ArrayList<>(); //用于存放该回复中的所有图片的url
//        String picUrlMatch = "!\\[.*\\]\\(http://www\\.seumstc\\.top\\.:90/.*\\.jpg\\)";
//        Pattern p = Pattern.compile(picUrlMatch);
//        Matcher m =p.matcher(content);
//        while(m.find()){
//            picUrlList.add(m.group());
//        }
//        for(String picUrl:picUrlList) {
//            String realPicUrl = url+StringUtils.substringBeforeLast(StringUtils.substringAfterLast(picUrl,"/"),")");
//            try {
//                File file = new File(realPicUrl);
//                file.delete();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return 0;
//    }

}
