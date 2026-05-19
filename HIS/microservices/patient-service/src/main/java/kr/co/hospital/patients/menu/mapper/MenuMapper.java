package kr.co.hospital.patients.menu.mapper;

import kr.co.hospital.patients.menu.dto.MenuFlatDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {

    List<MenuFlatDto> findHierarchy();
}
