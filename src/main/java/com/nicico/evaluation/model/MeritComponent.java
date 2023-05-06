package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

//شایستگی
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table(name = "tbl_merit_component", uniqueConstraints = {@UniqueConstraint(columnNames = {"c_title", "c_code"})})
public class MeritComponent extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_merit_component_id")
    @SequenceGenerator(name = "seq_merit_component_id", sequenceName = "seq_merit_component_id", allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "c_title", nullable = false, unique = true)
    private String title;

    @NotNull
    @Column(name = "c_code", nullable = false, unique = true)
    private String code;

    @OneToMany(mappedBy = "meritComponent", fetch = FetchType.LAZY)
    private List<MeritComponentType> meritComponentTypes;

}
