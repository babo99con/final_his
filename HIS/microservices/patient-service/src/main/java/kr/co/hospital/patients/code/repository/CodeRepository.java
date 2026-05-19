package kr.co.hospital.patients.code.repository;

import kr.co.hospital.patients.code.entity.CodeEntity;
import kr.co.hospital.patients.code.entity.CodeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CodeRepository extends JpaRepository<CodeEntity, CodeId> {

    List<CodeEntity> findAllByGroupCodeAndIsActiveTrueOrderBySortOrderAscCodeAsc(String groupCode);

    List<CodeEntity> findAllByGroupCodeOrderBySortOrderAscCodeAsc(String groupCode);

    List<CodeEntity> findAllByIsActiveTrueOrderByGroupCodeAscSortOrderAscCodeAsc();

    List<CodeEntity> findAllByOrderByGroupCodeAscSortOrderAscCodeAsc();

    Optional<CodeEntity> findByGroupCodeAndCode(String groupCode, String code);

    boolean existsByGroupCodeAndCodeAndIsActiveTrue(String groupCode, String code);

    long countByGroupCodeAndCodeAndIsActiveTrue(String groupCode, String code);

    @Modifying
    @Query("update CodeEntity c set c.isActive = false where c.groupCode = :groupCode")
    int deactivateAllByGroupCode(@Param("groupCode") String groupCode);

    @Modifying
    @Query("update CodeEntity c set c.isActive = true where c.groupCode = :groupCode")
    int activateAllByGroupCode(@Param("groupCode") String groupCode);
}

