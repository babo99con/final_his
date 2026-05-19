package kr.co.hospital.patients.code.service;

import kr.co.hospital.patients.code.dto.CodeRes;

import java.util.List;

public interface CodeService {

    List<CodeRes> findByGroup(String groupCode);
}
