
package com.nicico.evaluation.model;

import com.nicico.copper.common.domain.Auditable;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tbl_special_case")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpecialCase extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "special_case_seq")
    @SequenceGenerator(name = "special_case_seq", sequenceName = "seq_special_case_id", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "c_assess_full_name")
    private String assessFullName;

    @Column(name = "c_assess_national_code")
    private String assessNationalCode;

    @Column(name = "c_assess_post_code")
    private String assessPostCode;

    @Column(name = "c_assess_real_post_code")
    private String assessRealPostCode;

    @Column(name = "c_assessor_full_name")
    private String assessorFullName;

    @Column(name = "c_assessor_national_code")
    private String assessorNationalCode;

    @Column(name = "c_assessor_post_code")
    private String assessorPostCode;

    @Column(name = "c_start_date")
    private String startDate;

    @Column(name = "c_end_date")
    private String endDate;

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_catalog_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "f_special_case_status_to_catalog"))
    private Catalog statusCatalog;

    @NotNull
    @Column(name = "status_catalog_id")
    private Long statusCatalogId;

    @Column(name = "c_evaluation_group_post_code")
    private String evaluationGroupPostCode;

}
    