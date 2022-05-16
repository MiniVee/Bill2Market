package com.example.demo.service.contract;

import com.example.demo.exception.chat.ChatNotFoundException;
import com.example.demo.exception.contract.ContractNotFoundException;
import com.example.demo.model.chat.Chat;
import com.example.demo.model.chat.ChatMessage;
import com.example.demo.model.chat.MessageType;
import com.example.demo.model.contract.*;
import com.example.demo.repository.ChatRepository;
import com.example.demo.repository.ContractRepository;
import com.example.demo.repository.ContractRepositoryCustom;
import com.example.demo.service.chat.MessageService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ContractServiceImpl implements ContractService{

    private final RestTemplate restTemplate;
    private final Gson gson;
    private final ContractRepository contractRepository;
    private final ChatRepository chatRepository;
    private final MessageService messageService;
    private final ContractRepositoryCustom contractRepositoryCustom;

    @Override
    public Contract addContract(ContractRequestDTO contractRequestDTO) {
        Chat chat = chatRepository.findById(contractRequestDTO.getChatId()).orElseThrow(ChatNotFoundException::new);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return contractRepository.save(Contract.builder()
                                        .chat(chat)
                                        .startDate(LocalDate.parse(contractRequestDTO.getStartDate(), formatter))
                                        .endDate(LocalDate.parse(contractRequestDTO.getEndDate(), formatter))
                                        .contractStatus(ContractType.REQUEST)
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

    @Override
    public TokenResponseDTO tokenRequestDTO(Integer contractId) {
        String bankUrl = "https://testapi.openbanking.or.kr";

        URI uri = UriComponentsBuilder
                .fromUriString(bankUrl)
                .path("/oauth/2.0/token")
                .encode()
                .build()
                .toUri();

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("client_id", "07077a91-b1f4-4297-bbc6-33d5aad52f03");
        requestParams.add("client_secret", "69f797e1-36a9-40b2-8a56-65f4f760213a");
        requestParams.add("scope", "oob");
        requestParams.add("grant_type", "client_credentials");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(uri, requestParams, String.class);
        System.out.println(responseEntity.getBody());
        TokenResponseDTO tokenResponseDTO = null;
        if(responseEntity.getStatusCode() == HttpStatus.OK){
            tokenResponseDTO = gson.fromJson(responseEntity.getBody(), TokenResponseDTO.class);

        }
        System.out.println(tokenResponseDTO);
        return tokenResponseDTO;
    }



}
