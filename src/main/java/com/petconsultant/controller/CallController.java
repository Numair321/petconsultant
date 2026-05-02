package com.petconsultant.controller;

import com.petconsultant.enumerator.CallType;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.CallResponse;
import com.petconsultant.service.CallService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/call")
public class CallController {

    private final CallService callService;

    public CallController(CallService callService) {
        this.callService = callService;
    }

    // POST /test/v1/api/call/initiate/{vetId}?callType=VIDEO
    @PostMapping("/initiate/{vetId}")
    public ResponseEntity<ApiResponse> initiateCall(@PathVariable Long vetId,
                                                     @RequestParam(defaultValue = "VIDEO") CallType callType) {
        ApiResponse response = callService.initiateCall(vetId, callType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // PUT /test/v1/api/call/end/{callId}
    @PutMapping("/end/{callId}")
    public ResponseEntity<ApiResponse> endCall(@PathVariable Long callId) {
        ApiResponse response = callService.endCall(callId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // GET /test/v1/api/call/history
    @GetMapping("/history")
    public ResponseEntity<ApiResponse> getCallHistory() {
        List<CallResponse> calls = callService.getCallHistory();
        ApiResponse response = new ApiResponse("SUCCESS", "Call history fetched", calls);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
