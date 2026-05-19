package com.example.hospitalClinical.common.code;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CodeDetailRepository extends JpaRepository<CodeDetailEntity, CodeDetailId> {

    Page<CodeDetailEntity> findByGroupCodeAndIsActiveTrue(String groupCode, Pageable pageable);

    List<CodeDetailEntity> findByGroupCodeAndIsActiveTrue(String groupCode, Sort sort);

    @Query("""
            select c
              from CodeDetailEntity c
             where c.groupCode = :groupCode
               and c.isActive = true
               and (
                    (:searchByCode = true and lower(c.code) like lower(concat('%', :q, '%')))
                 or (:searchByCode = false and lower(c.codeName) like lower(concat('%', :q, '%')))
               )
            """)
    Page<CodeDetailEntity> searchActive(
            @Param("groupCode") String groupCode,
            @Param("q") String q,
            @Param("searchByCode") boolean searchByCode,
            Pageable pageable
    );

    @Query("""
            select c
              from CodeDetailEntity c
             where c.groupCode = :groupCode
               and c.isActive = true
               and (
                    (:code is not null and :code <> '' and lower(c.code) like lower(concat('%', :code, '%')))
                 or (:name is not null and :name <> '' and lower(c.codeName) like lower(concat('%', :name, '%')))
               )
            """)
    Page<CodeDetailEntity> searchActiveDrug(
            @Param("groupCode") String groupCode,
            @Param("name") String name,
            @Param("code") String code,
            Pageable pageable
    );
}

