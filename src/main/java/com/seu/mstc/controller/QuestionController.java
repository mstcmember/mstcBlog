package com.seu.mstc.controller;

import com.alibaba.fastjson.JSONObject;
import com.seu.mstc.dao.QuestionDao;
import com.seu.mstc.dao.UserDao;
import com.seu.mstc.model.HostHolder;
import com.seu.mstc.model.Question;
import com.seu.mstc.result.ResultInfo;
import com.seu.mstc.service.QuestionService;
import com.seu.mstc.service.UserService;
import com.seu.mstc.utils.EmailUtils;
import com.seu.mstc.utils.PicUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lk on 2018/5/3.
 */
@RestController
@RequestMapping(value="/question")
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    UserDao userDao;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    QuestionDao questionDao;

    @Value("${imageUpload.url}")
    private String url;

    @Value("${imageUpload.localhost}")
    private String localhost;


    /**
     * 加载技术帖子入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/onLoad",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo onLoadQuestion(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            int start = jsonObject.getInteger("start");
            int num = jsonObject.getInteger("num");
            int flag = jsonObject.getInteger("flag");
            result = questionService.getLatestQuestion(start,num,flag);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("加载技术帖子入口失败！");
        }

        return result;
    }

    /**
     * 加载热门技术帖入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/onLoadHot",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo onLoadBlogHot(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            String beginStr=jsonObject.getString("begin");
            int begin=Integer.parseInt(beginStr);
            String endStr=jsonObject.getString("end");
            int end=Integer.parseInt(endStr);
            result = questionService.getHotQuestionDetail();
        }catch (Exception e){
            e.printStackTrace();
            logger.error("加载热门技术帖入口失败！");
        }

        return result;
    }

    /**
     * 置顶技术帖入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/isTop",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo isTopQuestion(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            String idStr=jsonObject.getString("entityId");
            int id=Integer.parseInt(idStr);
            String statusStr=jsonObject.getString("topStatus");
            int status=Integer.parseInt(statusStr);
            result = questionService.updateIsTop(status,id);
        } catch (Exception e){
            e.printStackTrace();
            logger.error("置顶技术帖入口失败！");
        }

        return result;
    }

    /**
     * 发布技术帖子入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/public",method = RequestMethod.POST,
    produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo publicQuestion(@RequestBody JSONObject jsonObject){
        ResultInfo result = null;
        try {
            Question question = new Question();
            String title = jsonObject.getString("title");
            String content = jsonObject.getString("content");
            String imageUrl = null;//图片的存储方式改为结合markdown直接存在content中
            int status = jsonObject.getInteger("status");
            String keyWord = jsonObject.getString("keyword");
            String flagStr = jsonObject.getString("flag");
            int flag=Integer.parseInt(flagStr);
//            if(flagStr.equals("C++")){
//                flag=1;
//            }else if(flagStr.equals("Java")){
//                flag=2;
//            }else if(flagStr.equals("前端")){
//                flag=3;
//            }else if(flagStr.equals("后台")){
//                flag=4;
//            }else if(flagStr.equals("机器学习")){
//                flag=5;
//            }else if(flagStr.equals("硬件嵌入式")){
//                flag=6;
//            }
            //此处有待修改,如何判别帖子类型

            question.setTitle(title);
            question.setContent(content);
            question.setImageUrl(hostHolder.getUser().getHeadUrl());
            question.setKeyWord(keyWord);
            question.setFlag(flag);
            question.setStatus(status);
            result = questionService.addQuestion(question);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("发布技术帖子入口失败！");
        }

        return result;
    }


    /**
     * 评论技术贴入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/comment",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo commentQuestion(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        //添加业务逻辑和权限判断，调用service层
        return result;
    }

    /**
     * 加载技术帖子详情入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/onLoadDetail",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo onLoadQuestionDetail(@RequestBody JSONObject jsonObject){
        int questionId = jsonObject.getInteger("questionId");            //越界处理
        ResultInfo result=null;
        try {
            result = questionService.getQuestionDetail(questionId);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("加载技术帖子详情入口失败！");
        }

        return result;
    }

    /**
     * 评论技术帖子详情入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/publicQuestionComment",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo publicQuestionComment(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            int entityId = jsonObject.getInteger("entityId");
            int entityType = jsonObject.getInteger("entityType");
            int userId = jsonObject.getInteger("userId");
            int toCommentId = jsonObject.getInteger("toCommentId");
            String content = jsonObject.getString("content");
            result = questionService.addQuestionComment(entityType,entityId,userId,content,toCommentId);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("评论技术帖子详情入口失败！");
        }

        return result;
    }

    /**
     * 加载技术评论入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/questionComment/onLoad",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo onLoadQuestionComment(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            int start = jsonObject.getInteger("start");  //start为起点，num为多少个数据
            int num = jsonObject.getInteger("num");
            int entityId = jsonObject.getInteger("entityId");
            int entityType = jsonObject.getInteger("entityType");
            result = questionService.getLatestQuestionComment(start,num,entityId,entityType); //此处的entityType指的是评论对应的问题类型
        }catch (Exception e){
            e.printStackTrace();
            logger.error("加载技术评论入口失败！");
        }


        return result;
    }

    /**
     * 点赞点踩入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/addQuestionLike",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo addQuestionLike(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            int islike = jsonObject.getInteger("isLike");  //0代表点赞，1代表点踩
            int entityId = jsonObject.getInteger("entityId");//此处如果entityType代表问题则entityId代表问题id，如果entityType代表评论则entityId代表评论id
            int entityType = jsonObject.getInteger("entityType");  //0代表对问题点赞，1代表对评论点赞
            if(islike==0){
                result=questionService.addQuestionLike(entityType,entityId,hostHolder.getUser().getId());
            }else{
                result=questionService.addQuestionDislike(entityType,entityId,hostHolder.getUser().getId());
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("点赞点踩入口失败！");
        }


        return result;
    }


    /**
     * 保存上传的图片
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/uploadImage")
    public Map<String,Object> Image(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "editormd-image-file", required = false) MultipartFile file)
    {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            request.setCharacterEncoding( "utf-8" );
            response.setHeader( "Content-Type" , "text/html" );
            ResultInfo picResult = PicUtils.savePic(file);
            if (picResult.getStatus() == 0) {
                resultMap.put("success", 1);
                resultMap.put("message", "上传成功");
                resultMap.put("url", localhost + picResult.getData());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 删除技术帖（评论）入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/deleteQuestion",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo deleteQuestion(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            int entityId = jsonObject.getInteger("entityId");//此处如果entityType代表问题则entityId代表问题id，如果entityType代表评论则entityId代表评论id
            int entityType = jsonObject.getInteger("entityType");  //0代表问题，1代表评论
            int userId = jsonObject.getInteger("userId");//操作人的id（有可能是用户自己，也有可能是管理员）
            result = questionService.deleteQuestion(entityType,entityId,userId);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("删除技术帖（评论）入口失败！");
        }


        return result;
    }

    /**
     * 搜索技术帖入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/searchQuestion",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo searchQuestion(@RequestBody JSONObject jsonObject){
        String keyword = jsonObject.getString("keyword");//操作人的id（有可能是用户自己，也有可能是管理员）
        List<Question> resultList = new ArrayList<>();
        resultList = questionDao.selectQuestionByKeyword(keyword,0,2);

        return ResultInfo.ok(resultList);
    }


}



