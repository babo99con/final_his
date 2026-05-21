package com.staff.domain.employee.doctor.entity;

import jakarta.persistence.*;
import com.staff.domain.employee.basicInfo.entity.StaffEntity;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "EMPLOYEE_DOCTOR", schema = "HOSPITAL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorEntity {
    @Id
    @Column(name = "STAFF_ID", nullable = false, length = 30)
    private String staffId;

    @Column(name = "LICENSE_NO", nullable = false, length = 50)
    private String licenseNo;

    @Column(name = "DOCTOR_TYPE", nullable = false, length = 30)
    private String doctorType;

    @Column(name = "SPECIALTY_ID", length = 30)
    private String specialtyId;

    @Column(name = "PROFILE_SUMMARY", length = 200)
    private String profileSummary;

    @Column(name = "DOCTOR_FILE_URL", length = 1000)
    private String doctorFileUrl;

    @Column(name = "EDUCATION", length = 1000)
    private String education;

    @Column(name = "CAREER_DETAIL", length = 1000)
    private String careerDetail;

    @Column(name = "EXT_NO", length = 30)
    private String extNo;


//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "CREATED_AT", nullable = false)
//    private Date createdAt;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "UPDATED_AT", nullable = false)
//    private Date updatedAt;





    //@PreUpdate 媛 萸먮깘
    //湲곗〈 ?뷀꽣?곌? ?섏젙(update) ?섍린 吏곸쟾 ?ㅽ뻾
    //JPA媛 ?몄꽕?몄쟾???먮룞?ㅽ뻾
//    @PrePersist
//    protected void onCreate() {
//        Date now = new Date();
//        if (createdAt == null) createdAt = now;
//        updatedAt = now;
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        updatedAt = new Date();
//    }
//

    /**
     * 怨듯넻 吏곸썝 ?덈툕 李몄“.
     * STAFF_ID瑜?PK?댁옄 FK濡??ъ슜?섎뒗 1:1 ?앸퀎 愿怨꾨? JPA ?곌?愿怨꾨줈 ?쒗쁽?쒕떎.
     * ?ㅼ젣 ?곌린 而щ읆? staffId ?꾨뱶媛 ?대떦?섎?濡??곌? ?꾨뱶??議고쉶/?먯깋?⑹쑝濡??붾떎.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STAFF_ID", referencedColumnName = "STAFF_ID", insertable = false, updatable = false)
    private StaffEntity employee;

}