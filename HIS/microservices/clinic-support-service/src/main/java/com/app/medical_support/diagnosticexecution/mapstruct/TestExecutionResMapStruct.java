package com.app.medical_support.diagnosticexecution.mapstruct;

import com.app.medical_support.common.mapstruct.ResMapStruct;
import com.app.medical_support.diagnosticexecution.dto.TestExecutionDTO;
import com.app.medical_support.diagnosticexecution.entity.TestExecutionEntity;
import com.app.medical_support.nursingtreatment.dto.RecordDTO;
import com.app.medical_support.nursingtreatment.entity.RecordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TestExecutionResMapStruct extends ResMapStruct<TestExecutionDTO, TestExecutionEntity> {
    TestExecutionDTO toDTO(TestExecutionEntity entity);
    List<TestExecutionDTO> toDTOList(List<TestExecutionEntity> entities) ;

}
