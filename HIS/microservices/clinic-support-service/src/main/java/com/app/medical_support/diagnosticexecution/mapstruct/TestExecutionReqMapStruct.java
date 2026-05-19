package com.app.medical_support.diagnosticexecution.mapstruct;

import com.app.medical_support.common.mapstruct.ReqMapStruct;
import com.app.medical_support.diagnosticexecution.dto.TestExecutionReqDTO;
import com.app.medical_support.diagnosticexecution.entity.TestExecutionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TestExecutionReqMapStruct extends ReqMapStruct<TestExecutionEntity, TestExecutionReqDTO> {

    TestExecutionEntity toEntity(TestExecutionReqDTO dto);
}
