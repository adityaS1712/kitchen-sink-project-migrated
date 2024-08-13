/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.kitchensink.service.impl;


import org.jboss.as.quickstarts.kitchensink.model.Member;


import org.jboss.as.quickstarts.kitchensink.service.MemberRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


import java.util.logging.Logger;


@Service
public class MemberRegistrationImpl implements MemberRegistrationService {

    @Autowired
    private Logger log;


    @Autowired
    private MongoTemplate mongoTemplate;


@Override
    public void register(Member member) {
        log.info("Registering" + member.getName());
        mongoTemplate.save(member);
    }

}
