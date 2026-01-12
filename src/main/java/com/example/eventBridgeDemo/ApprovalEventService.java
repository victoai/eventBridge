package com.example.eventBridgeDemo;

import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequestEntry;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResponse;

public class ApprovalEventService {

    private final EventBridgeClient eventBridgeClient;

    public ApprovalEventService() {
        this.eventBridgeClient = EventBridgeClient.create(); // 기본 자격증명 + 리전
    }

    // 결재 완료 후 EventBridge 이벤트 발행
    public void publishApprovalCompleted(
            String approvalId,
            String requesterEmail,
            String approverEmail,
            String adminEmail
    ) {

        // JSON 문자열 직접 생성 (Jackson 사용 안 함)
        String detailJson =
                "{"
                        + "\"approvalId\":\"" + approvalId + "\","
                        + "\"emails\":{"
                        +     "\"requester\":\"" + requesterEmail + "\","
                        +     "\"approver\":\"" + approverEmail + "\","
                        +     "\"admin\":\"" + adminEmail + "\""
                        + "}"
                        + "}";

        PutEventsRequestEntry entry = PutEventsRequestEntry.builder()
                .source("com.acorn.approval")          // 이벤트 출처
                .detailType("approvalCompleted")      // 이벤트 타입
                .detail(detailJson)                   // JSON 문자열
                .eventBusName("default")              // 기본 EventBus
                .build();

        PutEventsRequest request = PutEventsRequest.builder()
                .entries(entry)
                .build();

        try {
            PutEventsResponse response = eventBridgeClient.putEvents(request);
            System.out.println("Approval event published: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
