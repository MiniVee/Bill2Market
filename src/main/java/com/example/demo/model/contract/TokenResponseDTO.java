package com.example.demo.model.contract;

import lombok.Data;

@Data
public class TokenResponseDTO {

    private String access_token;
    private String token_type;
    private String expire_in;
    private String scope;
    private String client_use_code;

}
