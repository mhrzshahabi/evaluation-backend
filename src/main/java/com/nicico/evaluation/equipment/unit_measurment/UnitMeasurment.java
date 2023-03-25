//
//
//package com.nicico.evaluation.equipment.unit_measurment;
//
//import com.nicico.evaluation.common.EvaluationAudit;
//import lombok.*;
//import lombok.experimental.Accessors;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotNull;
//
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Accessors(chain = true)
//@EqualsAndHashCode(of = {"id"}, callSuper = false)
//
//@Entity
//@Table(name = "tbl_unitMeasurments")
//public class UnitMeasurment extends EvaluationAudit {
//
//
//
//
//@Id
//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unitMeasurment_seq")
//@SequenceGenerator(name = "unitMeasurment_seq", sequenceName = "seq_unitMeasurment_id", allocationSize = 1)
//private Long id;
//
//
//    @Column( name="C_title",unique=true)
//    @NotNull(message = "Should not be null")
//private String title;
//
//
//
//    @Column( name="C_abreviation",unique=true)
//    @NotNull(message = "Should not be null")
//    private String abreviation;
//
//
//
//
//
//
//
//
//
//
//
//
//}
