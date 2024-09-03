package com.roundtable.roundtable;

import com.roundtable.roundtable.business.image.ImageUploader;
import com.roundtable.roundtable.business.mail.MailProvider;
import com.roundtable.roundtable.infrastructure.mail.MailProviderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {

    @MockBean
    protected MailProvider mailProvider;

    @MockBean
    protected ImageUploader imageUploader;
}
