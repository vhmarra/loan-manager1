package br.com.emprestimo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Topics {
    CREATE_USER_TOPIC("create.user.topic"),
    CREATE_PAYMENTS_TOPIC("create.payments.topic"),
    LOG_TOPIC("log.topic");

    public final String topicName;


}
