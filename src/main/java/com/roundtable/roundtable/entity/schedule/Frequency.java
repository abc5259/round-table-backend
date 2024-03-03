package com.roundtable.roundtable.entity.schedule;

import static com.roundtable.roundtable.entity.schedule.FrequencyType.*;
import static com.roundtable.roundtable.global.exception.errorcode.ScheduleErrorCode.*;

import com.roundtable.roundtable.global.exception.CoreException.CreateEntityException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Frequency {
    @Enumerated(EnumType.STRING)
    private FrequencyType frequencyType;

    @Column(nullable = false)
    private Integer frequencyInterval;

    @Builder
    private Frequency(FrequencyType frequencyType, Integer frequencyInterval) {
        this.frequencyType = frequencyType;
        this.frequencyInterval = frequencyInterval;
    }

    public static Frequency of(FrequencyType frequencyType, Integer frequencyInterval) {

        if(!isSupport(frequencyType, frequencyInterval)) {
            throw new CreateEntityException(FREQUENCY_NOT_SUPPORT);
        }

        return new Frequency(frequencyType, frequencyInterval);

    }

    public static boolean isSupport(FrequencyType frequencyType, Integer frequencyInterval) {
        if(frequencyInterval < 0)
            return false;

        if(frequencyType.equals(ONCE)) {
            return frequencyInterval == 0;
        }

        if(frequencyType.equals(DAILY)) {
            return frequencyInterval >= 1;
        }

        if(frequencyType.equals(WEEKLY)) {
            return frequencyInterval >= 1 && frequencyInterval <= 7;
        }

        return true;
    }
}

