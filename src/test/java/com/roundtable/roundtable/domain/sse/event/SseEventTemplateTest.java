package com.roundtable.roundtable.domain.sse.event;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.domain.sse.event.SseEventTemplateTest.TestSseEventTemplate.TestSseData;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter.DataWithMediaType;

class SseEventTemplateTest {

    @DisplayName("sseEmitter로 전송될 데이터를 만든다.")
    @Test
    void createSendData() {
        //given
        TestSseEventTemplate testSseEvent = new TestSseEventTemplate(new TestSseData("good"));
        SseEventId sseEventId = SseEventId.of(1L, 2L, LocalDateTime.of(2024, 10, 15, 12, 0, 0));

        //when
        Set<DataWithMediaType> data = testSseEvent.createSendData(sseEventId);

        //then
        Iterator<DataWithMediaType> iterator = data.iterator();

        assertThat(iterator.next().getData().toString()).contains("id:1_2_2024-10-15T12:00" , "event:test");

        DataWithMediaType sseData = iterator.next();
        assertThat(sseData.getData().toString()).contains("{\"message\":\"good\"}");
        assertThat(sseData.getMediaType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    static class TestSseEventTemplate extends JsonSseEventTemplate<TestSseData> {

        protected TestSseEventTemplate(TestSseData eventData) {
            super(eventData);
        }

        @Override
        public String getEventName() {
            return "test";
        }

        public record TestSseData(
                String message
        ) {}
    }

}