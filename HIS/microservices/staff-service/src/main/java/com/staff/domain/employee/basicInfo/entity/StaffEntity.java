package com.staff.domain.employee.basicInfo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "EMPLOYEE", schema = "HOSPITAL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffEntity {


    @Id
    @Column(name = "STAFF_ID", nullable = false, length = 30)
    private String staffId;

    @Column(name = "DEPT_ID", nullable = false, length = 30)
    private String deptId;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "PHONE", length = 50)
    private String phone;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "BIRTH_DATE", length = 6)
    private String birthDate;

    @Column(name = "GENDER_CODE", length = 1)
    private String genderCode;

    @Column(name = "ZIP_CODE", length = 20)
    private String zipCode;

    @Column(name = "ADDRESS1", length = 255)
    private String address1;

    @Column(name = "ADDRESS2", length = 255)
    private String address2;

    @Column(name = "STATUS", nullable = false, length = 20)
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT", nullable = false)
    private Date updatedAt;



    //@PreUpdate
    //湲곗〈 ?뷀꽣?곌? ?섏젙(update) ?섍린 吏곸쟾 ?ㅽ뻾
    //JPA媛 ?몄꽕?몄쟾???먮룞?ㅽ뻾
    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        if (createdAt == null) createdAt = now;
        updatedAt = now;
        if (status == null || status.isBlank()) status = "ACTIVE";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }





    /**
     * ?섏궗 ?곸꽭 1:1 ?곌?愿怨?
     * 怨듯넻 吏곸썝 ?앹꽦 ???섏궗 ?곸꽭媛 ?곌껐?섎뒗 援ъ“瑜?諛섏쁺?쒕떎.
     */
    @OneToOne(mappedBy = "employee", fetch = FetchType.LAZY)
    private com.staff.domain.employee.doctor.entity.DoctorEntity doctor;

    /**
     * 媛꾪샇???곸꽭 1:1 ?곌?愿怨?
     * 怨듯넻 吏곸썝 ?앹꽦 ??媛꾪샇???곸꽭媛 ?곌껐?섎뒗 援ъ“瑜?諛섏쁺?쒕떎.
     */
    @OneToOne(mappedBy = "employee", fetch = FetchType.LAZY)
    private com.staff.domain.employee.nurse.entity.NurseEntity nurse;

    /**
     * ?먮Т ?곸꽭 1:1 ?곌?愿怨?
     * 怨듯넻 吏곸썝 ?앹꽦 ??媛꾪샇???곸꽭媛 ?곌껐?섎뒗 援ъ“瑜?諛섏쁺?쒕떎.
     */
    @OneToOne(mappedBy = "employee", fetch = FetchType.LAZY)
    private com.staff.domain.employee.reception.entity.ReceptionEntity reception;
}
