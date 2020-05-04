package com.community.community.Provider;


import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@Service
public class TencentCloudProvider {
    @Value("${tencent.cos.sercetid}")
    private String COS_SECRETID;
    @Value("${tencent.cos.sercetkey}")
    private String COS_SECRETKEY;
    @Value("${tencent.cos.bucket}")
    private String bucket;
    @Value("${tencent.cos.regionname}")
    private String regionName;


    public String upload(InputStream fileStream, String contentType, String fileName) {
        String generatedFileName;
        String[] filePaths = fileName.split("\\.");
        if (filePaths.length > 1) {
            generatedFileName = UUID.randomUUID().toString() + "." + filePaths[filePaths.length - 1];
        } else {
            return null;
        }

        COSClient cosClient = init();
        // 指定要上传到的存储桶
        String bucketName = bucket;
        // 指定要上传到 COS 上对象键
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        String key = generatedFileName;
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, fileStream, objectMetadata);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);

        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);
        // 设置签名过期时间(可选), 若未进行设置, 则默认使用 ClientConfig 中的签名过期时间(1小时)
        // 这里设置签名在10年后过期
        Date expirationDate = new Date(System.currentTimeMillis() + 10L*365L*24L * 60L * 60L * 1000L);
        req.setExpiration(expirationDate);
        String url = cosClient.generatePresignedUrl(req).toString();
        cosClient.shutdown();
        return url;
    }

    public COSClient init(){
        // 1 初始化用户身份信息（secretId, secretKey）。
        String secretId = COS_SECRETID;
        String secretKey = COS_SECRETKEY;
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region(regionName);
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);
        return cosClient;
    }
}
