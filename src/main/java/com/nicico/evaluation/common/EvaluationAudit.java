package com.nicico.evaluation.common;


import com.nicico.copper.common.domain.Auditable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class EvaluationAudit extends Auditable {

    @Column(name = "is_deleted", columnDefinition = "NUMBER(1) default 0 not null")
    private Boolean deleted = false;

    @Column(name = "deleted_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedDate;
    @Column(name = "c_updatable", columnDefinition = "NUMBER(1) default 1 not null")
    private Boolean updatable = true;

    @Column(name = "c_comment", length = 4000)
    private String comment;

    @Column(name = "c_description", length = 4000)
    private String description;


}
