package com.shopme.common;

public class Constants {
//    public static final String S3_BASE_URI;
//
//    static {
//        String bucketName = "shopme-filesdir"; // Tên bucket của bạn
//        String region = "ap-southeast-1"; // Vùng của S3
//        String pattern = "https://%s.s3.%s.amazonaws.com";
//
//        S3_BASE_URI = String.format(pattern, bucketName, region);
//    }
	
public static final String S3_BASE_URI;
	
	static {
		String bucketName = System.getenv("AWS_BUCKET_NAME");
		String region = System.getenv("AWS_REGION");
		String pattern = "https://%s.s3.%s.amazonaws.com";
		
		S3_BASE_URI = bucketName == null ? "" : String.format(pattern, bucketName, region);
	}
}

