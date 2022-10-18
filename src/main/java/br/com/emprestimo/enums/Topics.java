package br.com.emprestimo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum Topics {
    CREATE_USER_TOPIC("create.user.topic");

    public final String topicName;


}
