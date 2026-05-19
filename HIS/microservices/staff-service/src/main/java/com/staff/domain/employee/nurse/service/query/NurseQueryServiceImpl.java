package com.staff.domain.employee.nurse.service.query;

import com.staff.common.exception.EntityNotFoundException;
import com.staff.domain.employee.nurse.dto.NurseResponseDTO;
import com.staff.domain.employee.nurse.mapper.NurseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NurseQueryServiceImpl implements NurseQueryService {
    private final NurseMapper nurseMapper;

    @Override public List<NurseResponseDTO> listNurses()
    { return nurseMapper.selectNurseList(); }

    //검색조회
    @Override
    public List<NurseResponseDTO> searchNurses(String search, String searchType) {
        return nurseMapper.searchNurseList(search, searchType);
    }


    @Override public NurseResponseDTO getNurseDetail(String staffId)
    { NurseResponseDTO dto = nurseMapper.selectNurseById(staffId);
        if (dto == null) throw new EntityNotFoundException("간호사 프로필이 없습니다. staffId=" + staffId); return dto; }
}
