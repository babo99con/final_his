package kr.co.hospital.patients.patient.service;

import kr.co.hospital.patients.patient.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PatientService {

    //환자기본정보
    List<PatientResDTO> findList();

    PatientResDTO findDetail(Long id);

    PatientResDTO register(CreateReqDTO createreqDTO, MultipartFile file);

    PatientResDTO modify(Long id, UpdateReqDTO updateReqDTO);

    void remove(Long id);

    PatientResDTO changeStatus(Long id, StatusChangeReqDTO statusChangeReqDTO);

    PatientResDTO changeVip(Long id, Boolean isVip);

    List<PatientResDTO> search(String type, String keyword);

    List<PatientResDTO> searchMulti(String name, String birthDate, String phone);

    List<PatientIdentifyResDTO> identify(PatientIdentifyReqDTO reqDTO);

    //환자가족정보
    List<FamilyResDTO> findByPatientId(Long patientId);

    List<FamilyResDTO> createForPatient(Long patientId, List<FamilyCreateReqDTO> families);

    void deleteByPatientId(Long patientId);

    //환자플래그
    List<FlagResDTO> findFlagList();

    FlagResDTO findFlagDetail(Long id);

    FlagResDTO registerFlag(FlagCreateReqDTO createReqDTO);

    FlagResDTO modifyFlag(Long id, FlagUpdateReqDTO updateReqDTO);

    void removeFlag(Long id);

    List<FlagResDTO> searchFlags(String type, String keyword);


    //환자 메모
    List<MemoResDTO> findMemoList();

    MemoResDTO findMemoDetail(Long id);

    MemoResDTO registerMemo(MemoCreateReqDTO createReqDTO);

    MemoResDTO modifyMemo(Long id, MemoUpdateReqDTO updateReqDTO);

    void removeMemo(Long id);

    List<MemoResDTO> searchMemos(String type, String keyword);


    //환자 제한
    List<RestrictionResDTO> findRestrictionList();

    RestrictionResDTO findRestrictionDetail(Long id);

    RestrictionResDTO registerRestriction(RestrictionCreateReqDTO createReqDTO);

    RestrictionResDTO modifyRestriction(Long id, RestrictionUpdateReqDTO updateReqDTO);

    void removeRestriction(Long id);

    List<RestrictionResDTO> searchRestrictions(String type, String keyword);

    //환자상태이력
    List<StatusHistoryResDTO> findStatusHistoryList();

    StatusHistoryResDTO findStatusHistoryDetail(Long id);

    StatusHistoryResDTO registerStatusHistory(StatusHistoryCreateReqDTO createReqDTO);

    StatusHistoryResDTO modifyStatusHistory(Long id, StatusHistoryUpdateReqDTO updateReqDTO);

    void removeStatusHistory(Long id);

    List<StatusHistoryResDTO> searchStatusHistories(String type, String keyword);

    //환자정보이력
    List<InfoHistoryResDTO> findInfoHistoryByPatientId(Long patientId);
}
