package request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Getter
@Setter
public class TransferRequest {
    private long senderAcctNum;

    private long receiverAcctNum;

    private BigDecimal amount;
}
