package com.example.demo.model.contract;

import lombok.Data;

@Data
public class DepositForClientDTO {

    private Integer contractId;
    private Integer deposit;
    private Integer price;
    private Integer lenterIndex;
    private Integer ownerIndex;
    private String clientName;
    private String fintechId;
}

