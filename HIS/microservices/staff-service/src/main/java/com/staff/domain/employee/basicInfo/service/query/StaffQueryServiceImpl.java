package com.staff.domain.employee.basicInfo.service.query;

import com.staff.domain.employee.basicInfo.dto.StaffResponseDTO;
import com.staff.domain.employee.basicInfo.mapper.StaffMapper;
import com.staff.domain.employee.basicInfo.validator.StaffCommonValidator;
import com.staff.domain.employee.doctor.dto.DoctorResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*** 조회는 MyBatis Mapper를 사용.*/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StaffQueryServiceImpl implements StaffQueryService {

    private final StaffMapper staffMapper;
    private final StaffCommonValidator staffCommonValidator;


    //디테일
    @Override
    public StaffResponseDTO detailStaff(String staffId) {
        String normalizedStaffId = staffCommonValidator.requireText(staffId, "조회할 STAFF_ID가 없습니다.");

        return staffMapper.selectStaffById(normalizedStaffId);
    }

    //리스트
    @Override
    public List<StaffResponseDTO> listStaff() {
        return staffMapper.selectStaffList();
    }


    //검색조회
    @Override
    public List<StaffResponseDTO> searchStaff(String search, String searchType) {
        return staffMapper.searchStaffList(search, searchType);

    }
    }
