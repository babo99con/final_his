package kr.co.hospital.patients.patient.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_family")
@Getter
@Setter
public class FamilyEntity {

    @Id
    @SequenceGenerator(name = "patient_family_seq", sequenceName = "PATIENT_FAMILY_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_family_seq")
    @Column(name = "family_id")
    private Long familyId;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "relation", nullable = false, length = 30)
    private String relation;

    @Column(name = "family_name", nullable = false, length = 50)
    private String familyName;

    @Column(name = "family_phone", length = 20)
    private String familyPhone;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
