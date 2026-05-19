package com.staff.common.mapper;

import java.util.List;

public interface EntityReqMapper<E,D> {
    E toEntity(D dto);

    List<E> toEntity(List<D> dto);

//
//    // ✅ 수정용 추가 (덮어씌우기 ) 엔터티-> dto (상세)auth만 지금 해놓은상태
//    void updateEntity(D dto, @MappingTarget E entity);

}
