package com.example.demo.service.contract;

import com.example.demo.model.contract.TokenResponseDTO;
import io.swagger.models.auth.In;

public interface ContractBankService {

    public TokenResponseDTO tokenRequestDTO();
    public void depositTransfer(Integer contractId, Integer clientIndex);

}
