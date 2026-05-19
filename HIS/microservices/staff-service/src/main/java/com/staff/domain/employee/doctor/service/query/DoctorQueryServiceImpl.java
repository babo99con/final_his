package com.staff.domain.employee.doctor.service.query;

import com.staff.common.exception.EntityNotFoundException;
import com.staff.domain.employee.doctor.dto.DoctorResponseDTO;
import com.staff.domain.employee.doctor.mapper.DoctorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DoctorQueryServiceImpl implements DoctorQueryService {
    private final DoctorMapper doctorMapper;

    //전체조회
    @Override public List<DoctorResponseDTO> listDoctors()
    {
        return doctorMapper.selectDoctorList(); }


    //검색조회
    @Override
    public List<DoctorResponseDTO> searchDoctors(String search, String searchType) {
        return doctorMapper.searchDoctorList(search, searchType);

    }


    //단건조회
    @Override public DoctorResponseDTO getDoctorDetail(String staffId)
    {
        DoctorResponseDTO dto = doctorMapper.selectDoctorById(staffId);
        if (dto == null) throw new EntityNotFoundException("의사 프로필이 없습니다. staffId=" + staffId); return dto; }
}
