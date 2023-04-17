package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_group")
public class Group extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_seq")
    @SequenceGenerator(name = "group_seq", sequenceName = "group_seq_id", allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "c_code")
    private String code;

    @NotNull
    @Column(name = "c_title")
    private String title;

    @Column(name = "b_merit_definition_allowed")
    private Boolean definitionAllowed;

}
