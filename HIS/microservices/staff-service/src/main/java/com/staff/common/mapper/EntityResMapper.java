package com.staff.common.mapper;

import java.util.List;

public interface EntityResMapper<E,D> {
    D toDto(E entity);

    List<D> toDto(List<E> entities);


}
