package kr.co.hospital.patients.patient.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "patient_info_history")
@Getter
@Setter
public class InfoHistoryEntity {

    @Id
    @SequenceGenerator(
            name = "patient_info_history_seq",
            sequenceName = "PATIENT_INFO_HISTORY_SEQ",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_info_history_seq")
    @Column(name = "history_id")
    private Long historyId;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "change_type", nullable = false, length = 20)
    private String changeType;

    @Lob
    @Column(name = "before_data")
    private String beforeData;

    @Lob
    @Column(name = "after_data")
    private String afterData;

    @Column(name = "changed_by", length = 50)
    private String changedBy;

    @Column(name = "changed_at", insertable = false, updatable = false)
    private LocalDateTime changedAt;
}
