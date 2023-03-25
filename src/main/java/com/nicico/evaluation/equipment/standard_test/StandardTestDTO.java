


package com.nicico.evaluation.equipment.standard_test;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class StandardTestDTO {

    @ApiModelProperty(required = false, hidden = true)
    private Long id;

            @ApiModelProperty(required = true)
            private String title;


        @ApiModelProperty(required = false)
        private String description;

   @ApiModelProperty(required = false, hidden = true)
        private Long deleteDate;

    @ApiModelProperty(required = false, hidden = true)
    private Boolean updatable=true;


    @ApiModelProperty(required = false, hidden = true)
    private Boolean deleted=false;


    @ApiModelProperty(required = false, hidden = true)
    private Date createdDate;

    @ApiModelProperty(required = false, hidden = true)
    private String createdBy;

    @ApiModelProperty(required = false, hidden = true)
    private Date lastModifiedDate;

    @ApiModelProperty(required = false, hidden = true)
    private String lastModifiedBy;

    @ApiModelProperty(required = false, hidden = true)
    private Integer version;




}
