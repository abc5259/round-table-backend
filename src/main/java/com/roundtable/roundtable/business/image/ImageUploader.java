package com.roundtable.roundtable.business.image;


import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {
    String upload(MultipartFile file);
}
