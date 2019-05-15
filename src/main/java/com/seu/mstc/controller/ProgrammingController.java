package com.seu.mstc.controller;

import com.alibaba.fastjson.JSONObject;
import com.seu.mstc.dao.ProgrammingDao;
import com.seu.mstc.dao.UserDao;
import com.seu.mstc.model.HostHolder;
import com.seu.mstc.model.Programming;
import com.seu.mstc.result.ResultInfo;
import com.seu.mstc.service.ProgrammingService;
import com.seu.mstc.service.UserService;
import com.seu.mstc.utils.EmailUtils;
import com.seu.mstc.utils.PicUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value="/programming")
public class ProgrammingController {
    private static final Logger logger = LoggerFactory.getLogger(ProgrammingController.class);

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    UserService userService;

    @Autowired
    ProgrammingService programmingService;

    @Autowired
    UserDao userDao;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    ProgrammingDao programmingDao;

    @Value("${imageUpload.url}")
    private String url;

    @Value("${imageUpload.localhost}")
    private String localhost;


    /**
     * 加载算法编程题入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/onLoad",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo onLoadProgramming(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;

        try {
            int start = jsonObject.getInteger("start");
            int num = jsonObject.getInteger("num");
            int flag = jsonObject.getInteger("flag");
            result = programmingService.getLatestProgramming(start,num,flag);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("加载算法编程题失败！");
        }

        return result;
    }


    /**
     * 发布算法编程题入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/public",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo publicProgramming(@RequestBody JSONObject jsonObject){
        ResultInfo result = null;
        try {
            Programming programming = new Programming();
            String title = jsonObject.getString("title");
            String content = jsonObject.getString("content");
            String imageUrl = null;//图片的存储方式改为结合markdown直接存在content中
            int status = jsonObject.getInteger("status");
//        String keyWord = jsonObject.getString("keyword");
            String flagStr = jsonObject.getString("flag");
            int flag=Integer.parseInt(flagStr);
//            if(flagStr.equals("剑指offer")){
//                flag=1;
//            }else if(flagStr.equals("LeetCode")){
//                flag=2;
//            }else if(flagStr.equals("其他")){
//                flag=3;
//            }

            programming.setTitle(title);
            programming.setIdeas(content);
            programming.setImageUrl(hostHolder.getUser().getHeadUrl());
//        programming.setKeyWord(keyWord);
            programming.setFlag(flag);
            programming.setStatus(status);
            result = programmingService.addProgramming(programming);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("发布算法编程题失败！");
        }

        return result;
    }



    /**
     * 加载算法编程题详情入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/onLoadDetail",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo onLoadProgrammingDetail(@RequestBody JSONObject jsonObject){
        int questionId = jsonObject.getInteger("programmingId");            //越界处理未添加
        ResultInfo result=null;
        try {
            result = programmingService.getProgrammingDetail(questionId);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("加载算法编程题详情失败！");
        }

        return result;
    }


    /**
     * 加载热门编程题入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/onLoadHot",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo onLoadProgrammingHot(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            String beginStr=jsonObject.getString("begin");
            int begin=Integer.parseInt(beginStr);
            String endStr=jsonObject.getString("end");
            int end=Integer.parseInt(endStr);
            result = programmingService.getHotProgrammingDetail();
        } catch (Exception e){
            e.printStackTrace();
            logger.error("加载热门编程题入口失败！");
        }

        return result;
    }

    /**
     * 置顶编程题入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/isTop",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo isTopBlog(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            String idStr=jsonObject.getString("entityId");
            int id=Integer.parseInt(idStr);
            String statusStr=jsonObject.getString("topStatus");
            int status=Integer.parseInt(statusStr);
            result= programmingService.updateIsTop(status,id);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("置顶编程题入口失败！");
        }
        return result;

    }

    /**
     * 评论算法编程题详情入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/publicProgrammingComment",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo publicProgrammingComment(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            int entityId = jsonObject.getInteger("entityId");
            int entityType = jsonObject.getInteger("entityType");
            int userId = jsonObject.getInteger("userId");
            int toCommentId = jsonObject.getInteger("toCommentId");
            String content = jsonObject.getString("content");
            result = programmingService.addProgrammingComment(entityType,entityId,userId,content,toCommentId);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("评论算法编程题详情入口失败！");
        }

        return result;
    }

    /**
     * 加载算法编程题评论入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/programmingComment/onLoad",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo onLoadProgrammingComment(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            int start = jsonObject.getInteger("start");  //start为起点，num为多少个数据
            int num = jsonObject.getInteger("num");
            int entityId = jsonObject.getInteger("entityId");
            int entityType = jsonObject.getInteger("entityType");
            result = programmingService.getLatestProgrammingComment(start,num,entityId,entityType); //此处的entityType指的是评论对应的问题类型
        }catch (Exception e){
            e.printStackTrace();
            logger.error("加载算法编程题评论入口失败！");
        }


        return result;
    }

    /**
     * 点赞点踩入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/addProgrammingLike",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo addProgrammingLike(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            int islike = jsonObject.getInteger("isLike");  //0代表点赞，1代表点踩
            int entityId = jsonObject.getInteger("entityId");//此处如果entityType代表编程题则entityId代表编程题id，如果entityType代表评论则entityId代表评论id
            int entityType = jsonObject.getInteger("entityType");  //0代表对问题点赞，1代表对评论点赞
            if(islike==0){
                result=programmingService.addProgrammingLike(entityType,entityId,hostHolder.getUser().getId());
            }else{
                result=programmingService.addProgrammingDislike(entityType,entityId,hostHolder.getUser().getId());
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
     * 删除编程题（评论）入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/deleteProgramming",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo deleteProgramming(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            int entityId = jsonObject.getInteger("entityId");//此处如果entityType代表编程题则entityId代表编程题id，如果entityType代表评论则entityId代表评论id
            int entityType = jsonObject.getInteger("entityType");  //0代表问题，1代表评论
            int userId = jsonObject.getInteger("userId");//操作人的id（有可能是用户自己，也有可能是管理员）
            result = programmingService.deleteProgramming(entityType,entityId,userId);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("删除编程题（评论）入口失败！");
        }


        return result;
    }

    /**
     * 保存上传的测试代码
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/uploadTest")
    public ResultInfo uploadTest(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "image", required = false) MultipartFile file, @RequestParam(value = "id", required = false) String id)
    {
        String url = "D://MSTCPicture";    //图片存储路径
        try {
            int programmingId = Integer.parseInt(id);
            String rootPath = url;
            File filePath = new File(rootPath);
            if(!filePath.exists()){
                filePath.mkdirs();
            }
            File realFile = new File(rootPath + File.separator + file.getOriginalFilename());
            FileUtils.copyInputStreamToFile(file.getInputStream(), realFile);
            programmingDao.updateAnswer("http://www.seumstc.top:90/"+file.getOriginalFilename(),programmingId);
            return ResultInfo.ok();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return ResultInfo.build(999,"error");
    }
}
