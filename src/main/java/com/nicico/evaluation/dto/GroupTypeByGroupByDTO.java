package com.nicico.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public abstract class GroupTypeByGroupByDTO {

    @Getter
    @Setter
    @ApiModel("GroupTypeInfoByGroupBy")
    public static class Info {
        private String groupName;
        private Long totalWeight;
        private List<GroupTypeDTO.Info> detailInfos;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupTypeSpecResponseByGroupBy")
    public static class SpecResponse {
        private Response response;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ApiModel("GroupTypeResponseByGroupBy")
    public static class Response {
        private List<Info> data;
        private Integer status;
        private Integer startRow;
        private Integer endRow;
        private Integer totalRows;
    }


    @Accessors(chain = true)
    @ApiModel("ResponseByGroupBy")
    public interface Resp {
        String getTitle();

        Long getGroupId();

        Long getTotalWeight();

        Long getHasAllKpiType();
    }

}
