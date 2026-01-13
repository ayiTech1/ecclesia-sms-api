package com.ayitech.ecclesiasms.service;

import com.ayitech.ecclesiasms.model.UserData;
import com.ayitech.ecclesiasms.repository.UserDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.pinpointsmsvoicev2.PinpointSmsVoiceV2AsyncClient;
import software.amazon.awssdk.services.pinpointsmsvoicev2.model.SendTextMessageRequest;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDataServiceImpl {

    private final UserDataRepository repository;
    private final PinpointSmsVoiceV2AsyncClient client;

    @Async
    @Transactional
    public CompletableFuture<String> sendBulkAsync(UserData request) {

        request.setStatus("PENDING");
        UserData saved = repository.save(request);

        SendTextMessageRequest awsReq = SendTextMessageRequest.builder()
                .destinationPhoneNumber(request.getPhoneNumbers().get(0))
                .messageBody(request.getMessage())
                .originationIdentity(request.getOriginationIdentity())
                .build();

        return client.sendTextMessage(awsReq)
                .thenApply(response -> {
                    saved.setStatus("SENT");
                    saved.setSentAt(LocalDateTime.now());
                    repository.save(saved);

                    log.info("SMS sent successfully. MessageId={}", response.messageId());
                    return "Request ID: " + saved.getId() + " - SENT";
                })
                .exceptionally(ex -> {
                    saved.setStatus("FAILED");
                    repository.save(saved);

                    log.error("SMS failed for request ID {}", saved.getId(), ex);
                    return "Request ID: " + saved.getId() + " - FAILED";
                });
    }
}
