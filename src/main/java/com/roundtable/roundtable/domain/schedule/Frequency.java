package com.roundtable.roundtable.domain.schedule;

import static com.roundtable.roundtable.domain.schedule.FrequencyType.*;
import static com.roundtable.roundtable.global.exception.errorcode.ScheduleErrorCode.FREQUENCY_NOT_SUPPORT;

import com.roundtable.roundtable.global.exception.CoreException.CreateEntityException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Frequency {

    public static final int ONCE_INTERVAL = 0;

    public static final int MINIMUM_DAILY_INTERVAL = 1;

    public static final int MINIMUM_WEEKLY_INTERVAL = 1;

    public static final int MAX_WEEKLY_INTERVAL = 7;

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

        frequencyInterval = convertFrequencyInterval(frequencyType, frequencyInterval);

        return new Frequency(frequencyType, frequencyInterval);

    }

    public static boolean isSupport(FrequencyType frequencyType, Integer frequencyInterval) {
        if(frequencyInterval < 0)
            return false;

        if(frequencyType.equals(ONCE)) {
            return frequencyInterval == ONCE_INTERVAL;
        }

        if(frequencyType.equals(DAILY)) {
            return frequencyInterval >= MINIMUM_DAILY_INTERVAL;
        }

        if(frequencyType.equals(WEEKLY)) {
            return frequencyInterval >= MINIMUM_WEEKLY_INTERVAL && frequencyInterval <= MAX_WEEKLY_INTERVAL;
        }

        return true;
    }

    private static Integer convertFrequencyInterval(FrequencyType frequencyType, Integer frequencyInterval) {
        if(frequencyType.equals(WEEKLY)) {
            frequencyInterval += 1;
            if(frequencyInterval == 8) frequencyInterval = 1;
        }
        return frequencyInterval;
    }
}

