package com.nicico.evaluation.iservice;

import com.nicico.evaluation.dto.WorkSpaceDTO;

import java.util.List;

public interface IWorkSpaceService {

    List<WorkSpaceDTO.Info> workSpaceList();
}
