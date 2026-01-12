package com.example.eventBridgeDemo;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApprovalEventController {

    private final ApprovalEventService approvalEventService;

    public ApprovalEventController() {
        this.approvalEventService = new ApprovalEventService();
    }

    @GetMapping("/send-approval-event")
    public String sendApprovalEvent() {

        // 💡 값은 매개변수 없이 코드에서 지정
        String approvalId = "APP-20240112-001";
        String requesterEmail = "victoai@naver.com";
        String approverEmail  = "victoai999@gmail.com";
        String adminEmail     = "victoai@naver.com";

        // EventBridge 이벤트 발행
        approvalEventService.publishApprovalCompleted(
                approvalId,
                requesterEmail,
                approverEmail,
                adminEmail
        );

        return "Approval event published for approvalId: " + approvalId;
    }
}
