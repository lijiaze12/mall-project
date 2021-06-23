package com.changgou.file.controller;



import com.changgou.file.service.PicUploadFileSystemService;
import com.changgou.file.vo.PicUploadResult;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/*
    提供一个上传图片到本地的接口
 */
@RequestMapping("pic/upload")
@Controller
public class LocalFileController {

    @Autowired
    private PicUploadFileSystemService picUploadService;
    /**
     *
     * @param uploadFile
     * @return
     * @throws Exception
     */
    @PostMapping
    @ResponseBody
    public PicUploadResult upload(@RequestParam("file") MultipartFile uploadFile)
            throws Exception {
        return this.picUploadService.upload(uploadFile);
    }

}
