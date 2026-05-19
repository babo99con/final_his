package com.app.medical_support.common.stafflookup.service;

import com.app.medical_support.common.stafflookup.dto.StaffOptionDTO;

import java.util.List;

public interface StaffLookupService {
    List<StaffOptionDTO> findStaffOptions(String role, String examType, String keyword, Integer limit);
}
