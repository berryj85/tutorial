package berryj.tutorial.microservice.domain.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class TransactionResponse implements Serializable {

    private static final long serialVersionUID = 639741717645810500L;

    private String transactionId;
    private Date transactionDate;
}
