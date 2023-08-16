package com.cadastrocliente.cadastroclienteapp.model.dto;

import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiErrorsDTO {

    private List<String> errors;

    public ApiErrorsDTO(BindingResult result){

        this.errors = new ArrayList<>();
        result.getAllErrors().forEach(error ->
                this.errors.add(error.getDefaultMessage()));
    }

    public ApiErrorsDTO(RuntimeException exception){
        this.errors = Arrays.asList(exception.getMessage());
    }

    public List<String> getErrors(){ return errors;}
}
