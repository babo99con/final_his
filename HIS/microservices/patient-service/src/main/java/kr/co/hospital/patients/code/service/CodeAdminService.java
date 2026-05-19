package kr.co.hospital.patients.code.service;

import kr.co.hospital.patients.code.dto.CodeDetailReq;
import kr.co.hospital.patients.code.dto.CodeDetailRes;
import kr.co.hospital.patients.code.dto.CodeGroupReq;
import kr.co.hospital.patients.code.dto.CodeGroupRes;

import java.util.List;

public interface CodeAdminService {

    List<CodeGroupRes> findGroups(boolean activeOnly);

    CodeGroupRes createGroup(CodeGroupReq req);

    CodeGroupRes updateGroup(String groupCode, CodeGroupReq req);

    void deactivateGroup(String groupCode);
    void activateGroup(String groupCode);

    List<CodeDetailRes> findDetails(String groupCode, boolean activeOnly);

    CodeDetailRes createDetail(CodeDetailReq req);

    CodeDetailRes updateDetail(String groupCode, String code, CodeDetailReq req);

    void deactivateDetail(String groupCode, String code);
    void activateDetail(String groupCode, String code);
}
