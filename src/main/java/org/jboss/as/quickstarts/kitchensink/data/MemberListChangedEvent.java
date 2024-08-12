package org.jboss.as.quickstarts.kitchensink.data;

import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.springframework.context.ApplicationEvent;

public class MemberListChangedEvent extends ApplicationEvent {
    private final Member member;

    public MemberListChangedEvent(Object source, Member member) {
        super(source);
        this.member = member;
    }

    public Member getMember() {
        return member;
    }
}