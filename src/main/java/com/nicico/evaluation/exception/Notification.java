package com.nicico.evaluation.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class Notification {
    private Status status;
    private Object notify;
}
