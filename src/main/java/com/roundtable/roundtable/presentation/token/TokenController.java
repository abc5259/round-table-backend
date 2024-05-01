package com.roundtable.roundtable.presentation.token;

import com.roundtable.roundtable.business.auth.dto.Tokens;
import com.roundtable.roundtable.business.token.TokenService;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.presentation.token.request.RefreshRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/refresh")
    public ResponseEntity<SuccessResponse<Tokens>> refreshToken(
            @Valid @RequestBody RefreshRequest refreshRequest
            ) {
        return ResponseEntity.ok().body(
                SuccessResponse.from(tokenService.refresh(refreshRequest.refreshToken()))
        );
    }
}