package org.example.service;

import host.HostSimulator;
import org.example.dto.Iso8583MessageDto;
import org.example.dto.PaymentDTO;
import org.example.dto.Result;

public class PaymentService {

    private final HostSimulator hostSimulator = new HostSimulator();

    /**
     * Processes a payment transaction by creating an ISO8583 DTO and delegating to the host simulator.
     *
     * @param payment The payment details
     * @return Result indicating success or failure
     */
    public Result<PaymentDTO> processPayment(PaymentDTO payment) {
        try {
            // Create ISO8583 message DTO from payment data
            Iso8583MessageDto isoMessageDto = new Iso8583MessageDto(payment);

            // Pass the DTO to host simulator for processing
            PaymentDTO processedPayment = hostSimulator.processTransaction(isoMessageDto, payment);

            // Check response code (field 39)
            String responseCode = isoMessageDto.getResponseCode();
            if ("00".equals(responseCode)) {
                processedPayment.setStatus("APPROVED");
                return Result.success(processedPayment);
            } else {
                processedPayment.setStatus("DECLINED");
                return Result.error("Transaction declined with response code: " + responseCode);
            }
        } catch (Exception e) {
            return Result.error("Error processing payment: " + e.getMessage());
        }
    }
}