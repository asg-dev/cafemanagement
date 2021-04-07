package com.org.cafemgmt.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;

@Service
public class SaveToS3ServiceImpl implements SaveToS3Service {

    final String accessKey = System.getenv("S3_KEY");
    final String secretKey = System.getenv("S3_SECRET");

    AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

    AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.US_EAST_2)
            .build();

    final String BUCKET_NAME = "freshbrew-cafemgmt";

    @Override
    public URL saveImageToS3(File image, String imageKey) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME,
                imageKey, image);
        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead); // setting access to public for all images
        s3Client.putObject(putObjectRequest);
        return s3Client.getUrl(BUCKET_NAME, imageKey);
    }
}