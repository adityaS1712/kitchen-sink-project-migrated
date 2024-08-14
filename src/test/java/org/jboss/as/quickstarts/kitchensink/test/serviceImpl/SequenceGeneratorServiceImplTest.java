package org.jboss.as.quickstarts.kitchensink.test.serviceImpl;

import org.jboss.as.quickstarts.kitchensink.model.DatabaseSequence;
import org.jboss.as.quickstarts.kitchensink.service.impl.SequenceGeneratorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SequenceGeneratorServiceImplTest {

    @Mock
    private MongoOperations mongoOperations;

    @InjectMocks
    private SequenceGeneratorServiceImpl sequenceGeneratorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateSequence() {
        // Arrange
        String seqName = "testSeq";
        DatabaseSequence databaseSequence = new DatabaseSequence();
        databaseSequence.setSeq(2);

        when(mongoOperations.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(DatabaseSequence.class)
        )).thenReturn(databaseSequence);

        // Act
        long sequence = sequenceGeneratorService.generateSequence(seqName);

        // Assert
        assertEquals(2, sequence);
    }

    @Test
    public void testGenerateSequenceWhenCounterIsNull() {
        // Arrange
        String seqName = "testSeq";

        when(mongoOperations.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(DatabaseSequence.class)
        )).thenReturn(null);

        // Act
        long sequence = sequenceGeneratorService.generateSequence(seqName);

        // Assert
        assertEquals(1, sequence);
    }
}