package com.roundtable.roundtable.presentation.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.roundtable.roundtable.ControllerTestSupport;
import com.roundtable.roundtable.presentation.auth.request.EmailRequest;
import com.roundtable.roundtable.security.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

class AuthControllerTest extends ControllerTestSupport {
    @DisplayName("이메일로 인증코드를 전송한다.")
    @Test
    @WithMockCustomUser
    void sendAuthCode() throws Exception {
        //given
        EmailRequest request = new EmailRequest("dlwogns3413@naver.com");

        //when
        mockMvc.perform(
                post("/auth/emails")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.code").isEmpty());

    }

    @DisplayName("이메일 형식이 아니라면 에러를 던진다.")
    @Test
    @WithMockCustomUser
    void sendAuthCode_fail() throws Exception {
        //given
        EmailRequest request = new EmailRequest("dlwogns3413");

        //when
        mockMvc.perform(
                        post("/auth/emails")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("이메일 형식이 아닙니다."))
                .andExpect(jsonPath("$.code").isEmpty());
    }

    @DisplayName("이메일이 없다면 에러를 던진다.")
    @Test
    @WithMockCustomUser
    void sendAuthCode_empty_email() throws Exception {
        //given
        EmailRequest request = new EmailRequest("");

        //when
        mockMvc.perform(
                        post("/auth/emails")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.code").isEmpty());
    }
}