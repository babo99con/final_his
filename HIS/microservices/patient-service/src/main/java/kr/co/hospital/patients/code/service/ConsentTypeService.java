package kr.co.hospital.patients.code.service;

import kr.co.hospital.patients.code.dto.ConsentTypeReq;
import kr.co.hospital.patients.code.dto.ConsentTypeRes;

import java.util.List;

public interface ConsentTypeService {
    List<ConsentTypeRes> findActive();
    List<ConsentTypeRes> findAll();
    ConsentTypeRes create(ConsentTypeReq req);
    ConsentTypeRes update(String code, ConsentTypeReq req);
    void deactivate(String code);
}
