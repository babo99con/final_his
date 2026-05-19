package kr.co.hospital.patients.code.repository;

import kr.co.hospital.patients.code.entity.CodeGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodeGroupRepository extends JpaRepository<CodeGroupEntity, String> {

    List<CodeGroupEntity> findAllByIsActiveTrueOrderByGroupCodeAsc();

    List<CodeGroupEntity> findAllByOrderByGroupCodeAsc();

    long countByGroupCodeAndIsActiveTrue(String groupCode);
}
