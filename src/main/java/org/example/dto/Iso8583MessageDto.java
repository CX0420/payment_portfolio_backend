package org.example.dto;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.MessageFactory;
import org.example.model.TID;
import org.example.model.TransactionData;

/**
 * DTO for ISO8583 transaction messages.
 * Uses j8583 library to handle ISO8583 message creation and parsing.
 */
public class Iso8583MessageDto {

    private IsoMessage isoMessage;

    // Constructor to create ISO8583 message from Payment
    public Iso8583MessageDto(PaymentDTO payment) {
        try {
            MessageFactory<IsoMessage> messageFactory = new MessageFactory<>();
            messageFactory.setCharacterEncoding("UTF-8");

            // Create a new ISO8583 message (MTI 0200 for transaction request)
            this.isoMessage = messageFactory.newMessage(0x200);

            // Set common fields
            isoMessage.setField(2, new IsoValue<>(IsoType.LLVAR, "1234567890123456")); // Primary Account Number (PAN)
            isoMessage.setField(3, new IsoValue<>(IsoType.NUMERIC, "000000", 6)); // Processing Code
            isoMessage.setField(4, new IsoValue<>(IsoType.NUMERIC, String.format("%012d", (int)(payment.getAmount() * 100)), 12)); // Transaction Amount
            isoMessage.setField(7, new IsoValue<>(IsoType.NUMERIC, "1225123456", 10)); // Transmission Date & Time
            isoMessage.setField(11, new IsoValue<>(IsoType.NUMERIC, "123456", 6)); // System Trace Audit Number (STAN)
            isoMessage.setField(12, new IsoValue<>(IsoType.NUMERIC, "123456", 6)); // Local Transaction Time
            isoMessage.setField(13, new IsoValue<>(IsoType.NUMERIC, "1225", 4)); // Local Transaction Date
            isoMessage.setField(18, new IsoValue<>(IsoType.NUMERIC, "5999", 4)); // Merchant Type
            isoMessage.setField(22, new IsoValue<>(IsoType.NUMERIC, "012", 3)); // Point of Service Entry Mode
            isoMessage.setField(25, new IsoValue<>(IsoType.NUMERIC, "00", 2)); // Point of Service Condition Code
            isoMessage.setField(32, new IsoValue<>(IsoType.LLVAR, "123456")); // Acquiring Institution ID Code
            isoMessage.setField(37, new IsoValue<>(IsoType.ALPHA, "123456789012", 12)); // Retrieval Reference Number
            isoMessage.setField(41, new IsoValue<>(IsoType.ALPHA, "TERM1234", 8)); // Card Acceptor Terminal ID
            isoMessage.setField(42, new IsoValue<>(IsoType.ALPHA, "MERCH123456789", 15)); // Card Acceptor ID Code
            isoMessage.setField(43, new IsoValue<>(IsoType.LLVAR, "Merchant Name                    City Name    Country")); // Card Acceptor Name/Location
            isoMessage.setField(49, new IsoValue<>(IsoType.NUMERIC, "840", 3)); // Transaction Currency Code (USD)

            // Set transaction type specific processing code
            String processingCode = getProcessingCode(payment.getTransactionType());
            isoMessage.setField(3, new IsoValue<>(IsoType.NUMERIC, processingCode, 6));

        } catch (Exception e) {
            throw new RuntimeException("Failed to create ISO8583 message", e);
        }
    }

    // Constructor to create ISO8583 message from TransactionData
    public Iso8583MessageDto(TransactionData transactionData, TID tid, String transactionType) {
        try {
            MessageFactory<IsoMessage> messageFactory = new MessageFactory<>();
            messageFactory.setCharacterEncoding("UTF-8");

            // Create a new ISO8583 message (MTI 0200 for transaction request)
            this.isoMessage = messageFactory.newMessage(0x200);

            // Set common fields
            isoMessage.setField(2, new IsoValue<>(IsoType.LLVAR, transactionData.getPan() != null ? transactionData.getPan() : "1234567890123456")); // Primary Account Number (PAN)
            isoMessage.setField(3, new IsoValue<>(IsoType.NUMERIC, getProcessingCode(transactionType), 6)); // Processing Code
            isoMessage.setField(4, new IsoValue<>(IsoType.NUMERIC, String.format("%012d", transactionData.getAmountAuthorized()), 12)); // Transaction Amount
            isoMessage.setField(7, new IsoValue<>(IsoType.NUMERIC, "1225123456", 10)); // Transmission Date & Time
            isoMessage.setField(11, new IsoValue<>(IsoType.NUMERIC, tid.getStan() != null ? String.format("%06d", tid.getStan()) : "123456", 6)); // System Trace Audit Number (STAN)
            isoMessage.setField(12, new IsoValue<>(IsoType.NUMERIC, "123456", 6)); // Local Transaction Time
            isoMessage.setField(13, new IsoValue<>(IsoType.NUMERIC, "1225", 4)); // Local Transaction Date
            isoMessage.setField(18, new IsoValue<>(IsoType.NUMERIC, "5999", 4)); // Merchant Type
            isoMessage.setField(22, new IsoValue<>(IsoType.NUMERIC, "012", 3)); // Point of Service Entry Mode
            isoMessage.setField(25, new IsoValue<>(IsoType.NUMERIC, "00", 2)); // Point of Service Condition Code
            isoMessage.setField(32, new IsoValue<>(IsoType.LLVAR, "123456")); // Acquiring Institution ID Code
            isoMessage.setField(37, new IsoValue<>(IsoType.ALPHA, "123456789012", 12)); // Retrieval Reference Number
            isoMessage.setField(41, new IsoValue<>(IsoType.ALPHA, "TERM1234", 8)); // Card Acceptor Terminal ID
            isoMessage.setField(42, new IsoValue<>(IsoType.ALPHA, "MERCH123456789", 15)); // Card Acceptor ID Code
            isoMessage.setField(43, new IsoValue<>(IsoType.LLVAR, "Merchant Name                    City Name    Country")); // Card Acceptor Name/Location
            isoMessage.setField(49, new IsoValue<>(IsoType.NUMERIC, "840", 3)); // Transaction Currency Code (USD)

        } catch (Exception e) {
            throw new RuntimeException("Failed to create ISO8583 message", e);
        }
    }

    // Constructor for response messages
    public Iso8583MessageDto(IsoMessage isoMessage) {
        this.isoMessage = isoMessage;
    }

    private String getProcessingCode(String transactionType) {
        switch (transactionType.toUpperCase()) {
            case "SALE":
                return "000000"; // Purchase
            case "VOID":
                return "020000"; // Purchase Reversal
            case "SETTLEMENT":
                return "920000"; // Settlement
            default:
                return "000000";
        }
    }

    public IsoMessage getIsoMessage() {
        return isoMessage;
    }

    public void setIsoMessage(IsoMessage isoMessage) {
        this.isoMessage = isoMessage;
    }

    // Get field value by field number
    public String getField(int fieldNumber) {
        try {
            IsoValue<?> field = isoMessage.getField(fieldNumber);
            return field != null ? field.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }

    // Set field value
    public void setField(int fieldNumber, String value) {
        try {
            isoMessage.setField(fieldNumber, new IsoValue<>(IsoType.LLVAR, value));
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field " + fieldNumber, e);
        }
    }

    // Get response code (field 39)
    public String getResponseCode() {
        return getField(39);
    }

    // Set response code (field 39)
    public void setResponseCode(String responseCode) {
        isoMessage.setField(39, new IsoValue<>(IsoType.NUMERIC, responseCode, 2));
    }
}