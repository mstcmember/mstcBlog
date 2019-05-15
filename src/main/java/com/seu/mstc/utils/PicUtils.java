package com.seu.mstc.utils;

import com.seu.mstc.result.ResultInfo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by XC on 2018/10/11
 * 本类用于图片的相关操作（上传，删除等）
 */

public class PicUtils {


    private final static String url = "D://MSTCPicture";    //图片存储路径

    //保存图片的函数，返回值中status为0代表成功，1代表传入文件有问题，data中存放保存时的文件名
    //传入参数file为图片文件
    public static ResultInfo savePic(MultipartFile file){
        try {
            String rootPath = url;
            //文件路径不存在则需要创建文件路径
            File filePath = new File(rootPath);
            if(!filePath.exists()){
                filePath.mkdirs();
            }
            //最终文件名
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0) {
                return ResultInfo.build(0,"picError");//返回1代表传入的文件有问题
            }
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
            File realFile = new File(rootPath + File.separator + fileName);
            FileUtils.copyInputStreamToFile(file.getInputStream(), realFile);
            return ResultInfo.build(0,"OK",fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultInfo.build(999,"error");
        }
    }

    //删除图片的函数，传入参数content为包含图片url的内容，url为图片在磁盘中存储的绝对路径
    //返回0代表正常，返回1代表失败
    public static int deletePic(String content,String url){    //content为包含图片url的内容，url为图片在磁盘中存储的绝对路径
        List<String> picUrlList = new ArrayList<>(); //用于存放该回复中的所有图片的url
        String picUrlMatch = "!\\[.*\\]\\(http://www\\.seumstc\\.top\\.:90/.*\\.jpg\\)";
        Pattern p = Pattern.compile(picUrlMatch);
        Matcher m =p.matcher(content);
        while(m.find()){
            picUrlList.add(m.group());
        }
        for(String picUrl:picUrlList) {
            String realPicUrl = url+StringUtils.substringBeforeLast(StringUtils.substringAfterLast(picUrl,"/"),")");
            try {
                File file = new File(realPicUrl);
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
                return 1;
            }
        }
        return 0;
    }

}
