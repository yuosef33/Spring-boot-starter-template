package com.yuosef.springbootstartertemplate.models.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponseDto {

    private boolean success;

    private  String apiPath;


    private HttpStatus errorCode;


    private  String message;


    private LocalDateTime errorTime;
}
