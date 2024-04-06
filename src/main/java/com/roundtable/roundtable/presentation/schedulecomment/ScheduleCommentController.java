package com.roundtable.roundtable.presentation.schedulecomment;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.common.CursorBasedRequest;
import com.roundtable.roundtable.business.common.CursorBasedResponse;
import com.roundtable.roundtable.business.schedulecomment.ScheduleCommentService;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedulecomment.dto.ScheduleCommentDetailDto;
import com.roundtable.roundtable.global.response.ApiResponse;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.presentation.argumentresolver.Login;
import com.roundtable.roundtable.presentation.common.request.CursorBasedPaginationRequest;
import com.roundtable.roundtable.presentation.schedulecomment.request.CreateScheduleCommentRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
            @Login AuthMember authMember,
            @Valid @RequestBody CreateScheduleCommentRequest createScheduleCommentRequest
            ) {
        return ResponseEntity.ok(
                SuccessResponse.from(
                        scheduleCommentService.createScheduleComment(
                                createScheduleCommentRequest.toCreateScheduleCommentDto(authMember.memberId())
                        )
                )
        );
    }

    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<ApiResponse<CursorBasedResponse<List<ScheduleCommentDetailDto>>>> findCommentsByScheduleId(
            @Login AuthMember authMember,
            @PathVariable Long scheduleId,
            @ModelAttribute CursorBasedPaginationRequest cursorBasedPaginationRequest
            ) {
        return ResponseEntity.ok(
                SuccessResponse.from(
                        scheduleCommentService.findByScheduleId(authMember, scheduleId, cursorBasedPaginationRequest.toCursorBasedRequest())
                )
        );
    }
}
