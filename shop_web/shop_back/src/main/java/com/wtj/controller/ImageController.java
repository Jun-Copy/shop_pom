package com.wtj.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/image")
public class ImageController {

	//导入fastFile类
	@Autowired
	private FastFileStorageClient storageClient;

	@RequestMapping("/upload")
	@ResponseBody
	public String uploadImage(MultipartFile file){
		String fileName = file.getOriginalFilename();
		int index = fileName.indexOf(".");
		System.out.println(fileName+"：正在上传。。。");
		/*上传到fastDFS*/
		try{
			//上传并且生成略缩图，参数为 文件输入流，文件大小，文件后缀名，
			StorePath storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(),
					file.getSize(),
					fileName.substring(index + 1),null);
			String fullPath = storePath.getFullPath();
			System.out.println(fullPath);
			//将图片路径返回给页面，用来添加到数据库中
			return "{\"uploadPath\":\""+fullPath+"\"}";
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*上传到本地
		try (
				InputStream in = file.getInputStream();
				OutputStream out = new FileOutputStream("D:\\images\\"+ UUID.randomUUID()+fileName.substring(index));
				){
			IOUtils.copy(in,out);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return null;
	}
}
