package com.example.demo.model.contract;

import com.example.demo.model.chat.Chat;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="Contract")
@NamedNativeQuery(
        name = "findClientInfo",
        query = "SELECT DISTINCT Contract.contract_id, deposit, price, Chat.owner_index, Chat.lenter_index, Client.nickname AS 'owner_nickname', Client2.nickname AS 'lenter_nickname', Billy_Pay.fintech_id AS 'owner_fintech_id', Billy_Pay2.fintech_id AS 'lenter_fintech_id' " +
                "FROM Contract " +
                "INNER JOIN Chat ON Contract.chat_id = Chat.chat_id " +
                "INNER JOIN Client ON Chat.owner_index=Client.client_index " +
                "INNER JOIN Client AS Client2 ON Chat.lenter_index=Client2.client_index " +
                "LEFT JOIN Billy_Pay ON Client.client_index = Billy_Pay.client_index " +
                "INNER JOIN Billy_Pay AS Billy_Pay2 ON Client2.client_index =Billy_Pay2.client_index " +
                "WHERE :contract_id=Contract.contract_id ",
        resultSetMapping = "DepositForClientDTOMapping"
        )
@SqlResultSetMapping(
        name = "DepositForClientDTOMapping",
        classes = @ConstructorResult(
                targetClass = DepositForClientDTO.class,
                columns = {
                        @ColumnResult(name = "contract_id", type = Integer.class),
                        @ColumnResult(name = "deposit", type = Integer.class),
                        @ColumnResult(name = "price", type = Integer.class),
                        @ColumnResult(name = "owner_index", type = Integer.class),
                        @ColumnResult(name = "lenter_index", type = Integer.class),
                        @ColumnResult(name = "owner_nickname", type = String.class),
                        @ColumnResult(name = "lenter_nickname", type = String.class),
                        @ColumnResult(name = "owner_fintech_id", type = String.class),
                        @ColumnResult(name = "lenter_fintech_id", type = String.class)
                }
        )
)
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
