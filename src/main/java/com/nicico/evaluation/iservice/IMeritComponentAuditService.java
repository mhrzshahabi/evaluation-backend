package com.nicico.evaluation.iservice;

import com.nicico.evaluation.model.MeritComponentAudit;

public interface IMeritComponentAuditService {

    MeritComponentAudit findLastActiveByMeritComponentId(Long meritComponentId);
}
