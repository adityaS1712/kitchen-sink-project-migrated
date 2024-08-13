package org.jboss.as.quickstarts.kitchensink.service;

public interface SequenceGeneratorService {
    long generateSequence(String seqName);
}