package host;

import com.solab.iso8583.IsoMessage;
import org.example.dto.Iso8583MessageDto;
import org.example.dto.PaymentDTO;

public class HostSimulator {

    /**
     * Processes a transaction using ISO8583 protocol.
     * Simulates host processing and sets response code to "00" (approved).
     *
     * @param isoMessageDto The ISO8583 message DTO
     * @param payment The original payment object
     * @return The updated payment with response information
     */
    public PaymentDTO processTransaction(Iso8583MessageDto isoMessageDto, PaymentDTO payment) {
        try {
            System.out.println("Processing ISO8583 transaction: " + payment.getTransactionType());

            // Simulate host processing logic
            // In a real implementation, this would:
            // 1. Parse the ISO8583 message
            // 2. Validate fields
            // 3. Perform business logic (authorization, etc.)
            // 4. Set response fields

            IsoMessage isoMessage = isoMessageDto.getIsoMessage();

            // Log some fields for debugging
            System.out.println("Processing Code: " + isoMessageDto.getField(3));
            System.out.println("Transaction Amount: " + isoMessageDto.getField(4));
            System.out.println("STAN: " + isoMessageDto.getField(11));

            // Simulate processing delay
            Thread.sleep(100);

            // Set response code to "00" (approved)
            isoMessageDto.setResponseCode("00");

            // Update payment with response information
            payment.setStatus("00");

            System.out.println("Transaction approved with response code: " + isoMessageDto.getResponseCode());

            return payment;

        } catch (Exception e) {
            System.err.println("Error processing transaction: " + e.getMessage());
            // Set declined response code
            isoMessageDto.setResponseCode("05"); // Declined
            payment.setStatus("DECLINED");
            return payment;
        }
    }

    /**
     * Legacy method for backward compatibility - processes sale transaction.
     * @deprecated Use processTransaction with Iso8583MessageDto instead
     */
    @Deprecated
    public String processSale(String transactionData) {
        System.out.println("Processing sale transaction: " + transactionData);
        return "SUCCESS";
    }

    /**
     * Legacy method for backward compatibility - processes void transaction.
     * @deprecated Use processTransaction with Iso8583MessageDto instead
     */
    @Deprecated
    public String processVoid(String transactionData) {
        System.out.println("Processing void transaction: " + transactionData);
        return "SUCCESS";
    }

    /**
     * Legacy method for backward compatibility - processes settlement transaction.
     * @deprecated Use processTransaction with Iso8583MessageDto instead
     */
    @Deprecated
    public String processSettlement(String transactionData) {
        System.out.println("Processing settlement transaction: " + transactionData);
        return "SUCCESS";
    }
}