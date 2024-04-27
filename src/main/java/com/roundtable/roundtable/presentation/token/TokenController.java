package com.roundtable.roundtable.presentation.token;

import com.roundtable.roundtable.business.auth.dto.Tokens;
import com.roundtable.roundtable.business.token.TokenService;
import com.roundtable.roundtable.global.response.SuccessResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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

    @PostMapping("/refresh-token")
    public ResponseEntity<SuccessResponse<Tokens>> refreshToken(
            @Valid @RequestBody @NotBlank(message = "refreshToken은 필수입니다.") String refreshToken
    ) {

        return ResponseEntity.ok().body(
                SuccessResponse.from(tokenService.refresh(refreshToken))
        );
    }
}
