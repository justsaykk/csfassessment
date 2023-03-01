package vttp2022.csf.assessment.server.repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.s3.AmazonS3;

@Repository
public class MapCache {

	@Autowired
	private AmazonS3 s3Client;

	private String bucketName = "csfassessmentkk";

	// TODO Task 4
	// Use this method to retrieve the map
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	public Optional<String> getMap(String id) {
		String url = s3Client.getUrl(this.bucketName, id).toString();
		System.out.printf("Map URL response from S3: %s\n", url);
		if (url.length() <= 0) {
			return Optional.empty();
		}
		return Optional.of(url);
	}

	// You may add other methods to this class

}
