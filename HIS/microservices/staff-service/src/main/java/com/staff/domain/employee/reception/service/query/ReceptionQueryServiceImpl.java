package com.staff.domain.employee.reception.service.query;

import com.staff.common.exception.EntityNotFoundException;
import com.staff.domain.employee.reception.dto.ReceptionResponseDTO;

import com.staff.domain.employee.reception.mapper.ReceptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ReceptionQueryServiceImpl implements ReceptionQueryService {

    private final ReceptionMapper receptionMapper;

    @Override
    public List<ReceptionResponseDTO> listReceptions() {
        return receptionMapper.selectReceptionList();
    }

    @Override
    public List<ReceptionResponseDTO> searchReceptions(String search, String searchType) {
        return receptionMapper.searchReceptionList(search, searchType);
    }

    @Override
    public ReceptionResponseDTO getReceptionDetail(String staffId) {
        ReceptionResponseDTO dto = receptionMapper.selectReceptionById(staffId);
        if (dto == null) {
            throw new EntityNotFoundException("원무 프로필이 없습니다. staffId=" + staffId);
        }
        return dto;
    }
}
