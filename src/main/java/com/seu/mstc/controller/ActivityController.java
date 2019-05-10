package com.seu.mstc.controller;

import com.alibaba.fastjson.JSONObject;
import com.seu.mstc.dao.UserDao;
import com.seu.mstc.model.Activity;
import com.seu.mstc.model.ActivityComment;
import com.seu.mstc.model.HostHolder;
import com.seu.mstc.result.ResultInfo;
import com.seu.mstc.service.ActivityService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zl on 2018/10/3.
 */
@RestController
@RequestMapping(value="/activity")
public class ActivityController {
    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    UserService userService;

    @Autowired
    ActivityService activityService;
    @Autowired
    UserDao userDao;

    @Autowired
    HostHolder hostHolder;


    @Value("${imageUpload.url}")
    private String url;


    @Value("${imageUpload.localhost}")
    private String localhost;

    /**
     * 加载博客入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/onLoad",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo onLoadActivity(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            int start = jsonObject.getInteger("start");
            int num = jsonObject.getInteger("num");
            result = activityService.getActivity(start,num);

        }catch (Exception e){
            e.printStackTrace();
            logger.error("加载博客失败！");
        }
        return result;
    }

    /**
     * 置顶活动入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/isTop",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo isTopActivity(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            String idStr=jsonObject.getString("entityId");
            int id=Integer.parseInt(idStr);
            String statusStr=jsonObject.getString("topStatus");
            int status=Integer.parseInt(statusStr);
            result = activityService.updateIsTop(status,id);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("置顶活动失败！");
        }
        return result;
    }

    /**
     * 发布活动入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/publish",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo publicActivity(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            Activity activity=new Activity();
            String title=jsonObject.getString("title");
            String content=jsonObject.getString("content");
            Date createTime=new Date();

            String userId_str=jsonObject.getString("userId");
            int userId=Integer.parseInt(userId_str);

            activity.setTitle(title);
            activity.setContent(content);
            activity.setCreateTime(createTime);
            activity.setUserId(userId);
            result = activityService.addActivity(activity);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("发布活动失败！");
        }

        return result;
    }


    /**
     * 评论活动入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/comment",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo commentActivity(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            int entityId = jsonObject.getInteger("entityId");
            int entityType = jsonObject.getInteger("entityType");
            int userId = jsonObject.getInteger("userId");
            int toCommentId = jsonObject.getInteger("toCommentId");
            String content = jsonObject.getString("content");

            ActivityComment activityComment=new ActivityComment();
            activityComment.setEntityId(entityId);
            activityComment.setEntityType(entityType);
            activityComment.setUserId(userId);
            activityComment.setContent(content);
            result = activityService.addActivityComment(activityComment,toCommentId);
            //添加业务逻辑和权限判断，调用service层
        }catch (Exception e){
            e.printStackTrace();
            logger.error("评论活动失败！");
        }

        return result;
    }
    /**
     * 加载活动详情入口
     * @param jsonObject
     * @return
     */

    @RequestMapping(value="/onLoadDetail",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo onLoadQuestionDetail(@RequestBody JSONObject jsonObject){
        int activityId = jsonObject.getInteger("activityId");            //越界处理
        ResultInfo result=null;
        try {
            result = activityService.getActivityDetail(activityId);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("加载活动失败！");
        }

        return result;
    }

    /**
     * 加载活动评论入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/loadActivityComment",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo onLoadActivityComment(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            int start = jsonObject.getInteger("start");  //start为起点，num为多少个数据
            int num = jsonObject.getInteger("num");
            int entityId = jsonObject.getInteger("entityId");
            int entityType = jsonObject.getInteger("entityType");
            result = activityService.getActivityComment(start,num,entityId,entityType);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("加载活动评论失败！");
        }


        return result;
    }


    /**
     * 点赞点踩入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/addActivityLike",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo addActivityLike(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            int islike = jsonObject.getInteger("isLike");  //0代表点赞，1代表点踩
            int entityId = jsonObject.getInteger("entityId");//此处如果entityType代表问题则entityId代表问题id，如果entityType代表评论则entityId代表评论id
            int entityType = jsonObject.getInteger("entityType");  //0代表对问题点赞，1代表对评论点赞
            if(islike==0){
                result=activityService.addActivityLike(entityType,entityId,hostHolder.getUser().getId());
            }else{
                result=activityService.addActivityDislike(entityType,entityId,hostHolder.getUser().getId());
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("活动点赞点踩失败！");
        }

        return result;
    }

    /**
     * 删除活动（评论）入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/deleteActivity",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo deleteActivity(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            int entityId = jsonObject.getInteger("entityId");//此处如果entityType代表问题则entityId代表问题id，如果entityType代表评论则entityId代表评论id
            int entityType = jsonObject.getInteger("entityType");  //0代表活动内容，1代表评论
            int userId = jsonObject.getInteger("userId");//操作人的id（有可能是用户自己，也有可能是管理员）
            result = activityService.deleteActivity(entityType,entityId,userId);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("删除活动失败！");
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
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            request.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type", "text/html");

            ResultInfo picResult = PicUtils.savePic(file);
            if (picResult.getStatus() == 0) {
                resultMap.put("success", 1);
                resultMap.put("message", "上传成功");
                resultMap.put("url", localhost + picResult.getData());
            }
        }
//            String rootPath = url;
//            //文件路径不存在则需要创建文件路径
//            File filePath = new File(rootPath);
//            if(!filePath.exists()){
//                filePath.mkdirs();
//            }
        //最终文件名
//            File realFile = new File(rootPath + File.separator + file.getOriginalFilename());
////            String localhost = "http://www.seumstc.top:90/";
//            FileUtils.copyInputStreamToFile(file.getInputStream(), realFile);
//            resultMap.put("success", 1);
//            resultMap.put("message", "上传成功");
//            resultMap.put("url", localhost+file.getOriginalFilename());
        catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }
}
