package com.roundtable.roundtable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roundtable.roundtable.business.auth.AuthService;
import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.business.member.MemberValidator;
import com.roundtable.roundtable.presentation.auth.AuthController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = {AuthController.class})
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private MemberReader memberReader;

    @MockBean
    private MemberValidator memberValidator;

    @Autowired
    protected ObjectMapper objectMapper;
}
