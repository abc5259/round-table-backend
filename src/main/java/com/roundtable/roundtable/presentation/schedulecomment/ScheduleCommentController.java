package com.roundtable.roundtable.presentation.schedulecomment;

import com.roundtable.roundtable.business.schdulecomment.ScheduleCommentService;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.global.response.ApiResponse;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.presentation.argumentresolver.Login;
import com.roundtable.roundtable.presentation.schedulecomment.request.CreateScheduleCommentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule-comment")
@RequiredArgsConstructor
public class ScheduleCommentController {

    private final ScheduleCommentService scheduleCommentService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createScheduleComment(
            @Login Member member,
            @Valid @RequestBody CreateScheduleCommentRequest createScheduleCommentRequest
            ) {
        return ResponseEntity.ok(
                SuccessResponse.from(
                        scheduleCommentService.createScheduleComment(
                                createScheduleCommentRequest.toCreateScheduleCommentDto(member.getId())
                        )
                )
        );
    }
}
