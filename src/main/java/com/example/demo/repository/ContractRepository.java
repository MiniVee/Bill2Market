package com.example.demo.repository;

import com.example.demo.model.contract.Contract;
import com.example.demo.model.contract.DepositForClientDTO;
import com.example.demo.model.contract.DepositInfoReqListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {

    @Query(name = "findClientInfo", nativeQuery = true) //결제를 위한 clientInfo에 관련된 쿼리
    public List<DepositForClientDTO> findLenterByContractId(@Param("contract_id") Integer contractId);
}
