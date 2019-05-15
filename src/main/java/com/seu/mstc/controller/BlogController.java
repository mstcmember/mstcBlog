package com.seu.mstc.controller;

import com.alibaba.fastjson.JSONObject;
import com.seu.mstc.dao.UserDao;
import com.seu.mstc.model.Blog;
import com.seu.mstc.model.BlogComment;
import com.seu.mstc.model.HostHolder;
import com.seu.mstc.result.ResultInfo;
import com.seu.mstc.service.BlogService;
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
@RequestMapping(value="/blog")
public class BlogController {
    private static final Logger logger = LoggerFactory.getLogger(BlogController.class);

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    UserService userService;

    @Autowired
    BlogService blogService;
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
    public ResultInfo onLoadBlog(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            String flag_str=jsonObject.getString("flag");
            int flag=Integer.parseInt(flag_str);
            int start = jsonObject.getInteger("start");
            int num = jsonObject.getInteger("num");
            result = blogService.getBlogsbyflag(start,num,flag);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("加载博客失败！");
        }

        return result;
    }

    /**
     * 加载热门博客入口
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
            result = blogService.getHotBlogDetail();
        }catch (Exception e){
            e.printStackTrace();
            logger.error("加载热门博客失败！");
        }

        return result;
    }

    /**
     * 置顶博客入口
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
            result = blogService.updateIsTop(status,id);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("置顶博客失败！");
        }

        return result;
    }

    /**
     * 发布博文入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/publish",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo publicBlog(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            Blog blog=new Blog();
            String title=jsonObject.getString("title");
            //String blogAbstract=jsonObject.getString("blogAbstract");
            String content=jsonObject.getString("content");
            //String imageUrl=jsonObject.getString("imageUrl");
            Date createTime=new Date();
            String flag_str=jsonObject.getString("flag");
            int flag=Integer.parseInt(flag_str);
            String keyWord=jsonObject.getString("keyword");
            String status_str=jsonObject.getString("status");
            int status=Integer.parseInt(status_str);
            String userId_str=jsonObject.getString("userId");
            int userId=Integer.parseInt(userId_str);
            blog.setTitle(title);
            blog.setContent(content);
            blog.setCreateTime(createTime);
            blog.setFlag(flag);
            blog.setKeyWord(keyWord);
            blog.setStatus(status);
            blog.setUserId(userId);
            blog.setHotScore(0);
            result = blogService.addBlog(blog);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("发布博文失败！");
        }


        return result;
    }


    /**
     * 评论博文入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/comment",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo commentBlog(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            int entityId = jsonObject.getInteger("entityId");
            int entityType = jsonObject.getInteger("entityType");
            int userId = jsonObject.getInteger("userId");
            int toCommentId = jsonObject.getInteger("toCommentId");
            String content = jsonObject.getString("content");

            BlogComment blogComment=new BlogComment();
            blogComment.setEntityId(entityId);
            blogComment.setEntityType(entityType);
            blogComment.setUserId(userId);
            blogComment.setContent(content);
            result = blogService.addComment(blogComment,toCommentId);
            //添加业务逻辑和权限判断，调用service层
        }catch (Exception e){
            e.printStackTrace();
            logger.error("评论博文失败！");
        }

        return result;
    }
    /**
     * 加载博客详情入口
     * @param jsonObject
     * @return
     */

    @RequestMapping(value="/onLoadDetail",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo onLoadQuestionDetail(@RequestBody JSONObject jsonObject){
        int blogId = jsonObject.getInteger("blogId");            //越界处理
        ResultInfo result=null;
        try {
            result = blogService.getBlogDetail(blogId);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("加载博客详情失败！");
        }

        return result;
    }

    /**
     * 加载博客评论入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/loadBlogComment",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo onLoadBlogComment(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            int start = jsonObject.getInteger("start");  //start为起点，num为多少个数据
            int num = jsonObject.getInteger("num");
            int entityId = jsonObject.getInteger("entityId");
            int entityType = jsonObject.getInteger("entityType");
            result = blogService.getLatestBlogComment(start,num,entityId,entityType);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("加载博客评论失败！");
        }
        return result;
    }


    /**
     * 点赞点踩入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/addBlogLike",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo addBlogLike(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            int islike = jsonObject.getInteger("isLike");  //0代表点赞，1代表点踩
            int entityId = jsonObject.getInteger("entityId");//此处如果entityType代表问题则entityId代表问题id，如果entityType代表评论则entityId代表评论id
            int entityType = jsonObject.getInteger("entityType");  //0代表对问题点赞，1代表对评论点赞
            if(islike==0){
                result=blogService.addBlogLike(entityType,entityId,hostHolder.getUser().getId());
            }else{
                result=blogService.addBlogDislike(entityType,entityId,hostHolder.getUser().getId());
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("点赞点踩失败！");
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
        Map<String,Object> resultMap = new HashMap<String,Object>();
        try {
            request.setCharacterEncoding( "utf-8" );
            response.setHeader( "Content-Type" , "text/html" );
            ResultInfo picResult = PicUtils.savePic(file);
            if (picResult.getStatus() == 0) {
                resultMap.put("success", 1);
                resultMap.put("message", "上传成功");
                resultMap.put("url", localhost + picResult.getData());
            }
//            String rootPath = url;
//            //文件路径不存在则需要创建文件路径
//            File filePath = new File(rootPath);
//            if(!filePath.exists()){
//                filePath.mkdirs();
//            }
//            //最终文件名
//            File realFile = new File(rootPath + File.separator + file.getOriginalFilename());
////            String localhost = "http://www.seumstc.top:90/";
//            FileUtils.copyInputStreamToFile(file.getInputStream(), realFile);
//            resultMap.put("success", 1);
//            resultMap.put("message", "上传成功");
//            resultMap.put("url", localhost+file.getOriginalFilename());
        } catch (Exception e) {
                e.printStackTrace();
            }
        return resultMap;
    }

    /**
     * 删除博客（评论）入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/deleteBlog",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo deleteBlog(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            int entityId = jsonObject.getInteger("entityId");//此处如果entityType代表问题则entityId代表问题id，如果entityType代表评论则entityId代表评论id
            int entityType = jsonObject.getInteger("entityType");  //0代表问题，1代表评论
            int userId = jsonObject.getInteger("userId");//操作人的id（有可能是用户自己，也有可能是管理员）
            result = blogService.deleteBlog(entityType,entityId,userId);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("删除博客失败！");
        }
        return result;
    }
}
