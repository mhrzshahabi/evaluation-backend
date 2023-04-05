package com.nicico.evaluation.model;

import com.nicico.evaluation.common.EvaluationAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tbl_group")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Group extends EvaluationAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_seq")
    @SequenceGenerator(name = "group_seq", sequenceName = "group_seq_id", allocationSize = 1)
    private Integer id;

    @Column(name = "C_CODE")
    private String code;


    @Column(name = "C_TITLE")
    private String title;

    @Column(name = "B_MERIT_DEFINITION_ALLOWED")
    private Boolean definitionAllowed;

}
