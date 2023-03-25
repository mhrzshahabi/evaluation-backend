package com.nicico.evaluation.common;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PageDTO {

    private long totalPage;
    private long currentPage;
    private int pageSize;
    private Long totalItems;
    private List list;





}


