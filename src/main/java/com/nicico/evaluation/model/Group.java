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

    @Column(name = "c_code")
    private String code;


    @Column(name = "c_title")
    private String title;

    @Column(name = "b_merit_definition_allowed")
    private Boolean definitionAllowed;

}
