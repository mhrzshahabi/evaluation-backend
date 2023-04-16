package com.nicico.evaluation.utility;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
public class CriteriaUtil {


    public static SearchDTO.SearchRq ConvertCriteriaToSearchRequest(List<FilterDTO> criteriaList, Integer count, Integer startIndex) {
        SearchDTO.SearchRq request = new SearchDTO.SearchRq();

        if (criteriaList != null && !criteriaList.isEmpty()) {
            List<SearchDTO.CriteriaRq> criteriaRqList = new ArrayList<>();
            AtomicReference<SearchDTO.CriteriaRq> criteriaRq = new AtomicReference<>(new SearchDTO.CriteriaRq());
            criteriaList.forEach(criteria -> {
                EOperator operator;
                if ("select".equals(criteria.getType())) {
                    operator = EOperator.equals;
                } else {
                    operator = EOperator.contains;
                }
                criteriaRq.set(makeCriteria(criteria.getField(), criteria.getValues().get(0), operator, criteriaRqList));
                criteriaRqList.add(criteriaRq.get());

            });
            request.setCriteria(criteriaRq.get());
        }
        request.setCount(count != null ? count : 30);
        request.setStartIndex(startIndex != null ? startIndex : 0);
        request.setDistinct(true);
        request.setSortBy("id");
        return request;
    }

    public static SearchDTO.CriteriaRq makeCriteria(String fieldName, Object value, EOperator operator, List<SearchDTO.CriteriaRq> criteriaRqList) {
        SearchDTO.CriteriaRq criteriaRq = new SearchDTO.CriteriaRq();
        criteriaRq.setOperator(operator);
        criteriaRq.setFieldName(fieldName);
        if (value != null && value.equals("true"))
            criteriaRq.setValue(1);
        else if (value != null && value.equals("false"))
            criteriaRq.setValue(0);
        else
            criteriaRq.setValue(value);
        criteriaRq.setCriteria(criteriaRqList);
        return criteriaRq;
    }

}
