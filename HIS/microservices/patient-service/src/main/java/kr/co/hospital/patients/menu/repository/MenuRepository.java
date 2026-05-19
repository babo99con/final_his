package kr.co.hospital.patients.menu.repository;

import kr.co.hospital.patients.menu.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<MenuEntity, Long> {

    List<MenuEntity> findAllByIsActiveTrue();
}
