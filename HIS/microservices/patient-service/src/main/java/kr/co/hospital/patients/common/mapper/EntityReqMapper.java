package kr.co.hospital.patients.common.mapper;

import java.util.List;

public interface EntityReqMapper<E,D> {

    E toEntity(D dto);

    List<E> toEntityList(List<D> dtos);
}
