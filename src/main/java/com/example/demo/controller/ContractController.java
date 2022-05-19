package com.example.demo.controller;

import com.example.demo.model.chat.ChatMessage;
import com.example.demo.model.chat.MessageType;
import com.example.demo.model.contract.Contract;
import com.example.demo.model.contract.ContractMessage;
import com.example.demo.model.contract.ContractRequestDTO;
import com.example.demo.model.response.CommonResult;
import com.example.demo.service.ResponseService;
import com.example.demo.service.chat.MessageService;
import com.example.demo.service.contract.ContractBankService;
import com.example.demo.service.contract.ContractService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@Api(tags = {"6. Contract"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/contracts")
public class ContractController {

    private final ResponseService responseService;
    private final ContractService contractService;
    private final MessageService messageService;
    private final ContractBankService contractBankService;

    @ApiOperation(value = "계약 정보 조회", notes = "계약 정보를 조회한다.")
    @GetMapping("/{contract-id}")
    public CommonResult getContractInfo(@PathVariable("contract-id") Integer contractId){
        return responseService.getSingleResult(contractService.getContract(contractId));
    }

    @ApiOperation(value = "채팅에 대한 계약 작성", notes = "채팅 정보에 맞는 계약을 작성한다.")
    @PostMapping("/{chat-id}")
    public CommonResult contract(@PathVariable("chat-id") Integer chatId, @RequestBody ContractRequestDTO contractRequestDTO){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        contractRequestDTO.setChatId(chatId);
        Contract contract = contractService.addContract(contractRequestDTO);
        ChatMessage chatMessage = ChatMessage.builder()
                .chatType(ChatMessage.ChatType.MESSAGE)
                .chatId(contract.getChat().getChatId())
                .senderNickname(contract.getChat().getOwner().getNickname())
                .messageType(MessageType.TRANS_REQUEST)
                .message(new ContractMessage(
                        contract.getContractId(),
                        contract.getStartDate(),
                        contract.getEndDate())
                        .toString())
                .build();
        messageService.message(chatMessage, Integer.parseInt(auth.getName()));
        return responseService.getSuccessfulResult();
    }

    @ApiOperation(value = "계약 상태 변경", notes = "계약의 상태를 변경한다.")
    @PutMapping("/status/{contract-id}")
    public CommonResult modifyContractStatus(@PathVariable("contract-id") Integer contractId, @RequestParam Integer contractStatus){
        Contract contract = contractService.modifyContract(contractId, contractStatus);
        ChatMessage chatMessage = ChatMessage.builder()
                .chatType(ChatMessage.ChatType.MESSAGE)
                .chatId(contract.getChat().getChatId())
                .senderNickname(contract.getChat().getOwner().getNickname())
                .messageType((contractStatus == 1)? MessageType.TRANS_ACCEPT : MessageType.TRANS_END)
                .message(String.valueOf(contract.getContractId()))
                .build();
        messageService.message(chatMessage, contract.getChat().getChatId());
        return responseService.getSuccessfulResult();
    }

    @ApiOperation(value = "계약 마감일 변경", notes = "계약의 마감일을 변경한다.")
    @PutMapping("/end-date/{contract-id}")
    public CommonResult modifyContractEndDate(@PathVariable("contract-id") Integer contractId, @RequestParam String endDate){
        Contract contract = contractService.modifyContract(contractId, endDate);
        ChatMessage chatMessage = ChatMessage.builder()
                .chatType(ChatMessage.ChatType.MESSAGE)
                .chatId(contract.getChat().getChatId())
                .senderNickname(contract.getChat().getOwner().getNickname())
                .messageType(MessageType.TRANS_ACCEPT)
                .message(String.valueOf(contract.getContractId()))
                .build();
        messageService.message(chatMessage, contract.getChat().getChatId());
        return responseService.getSuccessfulResult();
    }

    @ApiOperation(value = "보증금 돌려주기 구현", notes = "빌리페이 계좌에서 사용자 계좌에 이체된 보증금을 돌려준다.")
    @PostMapping("/deposit")
    public CommonResult contractDeposit(@RequestParam("contract-id")Integer contractId, Integer clientIndex){
        contractBankService.tokenRequestDTO();
        contractBankService.depositLenterTransfer(contractId, clientIndex);
        contractBankService.depositOwnerTransfer(contractId, clientIndex);
        return responseService.getSuccessfulResult();
    }

}
