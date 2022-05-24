package com.example.demo.service.contract;

import com.example.demo.exception.chat.ChatNotFoundException;
import com.example.demo.exception.contract.ContractNotFoundException;
import com.example.demo.model.chat.Chat;
import com.example.demo.model.contract.*;
import com.example.demo.model.chat.ChatMessage;
import com.example.demo.model.chat.MessageType;
import com.example.demo.repository.ChatRepository;
import com.example.demo.repository.ContractRepository;
import com.example.demo.repository.ContractRepositoryCustom;
import com.example.demo.service.chat.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ContractServiceImpl implements ContractService{

    private final ContractRepository contractRepository;
    private final ChatRepository chatRepository;
    private final MessageService messageService;
    private final ContractRepositoryCustom contractRepositoryCustom;

    @Override
    public Contract makeContract(ContractChatRequestDTO contractChatRequestDTO) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return contractRepository.save(Contract.builder()
                        .chat(chatRepository.findById(contractChatRequestDTO.getChatId()).orElseThrow(ChatNotFoundException::new))
                        .contractStatus(ContractType.REQUEST)
                        .price(contractChatRequestDTO.getPrice())
                        .deposit(contractChatRequestDTO.getDeposit())
                        .billpayStatus(BillpayStatus.NOTUSE)
                        .permissionStatus(PermissionStatus.NOTACCEPT)
                        .retrieveStatus(RetrieveStatus.RETURNNOTACCEPT)
                        .contractDate(null)
                        .startDate(LocalDate.parse(contractChatRequestDTO.getStartDate(), dateTimeFormatter))
                        .endDate(LocalDate.parse(contractChatRequestDTO.getEndDate(), dateTimeFormatter))
                        .reviewWrite(ReviewWrite.NOTWRITE)
                        .build());
    }

    @Override
    public Contract getContract(Integer contractId) {
        return contractRepository.findById(contractId).orElseThrow(ContractNotFoundException::new);
    }

    @Override
    public Contract modifyContract(Integer contractId, Integer contractStatus) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(ContractNotFoundException::new);
        contract.setContractStatus(ContractType.values()[contractStatus]);
        contract.setContractDate(LocalDate.now());
        return contractRepository.save(contract);
    }

    @Override
    public Contract modifyContract(Integer contractId, String endDate) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(ContractNotFoundException::new);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        contract.setEndDate(LocalDate.parse(endDate, formatter));
        return contractRepository.save(contract);
    }

    @Override
    public Slice<ContractIBorrowedResponseDTO> findBorrowedItemList(Integer clientIndex, Integer page) {

        return contractRepository.findContractsByClientIndex(clientIndex, PageRequest.of(page, 10));
    }

    @Async
    @Scheduled(cron = "0 0 0 1/1 * ?")
    @Override
    public void scheduleContractOneDayBeforeExpireDate() {
        List<ContractScheduleDTO> contractOneDayBeforeList = contractRepositoryCustom.findByExpireBeforeContract(LocalDate.now());
        contractOneDayBeforeList.forEach(contractScheduleDTO -> {
                    messageService.message(
                            ChatMessage.getChatMessage(contractScheduleDTO, MessageType.APPROACH_EXPIRE),
                            contractScheduleDTO.getSenderId()
                    );
                }
        );
    }

    @Transactional
    @Async
    @Scheduled(cron = "0 0 0 1/1 * ?")
    @Override
    public void scheduleContractExpireDate() {
        List<ContractScheduleDTO> contractExpireList = contractRepositoryCustom.findByExpireContract(LocalDate.now());
        contractExpireList.forEach(contractScheduleDTO -> {
                    contractRepositoryCustom.modifyState(contractScheduleDTO.getContractId(), ContractType.values()[2]);
                    messageService.message(
                            ChatMessage.getChatMessage(contractScheduleDTO, MessageType.EXPIRE),
                            contractScheduleDTO.getSenderId()
                    );
                }
        );
    }

}
