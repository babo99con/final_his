package com.staff.domain.employee.reception.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.staff.domain.employee.basicInfo.entity.StaffEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "EMPLOYEE_RECEPTION", schema = "JCH")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceptionEntity {

    @Id
    @Column(name = "STAFF_ID", nullable = false, length = 30)
    private String staffId;

    @Column(name = "JOB_TYPE_CD", length = 30)
    private String jobTypeCd;

    @Column(name = "DESK_NO", length = 20)
    private String deskNo;

    @Column(name = "SHIFT_TYPE", length = 20)
    private String shiftType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "WINDOW_AREA", length = 50)
    private String windowArea;

    @Column(name = "MULTI_TASK", length = 500)
    private String multiTask;

    @Column(name = "RMK", length = 1000)
    private String rmk;

    @Column(name = "RECEPTION_TYPE", length = 30)
    private String receptionType;

    @Column(name = "EXT_NO", length = 30)
    private String extNo;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STAFF_ID", referencedColumnName = "STAFF_ID", insertable = false, updatable = false)
    private StaffEntity employee;
}
