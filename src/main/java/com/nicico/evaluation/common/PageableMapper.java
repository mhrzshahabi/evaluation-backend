package com.nicico.evaluation.common;

import org.mapstruct.Mapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Mapper(componentModel = "spring")
public interface PageableMapper {

    default Pageable toPageable(int count, int startIndex) {
        int page = 0;
        int size = count;
        if (count != 0)
            page = startIndex / count;
        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
    }
}

