package kr.co.hospital.patients.insurance.service;

import kr.co.hospital.patients.insurance.dto.InsuranceCreateReqDTO;
import kr.co.hospital.patients.insurance.dto.InsuranceResDTO;
import kr.co.hospital.patients.insurance.dto.InsuranceTodayItemResDTO;
import kr.co.hospital.patients.insurance.dto.InsuranceUpdateReqDTO;
import kr.co.hospital.patients.insurancehistory.dto.InsuranceHistoryResDTO;

import java.util.List;

public interface InsuranceService {
    List<InsuranceTodayItemResDTO> findTodayItems();

    List<InsuranceResDTO> findList();

    InsuranceResDTO findDetail(Long id);

    InsuranceResDTO register(InsuranceCreateReqDTO insuranceCreateReqDTO);

    InsuranceResDTO modify(Long id, InsuranceUpdateReqDTO insuranceUpdateReqDTO);

    void remove(Long id);

    List<InsuranceResDTO> search(String type, String keyword);

    InsuranceResDTO findValidByPatientId(Long patientId);

    List<InsuranceHistoryResDTO> findHistoryByPatientId(Long patientId);
}
