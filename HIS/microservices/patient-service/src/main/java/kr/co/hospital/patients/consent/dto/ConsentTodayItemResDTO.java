package kr.co.hospital.patients.consent.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsentTodayItemResDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long patientId;
    private String patientName;
    private String patientNo;
    private LocalDateTime updatedAt;
}
