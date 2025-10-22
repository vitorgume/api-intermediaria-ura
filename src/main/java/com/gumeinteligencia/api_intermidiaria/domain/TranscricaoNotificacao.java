package com.gumeinteligencia.api_intermidiaria.domain;

import lombok.*;
import software.amazon.awssdk.services.sqs.model.Message;

@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class TranscricaoNotificacao {
    private String telefone;
    private String jobName;
    private String s3Bucket;
    private String s3Key;
    private String transcribeOutput;
    private String durationMs;
    private String language;
    private Message mensagemFila;
}
