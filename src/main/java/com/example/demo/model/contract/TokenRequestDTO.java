package com.example.demo.model.contract;

import lombok.Data;

@Data
public class TokenRequestDTO {

    private Integer contractId;
    private String bankClientID;
    private String bankClientSecret;
    private String scope;
    private String grant_type;
}
