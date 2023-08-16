package com.cadastrocliente.cadastroclienteapp.handlers;

import com.cadastrocliente.cadastroclienteapp.exceptions.BusinessExceptions;
import com.cadastrocliente.cadastroclienteapp.exceptions.InternalErrorException;
import com.cadastrocliente.cadastroclienteapp.model.dto.ApiErrorsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationaExceptionHandler {

    @InitBinder("businessException")
    protected void initBusinessExceptionBinder(WebDataBinder binder) {
        binder.setAllowedFields("detailMessage");

    }

    @InitBinder("")
    protected void initInternalErrorExceptionBinder(WebDataBinder binder) {
        binder.setDisallowedFields("detailMessage");
    }

    @ResponseStatus(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorsDTO methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        return new ApiErrorsDTO(result);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessExceptions.class)
    public ApiErrorsDTO businessExceptionHandler(BusinessExceptions businessExceptions){
        return new ApiErrorsDTO(businessExceptions);
    }
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalErrorException.class)
    public ApiErrorsDTO internalErrorExceptionHandler(InternalErrorException internalErrorException){
        return new ApiErrorsDTO(internalErrorException);
    }
}
