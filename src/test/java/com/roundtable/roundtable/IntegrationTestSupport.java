package com.roundtable.roundtable;

import com.roundtable.roundtable.business.image.ImageUploader;
import com.roundtable.roundtable.infrastructure.mail.JavaMailProvider;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {

    @MockBean
    protected JavaMailProvider mailProvider;

    @MockBean
    protected ImageUploader imageUploader;
}
