package com.staff.domain.employee.basicInfo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "EMPLOYEE", schema = "JCH")
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
    //기존 엔터티가 수정(update) 되기 직전 실행
    //JPA가 인설트전에 자동실행
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
     * 의사 상세 1:1 연관관계.
     * 공통 직원 생성 후 의사 상세가 연결되는 구조를 반영한다.
     */
    @OneToOne(mappedBy = "employee", fetch = FetchType.LAZY)
    private com.staff.domain.employee.doctor.entity.DoctorEntity doctor;

    /**
     * 간호사 상세 1:1 연관관계.
     * 공통 직원 생성 후 간호사 상세가 연결되는 구조를 반영한다.
     */
    @OneToOne(mappedBy = "employee", fetch = FetchType.LAZY)
    private com.staff.domain.employee.nurse.entity.NurseEntity nurse;

    /**
     * 원무 상세 1:1 연관관계.
     * 공통 직원 생성 후 간호사 상세가 연결되는 구조를 반영한다.
     */
    @OneToOne(mappedBy = "employee", fetch = FetchType.LAZY)
    private com.staff.domain.employee.reception.entity.ReceptionEntity reception;
}
