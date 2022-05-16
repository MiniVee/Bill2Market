package com.example.demo.service.contract;

import com.example.demo.model.contract.Contract;
import com.example.demo.model.contract.ContractRequestDTO;
import com.example.demo.model.contract.TokenResponseDTO;

public interface ContractService {

    public Contract addContract(ContractRequestDTO contractRequestDTO);
    public Contract getContract(Integer contractId);
    public Contract modifyContract(Integer contractId, Integer contractStatus);
    public Contract modifyContract(Integer contractId, String endDate);
    public void scheduleContractOneDayBeforeExpireDate();
    public void scheduleContractExpireDate();
    public TokenResponseDTO tokenRequestDTO(Integer contractId);


}
