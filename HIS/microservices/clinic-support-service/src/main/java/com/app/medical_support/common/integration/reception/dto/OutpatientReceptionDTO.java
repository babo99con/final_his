package com.app.medical_support.common.integration.reception.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 접수 MSA JSON 역직렬화용. {@code @AllArgsConstructor}는 두지 않는다.
 * Jackson이 생성자 바인딩으로 잘못 매핑해 {@code doctorName} 등이 null이 되는 것을 방지한다.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutpatientReceptionDTO {

    private Long receptionId;
    private Long patientId;
    private String patientName;
    private String departmentName;
    private String doctorName;
    private String status;
    private String doctorId;
    private String receptionNo;
    //private String scheduledAt;
    //private String arrivedAt;
    private String departmentId;
    private String visitType;
    private Long reservationId;
    private String note;
   // private Boolean isActive;
   // private String inactiveAt;
   // private String inactiveReasonCode;
   // private String inactiveReasonText;
    //private String cancelReasonCode;
    //private String cancelReasonText;
   // private String holdReasonCode;
    //private String holdReasonText;
    //private String createdBy;
    //private String updatedBy;
    //private String createdAt;
    //private String updatedAt;
}
