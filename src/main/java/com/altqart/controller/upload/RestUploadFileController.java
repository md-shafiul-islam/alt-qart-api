package com.altqart.controller.upload;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.altqart.helper.services.HelperServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/upload")
public class RestUploadFileController {

	@Autowired
	private HelperServices helperServices;

	@PostMapping(value = "/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadGivaenFileScOne(@RequestParam(name = "files") MultipartFile file) {

		log.info("Product Image Upoad ....");
		Map<String, Object> map = new HashMap<>();
		map.put("response", helperServices.uploadImageAndGetUrl(file, "products"));
		map.put("status", true);
		map.put("message", "Image uploaded");

		return ResponseEntity.ok(map);

	}

	@RequestMapping(value = "/source-two", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.PUT)
	public String uploadGivaenFileScTwo(Principal principal, @RequestParam(name = "scFileTwo") MultipartFile file) {
		log.info("File Upload Run");
		return helperServices.uploadImageAndGetUrl(file, "source-two");

	}

	@RequestMapping(value = "/image-gallery", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.PUT)
	public String uploadGivaenFileImageGallery(Principal principal,
			@RequestParam(name = "imageFile") MultipartFile file) {
		log.info("File Upload Run");
		return helperServices.uploadImageAndGetUrl(file, "image-gallery");

	}

	@RequestMapping(value = "/user-file/{dir}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.PUT)
	public String uploadUserAttachFileImage(Principal principal, @RequestParam(name = "attachFile") MultipartFile file,
			@PathVariable("dir") String dirPath) {
		log.info("File User Upload Run!! Dir Path: " + dirPath);
		return helperServices.uploadImageAndGetUrl(file, dirPath);

	}

	@RequestMapping(value = "/account/{dir}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.PUT)
	public String uploadAccountsFile(Principal principal, @RequestParam(name = "attachFile") MultipartFile file,
			@PathVariable("dir") String dirPath) {
		log.info("File User Upload Run!! Dir Path: " + dirPath);
		return helperServices.uploadImageAndGetUrl(file, dirPath);

	}

}
