package com.meiya.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author xiaopf
 */
@Getter
public class PersonChangeEvent extends ApplicationEvent {
    private final Person person;
    private final String operateType;
    public PersonChangeEvent(Person person,String operateType) {
        super(person);
        this.person = person;
        this.operateType = operateType;
    }
}
