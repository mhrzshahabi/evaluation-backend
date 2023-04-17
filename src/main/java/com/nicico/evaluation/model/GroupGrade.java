package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table(name = "tbl_group_grade")
public class GroupGrade extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_grade_seq")
    @SequenceGenerator(name = "group_grade_seq", sequenceName = "seq_group_grade_id", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "grade_id")
    private Long gradeId;
    @NonNull
    @Column(name = "c_grade_code")
    private String gradeCode;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_group_grade_to_group"))
    private Group group;

    @NonNull
    @Column(name = "group_id")
    private Long groupId;

    @NonNull
    @Column(name = "c_grade_title")
    private String gradeTitle;

}
