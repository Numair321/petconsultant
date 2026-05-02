package com.petconsultant.service;

import com.petconsultant.enumerator.CallType;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.CallResponse;
import java.util.List;

public interface CallService {
    ApiResponse initiateCall(Long vetId, CallType callType);
    ApiResponse endCall(Long callId);
    List<CallResponse> getCallHistory();
}
