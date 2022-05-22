package com.example.demo.advice;

import com.example.demo.exception.ExceptionList;
import com.example.demo.exception.basket.BasketNotFoundException;
import com.example.demo.exception.basket.DuplicateBasketException;
import com.example.demo.exception.chat.ChatFileCreateFailedException;
import com.example.demo.exception.chat.ChatNotFoundException;
import com.example.demo.exception.client.*;
import com.example.demo.exception.common.HttpFailException;
import com.example.demo.exception.contract.ContractNotFoundException;
import com.example.demo.exception.contract.ContractTransferErrorException;
import com.example.demo.exception.contract.TokenNotFoundException;
import com.example.demo.exception.item.ItemNotFoundException;
import com.example.demo.model.response.CommonResult;
import com.example.demo.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult unknown(Exception e){
        log.error(e.getMessage());
        return responseService.getFailResult(ExceptionList.UNKNOWN.getCode(), ExceptionList.UNKNOWN.getMessage());
    }

    @ExceptionHandler(ClientNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult clientNotFoundException(){
        return responseService.getFailResult(ExceptionList.CLIENT_NOT_FOUNT.getCode(), ExceptionList.CLIENT_NOT_FOUNT.getMessage());
    }

    @ExceptionHandler(PasswordMisMatchException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult passwordMisMatchException(){
        return responseService.getFailResult(ExceptionList.PASSWORD_MISMATCH.getCode(), ExceptionList.PASSWORD_MISMATCH.getMessage());
    }

    @ExceptionHandler(CAuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult authenticationEntryPointException(){
        return responseService.getFailResult(ExceptionList.AUTHENTICATION_ENTRYPOINT.getCode(), ExceptionList.AUTHENTICATION_ENTRYPOINT.getMessage());
    }

    @ExceptionHandler(CAccessDeniedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult accessDeniedException(){
        return responseService.getFailResult(ExceptionList.ACCESS_DENIED.getCode(), ExceptionList.ACCESS_DENIED.getMessage());
    }

    @ExceptionHandler(InputNullException.class) //빈 값 입력 예외 사항 처리
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult InputNullException(){
        return responseService.getFailResult(ExceptionList.INPUT_NULL.getCode(), ExceptionList.INPUT_NULL.getMessage());
    }

    @ExceptionHandler(ExistIdException.class) //이미 존재하는 아이디 예외 사항 처리
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult ExistIdException(){
        return responseService.getFailResult(ExceptionList.EXIST_ID.getCode(), ExceptionList.EXIST_ID.getMessage());
    }

    @ExceptionHandler(HttpFailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult HttpFailException(){
        return responseService.getFailResult(ExceptionList.HTTP_FAIL.getCode(), ExceptionList.EXIST_ID.getMessage());
    }

    @ExceptionHandler(ExistNicknameException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult ExistNicknameException(){//이미 존재하는 닉네임 예외 사항 처리
        return responseService.getFailResult(ExceptionList.EXIST_NICKNAME.getCode(), ExceptionList.EXIST_NICKNAME.getMessage());
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult itemNotFoundException(){
        return responseService.getFailResult(ExceptionList.ITEM_NOT_FOUND.getCode(), ExceptionList.ITEM_NOT_FOUND.getMessage());
    }

    @ExceptionHandler(DuplicateBasketException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult duplicateBasketException(){
        return responseService.getFailResult(ExceptionList.DUPLICATE_BASKET.getCode(), ExceptionList.DUPLICATE_BASKET.getMessage());
    }

    @ExceptionHandler(BasketNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult basketNotFoundException(){
        return responseService.getFailResult(ExceptionList.BASKET_NOT_FOUND.getCode(), ExceptionList.BASKET_NOT_FOUND.getMessage());
    }

    @ExceptionHandler(ChatNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult chatNotFoundException(){
        return responseService.getFailResult(ExceptionList.CHAT_NOT_FOUND.getCode(), ExceptionList.CHAT_NOT_FOUND.getMessage());
    }

    @ExceptionHandler(ChatFileCreateFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult chatFileCreateFailedException(){
        return responseService.getFailResult(ExceptionList.CHATFILE_CREATE_FAIL.getCode(), ExceptionList.CHATFILE_CREATE_FAIL.getMessage());
    }

    @ExceptionHandler(ContractNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult contractNotFoundException(){
        return responseService.getFailResult(ExceptionList.CONTRACT_NOT_FOUND.getCode(), ExceptionList.CONTRACT_NOT_FOUND.getMessage());
    }

    @ExceptionHandler(ContractTransferErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult ContractTransferErrorException(){
        return responseService.getFailResult(ExceptionList.TRANSFER_ERROR.getCode(), ExceptionList.TRANSFER_ERROR.getMessage());
    }

    @ExceptionHandler(TokenNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult TokenNotFoundException(){
        return responseService.getFailResult(ExceptionList.TOKEN_NOT_FOUND_ERROR.getCode(), ExceptionList.TOKEN_NOT_FOUND_ERROR.getMessage());
    }

}
