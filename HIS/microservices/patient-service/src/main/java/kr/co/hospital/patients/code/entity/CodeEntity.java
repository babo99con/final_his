package kr.co.hospital.patients.code.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "code_detail")
@IdClass(CodeId.class)
@Getter
@Setter
public class CodeEntity {

    @Id
    @Column(name = "group_code", nullable = false, length = 50)
    private String groupCode;

    @Id
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "code_name", nullable = false, length = 100)
    private String name;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "note", length = 400)
    private String note;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}