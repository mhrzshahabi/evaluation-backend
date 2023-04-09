package com.nicico.evaluation.common;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PageableMapper {

    default PageDTO toPageDto(Page page, List data) {
        if (page == null)
            return null;
        PageDTO pageDTO = new PageDTO();
//        pageDTO.setCurrentPage(0);
        pageDTO.setPageSize(page.getSize());
        pageDTO.setTotalPage(page.getTotalPages());
        pageDTO.setTotalItems(page.getTotalElements());
        pageDTO.setList(data);
        return pageDTO;
    }

    default Pageable toPageable(int count, int startIndex) {
        int page = 0;
        int size = count;
        if (count != 0)
            page = startIndex / count;
        return PageRequest.of(page, size);
    }
}

