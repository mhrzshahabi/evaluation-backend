package com.nicico.evaluation.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "tbl_group")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "create_seq_tbl_group")
    @SequenceGenerator(name = "create_seq_tbl_group", sequenceName = "group_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "C_CODE")
    private String code;


    @Column(name = "C_TITLE")
    private String title;

    @Column(name = "B_MERIT_DEFINITION_ALLOWED")
    private Boolean definitionAllowed;

}
