package com.founder.bussiness.FileOperate.Controller;

/**
 * @author: inwei
 * @create: ${Date} ${time}
 * @description:
 * @param: ${params}
 * @return: ${returns}
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.easymap.management.user.User;
import com.founder.bussiness.Base.BaseAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Scope("prototype")
@RequestMapping("/FileUpload")
public class FileUploadController  extends BaseAction {

	/*
	 * SpringMVC中的文件上传
	 * @第一步：由于SpringMVC使用的是commons-fileupload实现，故将其组件引入项目中
	 * @这里用到的是commons-fileupload-1.2.1.jar和commons-io-1.3.2.jar
	 * @第二步：spring-mvx中配置MultipartResolver处理器。可在此加入对上传文件的属性限制
	 *  <bean id="multipartResolver"
	 *  class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
	 *     <!-- 设置上传文件的最大尺寸为10MB -->
	 *        <property name="maxUploadSize">
	 *            <value>10000000</value>
	 *         </property>
	 * </bean>
	 * 第三步：在Controller的方法中添加MultipartFile参数。该参数用于接收表单中file组件的内容
	 *第四步：编写前台表单。注意enctype="multipart/form-data"以及<input type="file" name="****"/>
	 *  如果是单个文件 直接使用MultipartFile 即可
	 */
    @RequestMapping(params = "method=upload")
    public ModelAndView upload(@RequestParam("file") MultipartFile[] file, HttpServletRequest request) throws IllegalStateException,
        IOException {

        //页面参数
        Map<String, Object> param  = this.getParameterMap2(request);
        HttpSession session = request.getSession(true);
        String userid = "";
        //获取session中的userid
        User user = (User) session.getAttribute("user");
        if(user!=null){
            userid = user.getId();
        }

        //获取文件 存储位置
        String realPath = request.getSession().getServletContext()
            .getRealPath("/uploadFile");
        if(userid != null && !"".equals(userid)){
            realPath = realPath + "/" + userid;
        }


        File pathFile = new File(realPath);

        if (!pathFile.exists()) {
            //文件夹不存 创建文件
            pathFile.mkdirs();
        }
        for (MultipartFile f : file) {

            System.out.println("文件类型："+f.getContentType());
            System.out.println("文件名称："+f.getOriginalFilename());
            System.out.println("文件大小:"+f.getSize());
            System.out.println(".................................................");
            //将文件copy上传到服务器
            f.transferTo(new File(realPath + "/" + f.getOriginalFilename()));
            //FileUtils.copy
        }

        return null;
    }


     /**
      *@author: jinwei【jin_wei@founder.com.cn】
      *@description: 下载
      *@create: 2018/3/7 21:49
      */
    @RequestMapping(params = "method=download")
    public ModelAndView download(HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        //页面参数
        Map<String, Object> param  = this.getParameterMap2(request);

//      String storeName = "Spring3.xAPI_zh.chm";
        String storeName= (String) param.get("fileName");
        String contentType = "application/octet-stream";
        FileUploadController.download(request, response, storeName, contentType);
        return null;
    }


    //文件下载 主要方法
    public static void download(HttpServletRequest request,
                                HttpServletResponse response, String storeName, String contentType
    ) throws Exception {

        request.setCharacterEncoding("UTF-8");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        //获取项目根目录
        String ctxPath = request.getSession().getServletContext()
            .getRealPath("");

        //获取session中的userid
        HttpSession session = request.getSession(true);
        String userid = "";
        User user = (User) session.getAttribute("user");
        if(user!=null){
            userid = user.getId();
        }

        //获取下载文件露肩
        String downLoadPath = ctxPath+"/uploadFile/"+ storeName;
        if(userid != null && !"".equals(userid)){
            downLoadPath = ctxPath+"/uploadFile/"+ "/"+ userid + "/" + storeName;
        }

        //获取文件的长度
        long fileLength = new File(downLoadPath).length();

        //设置文件输出类型
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment; filename="
            + new String(storeName.getBytes("utf-8"), "ISO8859-1"));
        //设置输出长度
        response.setHeader("Content-Length", String.valueOf(fileLength));
        //获取输入流
        bis = new BufferedInputStream(new FileInputStream(downLoadPath));
        //输出流
        bos = new BufferedOutputStream(response.getOutputStream());
        byte[] buff = new byte[2048];
        int bytesRead;
        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
            bos.write(buff, 0, bytesRead);
        }
        //关闭流
        bis.close();
        bos.close();
    }

}
