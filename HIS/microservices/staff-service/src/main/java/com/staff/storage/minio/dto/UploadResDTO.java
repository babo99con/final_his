package com.staff.storage.minio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadResDTO {

    private String fileUrl;
    private String objectKey;
    private String contentType;
    private Long size;
    private String originalName;
}
