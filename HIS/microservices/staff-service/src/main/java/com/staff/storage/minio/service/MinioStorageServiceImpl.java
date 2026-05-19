package com.staff.storage.minio.service;


import com.staff.storage.minio.dto.UploadResDTO;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import com.staff.common.exception.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class MinioStorageServiceImpl implements MinioStorageService {

    private final MinioClient minioClient;



    @Value("${minio.bucket}")  //이름
    private String bucket;

    @Value("${minio.public-base-url}")   //주소
    private String publicBaseUrl;

    // ✅ bucketExists 매번 치지 않게 캐싱 (MVP 이상에서 체감 큼)
    private final Set<String> ensuredBuckets = ConcurrentHashMap.newKeySet();


    @Override
    public UploadResDTO upload(MultipartFile file,  //내가 업로드할 실제 파일
                               String Miniofolder) //MinIO 안에서 저장할 폴더
    {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 비어있습니다.");
        }

        try {
            String originalName = file.getOriginalFilename();
            // 사용자가 업로드한 파일의 원래 파일명을 꺼냄 (내장객체)

            String Miniosetting = Filesetting(originalName);   // 미니엄 파일 설정
            String Miniofolders = Foldersetting(Miniofolder);  // 미니엄 폴더 설정

            //✅여기서 폴더 경로 지정
            String objectKey = Miniofolders + "/" + UUID.randomUUID() + "_" + Miniosetting;

            //✅버킷(bucket)은 객체 저장소에서 파일들을 담아두는 최상위 보관함
            ensureBucketOnce(bucket);



            String contentType = (file.getContentType() == null || file.getContentType().isBlank())
                    ? "application/octet-stream"
                    : file.getContentType();

            long size = file.getSize();

            try (InputStream in = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucket)
                                .object(objectKey)
                                .stream(in, size, -1)
                                .contentType(contentType)
                                .build()
                );
            }
            // ✅ private bucket 기준: presigned URL 생성
            String fileUrl = buildPublicUrl(bucket, objectKey);

            return new UploadResDTO(
                    fileUrl,
                    objectKey,
                    contentType,
                    size,
                    originalName
            );

        } catch (Exception e) {
            throw new FileUploadException("MinIO 업로드 실패: " + e.getMessage(), e);
        }
    }




    /**
     * ✅ private bucket 객체를 잠시 열어주는 미리보기 URL
     */
    private String createPresignedGetUrl(String objectKey) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucket)
                        .object(objectKey)
                        .expiry(60 * 60) // 1시간
                        .build()
        );
    }














    /**
     * ✅ 폴더설정 설정:
     * - null/blank -> default
     * - 앞/뒤 슬래시 제거
     * - "//" 같은 중복 슬래시 정리
     * - ".." 같은 위험한 경로 제거
     */
    private String Foldersetting(String prefix) {
        if (prefix == null || prefix.isBlank()) return "default";

        String p = prefix.trim();

        // 역슬래시 들어오면 슬래시로 통일
        p = p.replace("\\", "/");

        // 앞/뒤 / 제거
        while (p.startsWith("/")) p = p.substring(1);
        while (p.endsWith("/")) p = p.substring(0, p.length() - 1);

        // 중복 슬래시 정리
        while (p.contains("//")) p = p.replace("//", "/");

        // 위험한 경로 요소 제거(최소 방어)
        p = p.replace("..", "");

        return p.isBlank() ? "default" : p;
    }

    /**
     * ✅ 파일 설정:
     * 파일경로 지정
     * - null -> file
     * - 공백 -> _
     * - 경로 문자 제거
     */
    private String Filesetting(String originalName) {
        String name = (originalName == null || originalName.isBlank()) ? "file" : originalName.trim();
        name = name.replaceAll("\\s+", "_");
        name = name.replace("\\", "_").replace("/", "_");
        return name;
    }

    /**
     * ✅ publicBaseUrl 끝에 / 있으면 // 생기는 것 방지
     */
    private String buildPublicUrl(String bucket, String objectKey) {
        String base = (publicBaseUrl == null) ? "" : publicBaseUrl.trim();
        while (base.endsWith("/")) base = base.substring(0, base.length() - 1);

        // objectKey는 이미 prefix/uuid_name 형태라 앞에 / 안 붙임
        return base + "/" + bucket + "/" + objectKey;
    }

    /**
     * ✅ 버킷 존재 확인은 "버킷당 1회"만 수행
     */
    private void ensureBucketOnce(String bucket) throws Exception {
        if (ensuredBuckets.contains(bucket)) return;

        synchronized (ensuredBuckets) {
            if (ensuredBuckets.contains(bucket)) return;

            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucket).build()
            );
            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucket).build()
                );
            }
            ensuredBuckets.add(bucket);
        }
    }
}