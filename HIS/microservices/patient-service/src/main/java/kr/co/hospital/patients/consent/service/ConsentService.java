package kr.co.hospital.patients.consent.service;


import kr.co.hospital.patients.consent.dto.ConsentCreateReqDTO;
import kr.co.hospital.patients.consent.dto.ConsentLatestResDTO;
import kr.co.hospital.patients.consent.dto.ConsentResDTO;
import kr.co.hospital.patients.consent.dto.ConsentTodayItemResDTO;
import kr.co.hospital.patients.consent.dto.ConsentUpdateReqDTO;
import kr.co.hospital.patients.consent.dto.ConsentWithdrawHistoryResDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ConsentService {

    List<ConsentTodayItemResDTO> findTodayItems();

    List<ConsentResDTO> findList(Long patientId);

    ConsentResDTO findDetail(Long patientId, Long consentId);

    ConsentResDTO register(Long patientId, ConsentCreateReqDTO createReqDTO, MultipartFile file);

    ConsentResDTO modify(Long patientId, Long consentId, ConsentUpdateReqDTO updateReqDTO, MultipartFile file);

    void remove(Long patientId, Long consentId);

    List<ConsentResDTO> search(Long patientId, String type, String keyword);

    List<ConsentLatestResDTO> findLatestByPatient(Long patientId);

    List<ConsentWithdrawHistoryResDTO> findWithdrawHistory(Long patientId);
}
