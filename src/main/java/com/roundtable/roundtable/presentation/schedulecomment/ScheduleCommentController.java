package com.roundtable.roundtable.presentation.schedulecomment;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.common.CursorBasedResponse;
import com.roundtable.roundtable.business.schedulecomment.ScheduleCommentService;
import com.roundtable.roundtable.domain.schedulecomment.dto.ScheduleCommentDetailDto;
import com.roundtable.roundtable.global.response.ResponseDto;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.global.support.annotation.Login;
import com.roundtable.roundtable.presentation.common.request.CursorBasedPaginationRequest;
import com.roundtable.roundtable.presentation.schedulecomment.request.CreateScheduleCommentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "스케줄에 대한 댓글 생성", description = "특정 스케줄에 대한 댓글을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "성공")
    @PostMapping("/house/{houseId}")
    public ResponseEntity<ResponseDto<Long>> createScheduleComment(
            @Login AuthMember authMember,
            @PathVariable Long houseId,
            @Valid @RequestBody CreateScheduleCommentRequest createScheduleCommentRequest
            ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                SuccessResponse.from(
                        scheduleCommentService.createScheduleComment(
                                authMember.toHouseAuthMember(houseId),
                                createScheduleCommentRequest.toCreateScheduleCommentDto()
                        )
                )
        );
    }

    @Operation(summary = "특정 스케줄에 대한 댓글 조회", description = "스케줄에 대한 댓글을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/schedule/{scheduleId}/house/{houseId}")
    public ResponseEntity<ResponseDto<CursorBasedResponse<List<ScheduleCommentDetailDto>>>> findCommentsByScheduleId(
            @Login AuthMember authMember,
            @PathVariable Long scheduleId,
            @PathVariable Long houseId,
            @ModelAttribute CursorBasedPaginationRequest cursorBasedPaginationRequest
            ) {
        return ResponseEntity.ok(
                SuccessResponse.from(
                        scheduleCommentService.findByScheduleId(authMember.toHouseAuthMember(houseId), scheduleId, cursorBasedPaginationRequest.toCursorBasedRequest())
                )
        );
    }
}
