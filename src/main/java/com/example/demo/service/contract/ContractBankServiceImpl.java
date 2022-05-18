package com.example.demo.service.contract;

import com.example.demo.model.contract.*;
import com.example.demo.repository.BillyPayRepository;
import com.example.demo.repository.ContractRepository;
import com.example.demo.util.OpenBankUtil;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ContractBankServiceImpl implements ContractBankService {

    private final RestTemplate restTemplate;
    private final Gson gson;
    private final ContractRepository contractRepository;
    private final Contract contract;
    private final BillyPayRepository billyPayRepository;

    @Override
    public TokenResponseDTO tokenRequestDTO() {
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

    @Override
    public void depositTransfer(Integer contractId, Integer clientIndex) {
//        Map<String, Object> temp = contractRepository.findLenterByContractId(contractId);

        List<DepositInfoReqListDTO> reqList = new ArrayList<>();
        DepositInfoReqListDTO depositInfoReqListDTO = new DepositInfoReqListDTO();
        depositInfoReqListDTO.setTran_no("1");
        depositInfoReqListDTO.setBank_tran_id(OpenBankUtil.getRandBankTranId("M202200946"));
        depositInfoReqListDTO.setFintech_use_num("");
        depositInfoReqListDTO.setPrint_content("빌리페이 보증금 환급");
        depositInfoReqListDTO.setTran_amt("");
        depositInfoReqListDTO.setReq_client_name("");
        depositInfoReqListDTO.setReq_client_num("BILL" + clientIndex);
        depositInfoReqListDTO.setTransfer_purpose("TR");

        reqList.add(depositInfoReqListDTO);

        String depositURL = "https://openapi.openbanking.or.kr";
        URI uri = UriComponentsBuilder
                .fromUriString(depositURL)
                .path("/v2.0/transfer/deposit/fin_num")
                .encode()
                .build()
                .toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer"+ tokenRequestDTO().getAccess_token());

        System.out.println("Test  " + httpHeaders);

        DepositInfoDTO depositInfoDTO = DepositInfoDTO.builder()
                .cntr_account_type("N")
                .contr_account_num("3521080943483")
                .wd_pass_phrase("NONE")
                .wd_print_content("보증금 반납 및 대여료 전송")
                .name_check_option("off")
                .tran_dtime(LocalTime.now().toString())
                .req_cnt("1")
                .req_list(reqList)
                .build();

        HttpEntity<String> httpEntity = new HttpEntity<String>(gson.toJson(depositInfoDTO), httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(uri, httpEntity, String.class);

        System.out.println("TTEST"+depositInfoDTO);

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            System.out.println("\nGood Test");
        }else {
            //exception처리
            System.out.println("\nBad Test");
        }
    }
}
