package com.roundtable.roundtable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roundtable.roundtable.business.auth.AuthService;
import com.roundtable.roundtable.business.house.HouseService;
import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.business.member.MemberService;
import com.roundtable.roundtable.business.member.MemberValidator;
import com.roundtable.roundtable.presentation.auth.AuthController;
import com.roundtable.roundtable.presentation.house.HouseController;
import com.roundtable.roundtable.presentation.member.MemberController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = {
        AuthController.class,
        MemberController.class,
        HouseController.class
})
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private MemberReader memberReader;

    @MockBean
    private MemberValidator memberValidator;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected HouseService houseService;
}
