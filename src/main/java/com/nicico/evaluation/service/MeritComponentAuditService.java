package com.nicico.evaluation.service;

import com.nicico.evaluation.exception.EvaluationHandleException;
import com.nicico.evaluation.iservice.ICatalogService;
import com.nicico.evaluation.iservice.IMeritComponentAuditService;
import com.nicico.evaluation.model.MeritComponentAudit;
import com.nicico.evaluation.repository.MeritComponentAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MeritComponentAuditService implements IMeritComponentAuditService {

    private final ICatalogService catalogService;
    private final MeritComponentAuditRepository repository;

    @Override
    public MeritComponentAudit findLastActiveByMeritComponentId(Long meritComponentId) {
        return repository.findLastActiveByMeritComponentId(meritComponentId, catalogService.getByCode("Active-Merit").getId()).orElseThrow(() -> new EvaluationHandleException(EvaluationHandleException.ErrorType.NotFound));
    }
}
