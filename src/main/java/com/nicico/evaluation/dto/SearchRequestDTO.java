package com.nicico.evaluation.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SearchRequestDTO {

   private List<SearchDataDTO> searchDataDTOList;

   @Setter
   @Getter
   public static class SearchDataDTO {
      private String fieldName;
      private Object value;
   }
}
