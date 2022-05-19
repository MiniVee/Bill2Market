package com.example.demo.service.contract;

import com.example.demo.model.contract.TokenResponseDTO;

public interface ContractBankService {

    public TokenResponseDTO tokenRequestDTO();
    public void depositLenterTransfer(Integer contractId, Integer clientIndex);
    public void depositOwnerTransfer(Integer contractId, Integer clientIndex);

}
