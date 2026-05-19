package kr.co.hospital.patients.menu.service;

import kr.co.hospital.patients.menu.dto.MenuTreeRes;

import java.util.List;

public interface MenuService {

    List<MenuTreeRes> getMenus();
}
