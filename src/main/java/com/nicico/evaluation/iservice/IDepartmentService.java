package com.nicico.evaluation.iservice;

import com.nicico.evaluation.dto.DepartmentDTO;

public interface IDepartmentService {

    DepartmentDTO.SpecResponse mojtameList(int startIndex, int count);

    DepartmentDTO.SpecResponse moavenatList(int startIndex, int count);

    DepartmentDTO.SpecResponse omorList(int startIndex, int count);

    DepartmentDTO.SpecResponse ghesmatList(int startIndex, int count);
}
