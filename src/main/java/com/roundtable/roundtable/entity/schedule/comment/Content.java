package com.roundtable.roundtable.entity.schedule.comment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {

    public static final int MAX_CONTENT_LENGTH = 3000;

    @Column(nullable = false)
    private String content;

    @Builder
    private Content(String content) {
        this.content = content;
    }

    public static Content of(String content) {
        validate(content);
        return Content.builder()
                .content(content)
                .build();
    }

    private static void validate(String content) {
        if(content.isBlank()) {

        }

        if(content.length() > MAX_CONTENT_LENGTH) {

        }
        //validate 실행
    }
}
