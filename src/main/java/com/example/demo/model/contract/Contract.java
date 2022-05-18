package com.example.demo.model.contract;

import com.example.demo.model.chat.Chat;
import com.example.demo.model.item.ItemOwnerResponseDTO;
import com.example.demo.model.item.SimpleItem;
import io.swagger.models.auth.In;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="Contract")

//@NamedNativeQuery(
//        name = "findLenterInfo",
//        query = "SELECT DISTINCT Contract.contract_id, Contract.deposit, Chat.lenter_index, Client.client_name, Billy_Pay.fintech_id " +
//                "FROM Contract " +
//                "INNER JOIN Chat ON Contract.chat_id = Chat.chat_id " +
//                "INNER JOIN Client ON Chat.lenter_index=Client.client_index " +
//                "LEFT JOIN Billy_Pay ON Client.client_index = Billy_Pay.client_index " +
//                "WHERE :contract_id=Contract.contract_id ",
//        resultSetMapping = "DepositForClientDTOMapping"
//        )
//@SqlResultSetMapping(
//        name = "DepositForClientDTOMapping",
//        classes = @ConstructorResult(
//                targetClass = DepositForClientDTO.class,
//                columns = {
//                        @ColumnResult(name = "contract_id", type = Integer.class),
//                        @ColumnResult(name = "deposit", type = Integer.class),
//                        @ColumnResult(name = "price", type = Integer.class),
//                        @ColumnResult(name = "lenter_index", type = Integer.class),
//                        @ColumnResult(name = "owner_index", type = Integer.class),
//                        @ColumnResult(name = "client_name", type = String.class),
//                        @ColumnResult(name = "fintect_id", type = String.class),
//                }
//        )
//)
public class Contract {
    @Id
    @Column(name = "contract_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contractId;
    @OneToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;
    @Column(name = "contract_status")
    @Enumerated(value = EnumType.ORDINAL)
    private ContractType contractStatus;
    @Column(name = "price")
    private Integer price;
    @Column(name = "deposit")
    private Integer deposit;
    @Column(name = "billpay_status")
    @Enumerated(value = EnumType.ORDINAL)
    private BillpayStatus billpayStatus;
    @Column(name = "permission_status")
    @Enumerated(value = EnumType.ORDINAL)
    private PermissionStatus permissionStatus;
    @Column(name = "retrieve_status")
    @Enumerated(value = EnumType.ORDINAL)
    private RetrieveStatus retrieveStatus;
    @Column(name = "contract_date")
    private LocalDate contractDate;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "review_write")
    @Enumerated(value = EnumType.ORDINAL)
    private ReviewWrite reviewWrite;


}
