package com.staff.domain.employee.nurse.entity;

import jakarta.persistence.*;
import com.staff.domain.employee.basicInfo.entity.StaffEntity;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "EMPLOYEE_NURSE", schema = "JCH")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NurseEntity {
    @Id
    @Column(name = "STAFF_ID", nullable = false, length = 30)
    private String staffId;

    @Column(name = "LICENSE_NO", nullable = false, length = 50)
    private String licenseNo;

    @Column(name = "NURSE_TYPE", nullable = false, length = 30)
    private String nurseType;

    @Column(name = "SHIFT_TYPE", length = 30)
    private String shiftType;


    @Column(name = "NURSE_FILE_URL", length = 1000)
    private String nurseFileUrl;

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
//
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "UPDATED_AT", nullable = false)
//    private Date updatedAt;

//    @PrePersist
//    protected void onCreate() {
//        Date now = new Date();
//        if (createdAt == null) createdAt = now;
//        updatedAt = now;
//    }
//
//    @PreUpdate
//    protected void onUpdate() { updatedAt = new Date(); }
//
//
//    /**
//     * 공통 직원 허브 참조.
//     * STAFF_ID를 PK이자 FK로 사용하는 1:1 식별 관계를 JPA 연관관계로 표현한다.
//     * 실제 쓰기 컬럼은 staffId 필드가 담당하므로 연관 필드는 조회/탐색용으로 둔다.
//     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STAFF_ID", referencedColumnName = "STAFF_ID", insertable = false, updatable = false)
    private StaffEntity employee;

}
