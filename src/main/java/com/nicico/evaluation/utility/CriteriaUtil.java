package com.nicico.evaluation.utility;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.evaluation.dto.FilterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CriteriaUtil {

    public static List<FilterDTO.CriteriaRq> addCriteria(List<FilterDTO.CriteriaRq> criteriaRq, String field, String type, EOperator operator, String values) {
        FilterDTO.CriteriaRq newCriteria = createCriteria(field, type, operator, values);
        criteriaRq.add(newCriteria);
        return criteriaRq;
    }

    public static FilterDTO.CriteriaRq createCriteria(String field, String type, EOperator operator, Object values) {
        FilterDTO.CriteriaRq criteriaRq = new FilterDTO.CriteriaRq();
        criteriaRq.setField(field);
        criteriaRq.setType(type);
        criteriaRq.setOperator(operator);
        criteriaRq.setValues(values);
        return criteriaRq;
    }
}
