package com.example.demo.service.contract;

import com.example.demo.model.contract.Contract;
import com.example.demo.model.contract.ContractIBorrowedResponseDTO;
import com.example.demo.model.contract.ContractRequestDTO;
import org.springframework.data.domain.Slice;

public interface ContractService {

    public Contract addContract(ContractRequestDTO contractRequestDTO);
    public Contract getContract(Integer contractId);
//    public Contract modifyContract(Integer contractId, Integer contractStatus);
    public Contract modifyContract(Integer contractId, Integer permissionStatus);
    public Contract modifyContract(Integer contractId, String endDate);
    public Slice<ContractIBorrowedResponseDTO> findBorrowedItemList(Integer clientIndex, Integer page);
    public void scheduleContractOneDayBeforeExpireDate();
    public void scheduleContractExpireDate();
}
