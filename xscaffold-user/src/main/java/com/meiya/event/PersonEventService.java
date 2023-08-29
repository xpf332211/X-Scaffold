package com.meiya.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xiaopf
 */
@Service
@Slf4j
public class PersonEventService {
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    public void createPerson(Person person){
        PersonChangeEvent changeEvent = new PersonChangeEvent(person, "create");
        applicationEventPublisher.publishEvent(changeEvent);
    }
}
