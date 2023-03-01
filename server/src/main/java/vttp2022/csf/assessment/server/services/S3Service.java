package vttp2022.csf.assessment.server.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

    @Autowired
    private AmazonS3 s3Client;

    private String bucketName = "csfassessmentkk";

    public void upload(byte[] file, String id) throws IOException {

        // User data
        Map<String, String> userData = new HashMap<>();
        userData.put("name", "fred");
        userData.put("uploadTime", (new Date()).toString());
        userData.put("originalFilename", id);

        // Metadata of the file
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setUserMetadata(userData);

        
        // Create a put request
        InputStream is = new ByteArrayInputStream(file);
        PutObjectRequest putReq = new PutObjectRequest(this.bucketName,
            "myobjects/%s".formatted(id),
            is,
            metadata)
            .withCannedAcl(CannedAccessControlList.PublicRead); // Allow public access

        s3Client.putObject(putReq);
    }
}