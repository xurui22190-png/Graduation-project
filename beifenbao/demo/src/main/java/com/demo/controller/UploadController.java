package com.demo.controller;

import com.demo.common.ResponseResult;
import io.swagger.annotations.Api;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/pub")
@Api(value = "UploadController", tags = "上传文件", description = "")
public class UploadController {

    @Value("${web.upload-path}")
    public String saveUploadPath;

    @PostMapping(value = "uploadfile")
    public ResponseResult uploadfile(@RequestParam(required = false) MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return ResponseResult.Fail("没有文件");
        }

        try {
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            String newFileName = UUID.randomUUID() + "." + ext;
            String newFilePath = saveUploadPath + newFileName;

            File dir = new File(saveUploadPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File newFile = new File(newFilePath);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(newFile));
            out.write(file.getBytes());
            out.flush();
            out.close();

            return ResponseResult.success("上传成功", newFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ResponseResult.Fail("上传失败");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseResult.Fail("上传失败");
        }
    }
}