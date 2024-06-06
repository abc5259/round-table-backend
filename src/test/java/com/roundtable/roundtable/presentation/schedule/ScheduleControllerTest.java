package com.roundtable.roundtable.presentation.schedule;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.roundtable.roundtable.ControllerTestSupport;
import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.presentation.schedule.request.CreateScheduleRequest;
import com.roundtable.roundtable.security.WithMockCustomUser;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class ScheduleControllerTest extends ControllerTestSupport {

    private static final String API_PREFIX = "/schedule";

    @DisplayName("반복 스케줄을 생성할 수 있다.")
    @WithMockCustomUser
    @Test
    void createRepeatSchedule() throws Exception {
        //given
        CreateScheduleRequest request = new CreateScheduleRequest(
                "name",
                LocalDate.now(),
                LocalTime.now(),
                DivisionType.FIX,
                List.of(1L),
                List.of(1),
                Category.CLEANING
        );

        Mockito.when(scheduleService.createSchedule(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(1L);

        //when
        ResultActions resultActions = mockMvc.perform(
                post(API_PREFIX + "/repeat/house/{houseId}", 1L)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").value(1L))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.code").isEmpty());

    }

    @DisplayName("일회성 스케줄을 생성할 수 있다.")
    @WithMockCustomUser
    @Test
    void createOneTimeSchedule() throws Exception {
        //given
        CreateScheduleRequest request = new CreateScheduleRequest(
                "name",
                LocalDate.now(),
                LocalTime.now(),
                DivisionType.FIX,
                List.of(1L),
                List.of(1),
                Category.CLEANING
        );

        Mockito.when(scheduleService.createSchedule(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(1L);

        //when
        ResultActions resultActions = mockMvc.perform(
                post(API_PREFIX + "/one-time/house/{houseId}", 1L)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").value(1L))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.code").isEmpty());

    }

    @DisplayName("반복 스케줄을 생성할떄 요청 body에 잘못된 값을 주면 실패한다.")
    @WithMockCustomUser
    @ParameterizedTest
    @MethodSource("invalidCreateScheduleRequestProvider")
    void createRepeatScheduleWithInvalidRequest(
            String name,
            LocalDate localDate,
            LocalTime localTime,
            DivisionType divisionType,
            List<Long> memberIds,
            List<Integer> dayIds,
            Category category,
            String expectedMessage
    ) throws Exception {
        //given
        CreateScheduleRequest request = new CreateScheduleRequest(
                name,
                localDate,
                localTime,
                divisionType,
                memberIds,
                dayIds,
                category
        );

        Mockito.when(scheduleService.createSchedule(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(1L);

        //when
        ResultActions resultActions = mockMvc.perform(
                post(API_PREFIX + "/repeat/house/{houseId}", 1L)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value(expectedMessage))
                .andExpect(jsonPath("$.code").isEmpty());

    }

    @DisplayName("일회성 스케줄을 생성할떄 요청 body에 잘못된 값을 주면 실패한다.")
    @WithMockCustomUser
    @ParameterizedTest
    @MethodSource("invalidCreateScheduleRequestProvider")
    void createOneTimeScheduleWithInvalidRequest(
            String name,
            LocalDate localDate,
            LocalTime localTime,
            DivisionType divisionType,
            List<Long> memberIds,
            List<Integer> dayIds,
            Category category,
            String expectedMessage
    ) throws Exception {
        //given
        CreateScheduleRequest request = new CreateScheduleRequest(
                name,
                localDate,
                localTime,
                divisionType,
                memberIds,
                dayIds,
                category
        );

        Mockito.when(scheduleService.createSchedule(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(1L);

        //when
        ResultActions resultActions = mockMvc.perform(
                post(API_PREFIX + "/one-time/house/{houseId}", 1L)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value(expectedMessage))
                .andExpect(jsonPath("$.code").isEmpty());

    }

    private static Stream<Arguments> invalidCreateScheduleRequestProvider() {
        return Stream.of(
                Arguments.of(
                        null,
                        LocalDate.now(),
                        LocalTime.now(),
                        DivisionType.FIX,
                        List.of(1L),
                        List.of(1),
                        Category.CLEANING,
                        "name에 빈 값이 올 수 없습니다."
                ),
                Arguments.of(
                        " ",
                        LocalDate.now(),
                        LocalTime.now(),
                        DivisionType.FIX,
                        List.of(1L),
                        List.of(1),
                        Category.CLEANING,
                        "name에 빈 값이 올 수 없습니다."
                ),
                Arguments.of(
                        "name",
                        null,
                        LocalTime.now(),
                        DivisionType.FIX,
                        List.of(1L),
                        List.of(1),
                        Category.CLEANING,
                        "startDate에 빈 값이 올 수 없습니다."
                ),
                Arguments.of(
                        "name",
                        LocalDate.now(),
                        null,
                        DivisionType.FIX,
                        List.of(1L),
                        List.of(1),
                        Category.CLEANING,
                        "startTime에 빈 값이 올 수 없습니다."
                ),
                Arguments.of(
                        "name",
                        LocalDate.now(),
                        LocalTime.now(),
                        null,
                        List.of(1L),
                        List.of(1),
                        Category.CLEANING,
                        "divisionType에 빈 값이 올 수 없습니다."
                ),
                Arguments.of(
                        "name",
                        LocalDate.now(),
                        LocalTime.now(),
                        DivisionType.FIX,
                        List.of(),
                        List.of(1),
                        Category.CLEANING,
                        "담당자는 최소 1명 최대 30명까지 가능합니다."
                ),
                Arguments.of(
                        "name",
                        LocalDate.now(),
                        LocalTime.now(),
                        DivisionType.FIX,
                        LongStream.rangeClosed(1,31).boxed().toList(),
                        List.of(1),
                        Category.CLEANING,
                        "담당자는 최소 1명 최대 30명까지 가능합니다."
                ),
                Arguments.of(
                        "name",
                        LocalDate.now(),
                        LocalTime.now(),
                        DivisionType.FIX,
                        List.of(1L),
                        List.of(),
                        Category.CLEANING,
                        "Day는 최소 1개 최대 7개까지 가능합니다."
                ),
                Arguments.of(
                        "name",
                        LocalDate.now(),
                        LocalTime.now(),
                        DivisionType.FIX,
                        List.of(1L),
                        IntStream.rangeClosed(1,8).boxed().toList(),
                        Category.CLEANING,
                        "Day는 최소 1개 최대 7개까지 가능합니다."
                ),
                Arguments.of(
                        "name",
                        LocalDate.now(),
                        LocalTime.now(),
                        DivisionType.FIX,
                        LongStream.rangeClosed(1,30).boxed().toList(),
                        IntStream.rangeClosed(1,7).boxed().toList(),
                        null,
                        "category에 빈 값이 올 수 없습니다."
                )
        );
    }

}