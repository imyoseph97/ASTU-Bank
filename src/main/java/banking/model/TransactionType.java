package banking.model;

import java.io.Serializable;

public enum TransactionType implements Serializable {
    DEPOSIT,
    WITHDRAWAL,
    TRANSFER_IN,
    TRANSFER_OUT
}
