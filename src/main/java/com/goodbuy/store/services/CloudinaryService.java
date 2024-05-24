package com.goodbuy.store.services;

import com.cloudinary.Cloudinary;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {

	@Resource
	private Cloudinary cloudinary;

	public String uploadFile(MultipartFile file, long productId) throws IOException {
			HashMap<Object, Object> options = new HashMap<>();
			options.put("upload_preset", "goodbuyStore");
			String fileName = new StringBuilder("IMG-").append(productId).append("-").append(Instant.now()).toString();
			options.put("public_id", fileName);
			Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
			String publicId = (String) uploadedFile.get("public_id");
			return cloudinary.url().secure(true).generate(publicId);
	}
}