package com.staff.storage.minio.service;


import com.staff.storage.minio.dto.UploadResDTO;
import org.springframework.web.multipart.MultipartFile;

public interface MinioStorageService {


    UploadResDTO upload(MultipartFile file, String folderPrefix);


}
