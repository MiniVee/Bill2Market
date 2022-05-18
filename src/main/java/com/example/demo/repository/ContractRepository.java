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

    @Query(name = "findLenterInfo", nativeQuery = true)
    public List<DepositForClientDTO> findLenterByContractId(@Param("contract_id") Integer contractId);

//    @Query(Name = "SELECT DISTINCT Contract.contract_id, Contract.price, Chat.owner_index, Client.nickname, Billy_Pay.fintech_id " +
//            "FROM Contract " +
//            "INNER JOIN Chat ON Contract.chat_id = Chat.chat_id " +
//            "INNER JOIN Client ON Chat.owner_index=Client.client_index " +
//            "LEFT JOIN Billy_Pay ON Client.client_index = Billy_Pay.client_index " +
//            "WHERE :contract_id=Contract.contract_id ", nativeQuery = true)
//    public


}
