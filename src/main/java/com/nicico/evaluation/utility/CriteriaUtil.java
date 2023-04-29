package com.nicico.evaluation.utility;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.evaluation.dto.FilterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
@RequiredArgsConstructor
public class CriteriaUtil {


    public static SearchDTO.SearchRq ConvertCriteriaToSearchRequest(List<FilterDTO> list, Integer count, Integer startIndex) {
        SearchDTO.SearchRq searchRq = new SearchDTO.SearchRq();
        Set<SearchDTO.CriteriaRq> criteriaRqList=new HashSet<>();
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
                } else if ("null".equals(criteria.getType())){
                    operator = EOperator.isNull;
                }else if ("notNull".equals(criteria.getType())){
                    operator = EOperator.notNull;
                }else {
                    operator = EOperator.contains;
                }
                criteriaRq=makeCriteria(criteria.getField(), (criteria.getValues()!=null  && !criteria.getValues().isEmpty()) ? criteria.getValues().get(0) : null, operator, new ArrayList<>());
             //todo
               if (!criteriaRq.getFieldName().equals("hasEvaluation"))
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
        else if (value != null && value.equals("null")){
            criteriaRq.setValue(null);
            criteriaRq.setOperator(EOperator.isNull);
        }
        else
            criteriaRq.setValue(value);
        criteriaRq.setCriteria(criteriaRqList);
        return criteriaRq;
    }

}
