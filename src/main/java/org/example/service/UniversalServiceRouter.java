package org.example.service;

import org.example.dto.ApiResponse;
import org.example.dto.UniversalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.enumConstant.ServiceNames;
/**
 * Universal Service Router - Routes UniversalDTO to appropriate service based on serviceName
 * This is the single entry point for all service operations
 */
@Service
public class UniversalServiceRouter {

    @Autowired
    private LoginService loginService;

    @Autowired
    private MobileUserService mobileUserService;

    @Autowired
    private TransactionDataService transactionDataService;

//    @Autowired
//    private MobileUserSettingService mobileUserSettingService;
//
//    @Autowired
//    private MIDService midService;
//
//    @Autowired
//    private TIDService tidService;
//
//    @Autowired
//    private BatchService batchService;

    /**
     * Main routing method - processes UniversalDTO based on serviceName
     */
    public ApiResponse<UniversalDTO> process(UniversalDTO request) {
        if (request == null || request.getServiceName() == null) {
            return ApiResponse.badRequest("Service name is required");
        }

        String serviceName = request.getServiceName().toUpperCase();

        if(serviceName.equals(ServiceNames.LOGIN.name())){
            return loginService.processLogin(request);
        }
        else if(serviceName.equals(ServiceNames.SIGN_UP.name())) {
            return mobileUserService.processSignup(request);
        }
        else if(serviceName.equals(ServiceNames.FORGOT_PASSWORD.name())) {
            return mobileUserService.processForgotPassword(request);
        }
        else if(serviceName.equals(ServiceNames.PAYMENT.name())) {
            return transactionDataService.processTransaction(request);
        }
        else if(serviceName.equals(ServiceNames.SALES_HISTORY.name())) {
            return transactionDataService.getUnsettledTransactionsList(request);
        }
        else if(serviceName.equals(ServiceNames.SALES_HISTORY_DETAILS.name())) {
            return transactionDataService.getSalesHistoryDetailById(request);
        }
        else if(serviceName.equals(ServiceNames.VOID.name())) {
            return transactionDataService.voidTransaction(request);
        }
        else if(serviceName.equals(ServiceNames.SETTLEMENT.name())) {
            return transactionDataService.doSettlement(request);
        }
        else if(serviceName.equals(ServiceNames.LOGOUT.name())) {
            loginService.logout(request);
            return ApiResponse.success(request, "Logout successful");
        }else if(serviceName.equals(ServiceNames.RESET_PASSWORD.name())) {
            return mobileUserService.processResetPassword(request);
        }
//        else if(serviceName.equals(ServiceNames.SEND_RECEIPT.name())) {
//            return batchService.processBatch(request);
//        }
        else{
            return ApiResponse.badRequest("Unknown service name: " + serviceName);
        }
    }

}
