package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
//مصداق-

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_instance")
public class Instance extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "instance_seq")
    @SequenceGenerator(name = "instance_seq", sequenceName = "instance_seq_id", allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "c_code")
    private String code;

    @NotNull
    @Column(name = "c_title")
    private String title;

    @OneToMany(mappedBy = "instance", fetch = FetchType.LAZY)
    private List<PostMeritInstance> postMeritInstances;

    @OneToMany(mappedBy = "instance", fetch = FetchType.LAZY)
    private List<EvaluationItemInstance> evaluationItemInstanceList;
}
