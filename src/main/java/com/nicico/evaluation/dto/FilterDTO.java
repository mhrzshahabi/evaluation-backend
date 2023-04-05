package com.nicico.evaluation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nicico.copper.common.dto.search.EOperator;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

public class FilterDTO {

    public FilterDTO() {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CriteriaRq {
        private String field;
        private String type;
        private EOperator operator;
        private Object values;
        private Object fromValues;
        private Object toValues;
        private Object start;
        private Object end;
        private List<FilterDTO.CriteriaRq> criteria;

        public CriteriaRq() {
        }

        public List<Object> getValues() {
            if (this.values != null) {
                if (this.values instanceof Collection) {
                    return (List) this.values;
                } else {
                    return this.values instanceof Map && ((Map) this.values).size() == 0 ? null : Collections.singletonList(this.values);
                }
            } else {
                List<Object> tempValue = new ArrayList();
                tempValue.add(this.start);
                tempValue.add(this.end);
                return tempValue;
            }
        }

        public String getField() {
            return this.field;
        }

        public String getType() {
            return this.type;
        }

        public EOperator getOperator() {
            return this.operator;
        }

        public Object getStart() {
            return this.start;
        }

        public Object getEnd() {
            return this.end;
        }

        public List<FilterDTO.CriteriaRq> getCriteria() {
            return this.criteria;
        }

        public FilterDTO.CriteriaRq setField(final String field) {
            this.field = field;
            return this;
        }

        public FilterDTO.CriteriaRq setType(final String type) {
            this.type = type;
            return this;
        }

        public FilterDTO.CriteriaRq setOperator(final EOperator operator) {
            this.operator = operator;
            return this;
        }

        public FilterDTO.CriteriaRq setValues(final Object values) {
            this.values = values;
            return this;
        }

        public FilterDTO.CriteriaRq setStart(final Object start) {
            this.start = start;
            return this;
        }

        public FilterDTO.CriteriaRq setEnd(final Object end) {
            this.end = end;
            return this;
        }

        public FilterDTO.CriteriaRq setCriteria(final List<FilterDTO.CriteriaRq> criteria) {
            this.criteria = criteria;
            return this;
        }

        public String toString() {
            String var10000 = this.getField();
            return "FilterDTO.CriteriaRq(field=" + var10000 + ", type=" + this.getType() + ", operator=" + this.getOperator() + ", values=" + this.getValues() + ", start=" + this.getStart() + ", end=" + this.getEnd() + ", criteria=" + this.getCriteria() + ")";
        }
    }

    public static class SearchRs<T> {
        private List<T> list;
        private Long totalCount;

        public SearchRs() {
        }

        public List<T> getList() {
            return this.list;
        }

        public Long getTotalCount() {
            return this.totalCount;
        }

        public void setList(final List<T> list) {
            this.list = list;
        }

        public void setTotalCount(final Long totalCount) {
            this.totalCount = totalCount;
        }
    }

    public static class SortByRq {
        @NotEmpty
        @ApiModelProperty(
                required = true
        )
        private String field;
        private Boolean descending;

        @ApiModelProperty(
                hidden = true
        )
        public boolean getDescendingSafe() {
            return this.getDescending() != null ? this.getDescending() : false;
        }

        public String getField() {
            return this.field;
        }

        public Boolean getDescending() {
            return this.descending;
        }

        public FilterDTO.SortByRq setField(final String field) {
            this.field = field;
            return this;
        }

        public FilterDTO.SortByRq setDescending(final Boolean descending) {
            this.descending = descending;
            return this;
        }

        public String toString() {
            String var10000 = this.getField();
            return "FilterDTO.SortByRq(field=" + var10000 + ", descending=" + this.getDescending() + ")";
        }

        public SortByRq() {
        }

        public SortByRq(final String fieldName, final Boolean descending) {
            this.field = fieldName;
            this.descending = descending;
        }
    }

    public static class SearchRq {
        public static final String FIELD_START_INDEX = "startIndex";
        public static final String FIELD_COUNT = "count";
        @NotNull
        @Min(0L)
        @ApiModelProperty(required = true)
        private Integer startIndex;
        @NotNull
        @Min(1L)
        @ApiModelProperty(required = true)
        private Integer count;
        private FilterDTO.CriteriaRq criteria;
        private Object sortBy;
        private Boolean distinct = false;

        public SearchRq() {
        }

        public List<FilterDTO.SortByRq> getSortBy() {
            if (this.sortBy != null) {
                List<FilterDTO.SortByRq> sortByRqList = new ArrayList();
                if (this.sortBy instanceof Collection) {
                    List<String> fieldNames = (List) this.sortBy;
                    fieldNames.forEach((fieldName) -> {
                        sortByRqList.add(this.getSortByRq(fieldName));
                    });
                } else {
                    String fieldName = (String) this.sortBy;
                    sortByRqList.add(this.getSortByRq(fieldName));
                }

                return sortByRqList;
            } else {
                return null;
            }
        }

        private FilterDTO.SortByRq getSortByRq(String field) {
            FilterDTO.SortByRq sortByRq = new FilterDTO.SortByRq();
            if (field.startsWith("-")) {
                sortByRq.setField(field.substring(1)).setDescending(true);
            } else {
                sortByRq.setField(field);
            }

            return sortByRq;
        }

        public Integer getStartIndex() {
            return this.startIndex;
        }

        public Integer getCount() {
            return this.count;
        }

        public FilterDTO.CriteriaRq getCriteria() {
            return this.criteria;
        }

        public Boolean getDistinct() {
            return this.distinct;
        }

        public FilterDTO.SearchRq setStartIndex(final Integer startIndex) {
            this.startIndex = startIndex;
            return this;
        }

        public FilterDTO.SearchRq setCount(final Integer count) {
            this.count = count;
            return this;
        }

        public FilterDTO.SearchRq setCriteria(final FilterDTO.CriteriaRq criteria) {
            this.criteria = criteria;
            return this;
        }

        public FilterDTO.SearchRq setSortBy(final Object sortBy) {
            this.sortBy = sortBy;
            return this;
        }

        public FilterDTO.SearchRq setDistinct(final Boolean distinct) {
            this.distinct = distinct;
            return this;
        }

        public String toString() {
            Integer var10000 = this.getStartIndex();
            return "FilterDTO.SearchRq(startIndex=" + var10000 + ", count=" + this.getCount() + ", criteria=" + this.getCriteria() + ", sortBy=" + this.getSortBy() + ", distinct=" + this.getDistinct() + ")";
        }
    }
}

