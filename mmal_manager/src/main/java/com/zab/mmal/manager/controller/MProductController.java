package com.zab.mmal.manager.controller;

import com.google.common.collect.Lists;
import com.zab.mmal.api.entity.MmallProduct;
import com.zab.mmal.common.annotions.AdminOperate;
import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.common.enums.SysCodeMsg;
import com.zab.mmal.common.exceptions.WrongArgumentException;
import com.zab.mmal.common.utils.JudgeUtil;
import com.zab.mmal.common.config.FtpConfiguration;
import com.zab.mmal.manager.feign.ManagerFeignService;
import com.zab.mmal.manager.utils.FTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/manage/product")
@AdminOperate
@Slf4j
public class MProductController {

    @Autowired
    private ManagerFeignService feignService;
    @Resource
    private FtpConfiguration ftpConfiguration;
    @Resource
    private FTPUtil ftpUtil;

    @PostMapping("productSave")
    public ReturnData productSave(HttpServletRequest request, @RequestBody MmallProduct product) {
        return feignService.productSave(product);
    }

    @PostMapping("setSaleStatus")
    public ReturnData setSaleStatus(HttpServletRequest request, @RequestParam Integer productId, @RequestParam Integer status) {
        if (JudgeUtil.isNullOr(productId, status)) {
            return new ReturnData(SysCodeMsg.FAIL.getCode(), "产品id和状态都不能为空");
        }
        return feignService.setSaleStatus(productId, status);
    }

    @GetMapping("getProductDetail/{productId}")
    public ReturnData getProductDetail(HttpServletRequest request, @PathVariable(value = "productId") Integer productId) {
        return new ReturnData(feignService.getProductDetail(productId));
    }

    @GetMapping("pageProduct")
    public ReturnData pageProduct(HttpServletRequest request, @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                  @RequestParam(required = false, defaultValue = "1") Integer pageNo, @RequestParam(required = false) String productName,
                                  @RequestParam(required = false) Integer productId) {
        return feignService.pageProduct(pageNo, pageSize, productName, productId);
    }

    @PostMapping("upload")
    public ReturnData upload(HttpServletRequest request, @RequestBody MultipartFile file) {
        String targetFileName = this.uploadFile(request, file);
        String url = ftpConfiguration.getServerHttpPrefix() + targetFileName;
        Map<String, Object> map = new HashMap<>();
        map.put("uri", targetFileName);
        map.put("url", url);
        return new ReturnData(map);
    }

    @PostMapping("richtextImgUpload")
    public ReturnData richtextImgUpload(HttpServletRequest request, HttpServletResponse response, @RequestBody MultipartFile file) {
        // 富文本中对于返回值有自己的要求，我们使用是simditor所以按照是simditor的要求返回
        String targetFileName = this.uploadFile(request, file);
        String url = ftpConfiguration.getServerHttpPrefix() + targetFileName;
        response.setHeader("Access-Control-Allow-Headers", "X-File-Name");
        return new ReturnData("上传成功", url);
    }

    private String uploadFile(HttpServletRequest request, MultipartFile file) {
        String path = request.getServletContext().getRealPath("upload");
        String fileName = file.getOriginalFilename();
        String extensionName = StringUtils.substringAfterLast(fileName, ".");
        String uploadName = UUID.randomUUID().toString() + "." + extensionName;

        log.info("开始上传文件,上传文件的文件名:{},上传文件的路径:{},新文件名:{}", fileName, path, uploadName);
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdir();
        }

        File targetFile = new File(path, uploadName);
        try {
            file.transferTo(targetFile);
            //到这里文件已经上传到本地path文件下

            ftpUtil.uploadFile("img",Lists.newArrayList(targetFile));

            targetFile.delete();
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new WrongArgumentException("文件上传失败", e);
        }

        return targetFile.getName();
    }

}
