package com.retail.rewards.model.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString(of = "message")
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseModel {

    private String message;

}