package org.example.enumConstant;

public enum TransactionStatus implements Status {
    SUCCESS(100, "Success", "transaction.status.success"),
    VOID(101, "Voided", "transaction.status.voided"),
    PENDING(102, "Pending", "transaction.status.pending"),
    SETTLED(103, "Settled", "transaction.status.settled");

    private final int code;
    private final String message;
    private final String messageKey;

    TransactionStatus(final int code, final String message, final String messageKey) {
        this.code = code;
        this.message = message;
        this.messageKey = messageKey;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getCodeInStringFormat() {
        return String.valueOf(this.code);
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getMessageKey() {
        return this.messageKey;
    }
}
