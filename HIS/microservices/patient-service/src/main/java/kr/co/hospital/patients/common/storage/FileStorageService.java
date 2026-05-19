package kr.co.hospital.patients.common.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String save(MultipartFile file, String category); // category = "consents", "insurances" ...
}
