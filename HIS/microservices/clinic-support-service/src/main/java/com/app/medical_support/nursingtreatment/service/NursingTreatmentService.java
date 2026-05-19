package com.app.medical_support.nursingtreatment.service;

import com.app.medical_support.nursingtreatment.dto.*;

import java.util.List;

public interface NursingTreatmentService {
    List<RecordResponseDTO> search(String searchType, String searchValue, String startDate, String endDate);
    List<RecordResponseDTO> findRecordList();
    RecordResponseDTO findRecordDetail(String nursingId);
    RecordDTO registerRecord(RecordCreateReqDTO recordRequestDTO);
    RecordDTO modifyRecord(String nursingId, RecordUpdateDTO recordDTO);
    RecordDTO updateRecordStatus(String nursingId, String status);

    List<MedicationRecordDTO> findMedicationRecordList();
    List<MedicationRecordDTO> searchMedicationRecord(String patientName, String departmentName, String progressStatus, String startDate, String endDate);
    MedicationRecordDTO findMedicationRecordDetail(String id);
    MedicationRecordDTO registerMedicationRecord(MedicationRecordReqDTO medicationRecordDTO);
    MedicationRecordDTO modifyMedicationRecord(String id, MedicationRecordUpdateDTO medicationRecordDTO);
    MedicationRecordDTO updateMedicationRecordStatus(String id, String status);

    List<TreatmentResultDTO> findTreatmentResultList();
    List<TreatmentResultDTO> searchTreatmentResult(String patientName, String departmentName, String progressStatus, String startDate, String endDate);
    TreatmentResultDTO findTreatmentResultDetail(String id);
    TreatmentResultDTO registerTreatmentResult(TreatmentResultCreateDTO treatmentResultDTO);
    TreatmentResultDTO modifyTreatmentResult(String id, TreatmentResultUpdateDTO treatmentResultDTO);
    TreatmentResultDTO updateTreatmentResultStatus(String id, String status);
}
