package com.nicico.evaluation.utility;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.common.util.date.DateUtil;
import com.nicico.evaluation.dto.FilterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CriteriaUtil {

    public static SearchDTO.SearchRq ConvertCriteriaToSearchRequest(List<FilterDTO> list, Integer count, Integer startIndex) {
        SearchDTO.SearchRq searchRq = new SearchDTO.SearchRq();
        Set<SearchDTO.CriteriaRq> criteriaRqList = new HashSet<>();
        Integer startRow = (startIndex != null) ? Integer.parseInt(startIndex.toString()) : 0;
        searchRq.setStartIndex(startRow);
        searchRq.setCount(count != null ? count : 30);
        searchRq.setSortBy("id");
        searchRq.setDistinct(true);

        if (list != null && !list.isEmpty()) {
            list.forEach(criteria -> {
                SearchDTO.CriteriaRq criteriaRq;
                EOperator operator;
                if ("select".equals(criteria.getType())) {
                    operator = EOperator.equals;
                } else if ("date".equals(criteria.getType())) {
                    operator = EOperator.equals;
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
                    Date date = new Date(Long.parseLong(criteria.getValues().get(0).toString()));
                    String dateformat = dateFormat.format(date);
                    criteria.setValues(criteria.getValues().stream().map(item -> DateUtil.convertMiToKh(dateformat)).collect(Collectors.toList()));
                } else {
                    operator = EOperator.contains;
                }
                criteriaRq = makeCriteria(criteria.getField(), (criteria.getValues() != null && !criteria.getValues().isEmpty()) ? criteria.getValues().get(0) : null, operator, new ArrayList<>());

                if ("NotEqual".equals(criteria.getOperator())) {
                    SearchDTO.CriteriaRq inNullRq = makeCriteria(criteria.getField(), null, EOperator.isNull, new ArrayList<>());
                    SearchDTO.CriteriaRq notNullRq = makeCriteria(criteria.getField(), (criteria.getValues() != null && !criteria.getValues().isEmpty()) ? criteria.getValues().get(0) : null, EOperator.notEqual, new ArrayList<>());
                    final List<SearchDTO.CriteriaRq> criteriaList = new ArrayList<>();
                    criteriaList.add(inNullRq);
                    criteriaList.add(notNullRq);
                    criteriaRq = new SearchDTO.CriteriaRq().setOperator(EOperator.or).setCriteria(criteriaList);
                }

                //todo
                if ((Objects.nonNull(criteriaRq.getFieldName()) && !criteriaRq.getFieldName().equals("hasEvaluation")) || Objects.isNull(criteriaRq.getFieldName()))
                    criteriaRqList.add(criteriaRq);
            });
        }
        try {
            SearchDTO.CriteriaRq criteria = makeCriteria(null, null, EOperator.and, criteriaRqList.stream().toList());
            if (searchRq.getCriteria() != null) {
                criteria.getCriteria().add(searchRq.getCriteria());
            }
            searchRq.setCriteria(criteria);
        } catch (Exception ex) {

        }

        return searchRq;
    }

    public static SearchDTO.CriteriaRq makeCriteria(String fieldName, Object value, EOperator operator, List<SearchDTO.CriteriaRq> criteriaRqList) {
        SearchDTO.CriteriaRq criteriaRq = new SearchDTO.CriteriaRq();
        criteriaRq.setOperator(operator);
        criteriaRq.setFieldName(fieldName);
        if (value != null && value.equals("true"))
            criteriaRq.setValue(1);
        else if (value != null && value.equals("false"))
            criteriaRq.setValue(0);
        else if (value != null && value.equals("null")) {
            criteriaRq.setValue(null);
            criteriaRq.setOperator(EOperator.isNull);
        } else if (value != null && value.equals("notNull")) {
            criteriaRq.setValue(null);
            criteriaRq.setOperator(EOperator.notNull);
        } else
            criteriaRq.setValue(value);
        criteriaRq.setCriteria(criteriaRqList);
        return criteriaRq;
    }

}
