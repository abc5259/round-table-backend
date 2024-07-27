package com.roundtable.roundtable.business.chore.unit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.roundtable.roundtable.business.chore.ChoreReader;
import com.roundtable.roundtable.business.chore.ChoreService;
import com.roundtable.roundtable.business.chore.ChoreValidator;
import com.roundtable.roundtable.business.chore.event.ChoreCompleteEvent;
import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.image.ImageUploader;
import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.global.exception.ChoreException.AlreadyCompletedException;
import com.roundtable.roundtable.global.exception.CoreException;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.errorcode.ImageErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class ChoreServiceUnitTest {

    @Mock
    private ChoreReader choreReader;

    @Mock
    private ChoreValidator choreValidator;

    @Mock
    private ImageUploader imageUploader;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ChoreService choreService;

    @DisplayName("집안을을 완료할 수 있다.")
    @Test
    void completeChore() {
        //given

        Long memberId = 1L;
        Long choreId = 1L;
        long houseId = 1L;
        Chore chore = Chore.builder().id(choreId).isCompleted(false).build();
        given(choreReader.readById(choreId)).willReturn(chore);

        MultipartFile file = new MockMultipartFile("file", "test.png", "image/png", new byte[]{});
        String imageUrl = "imageUrl";
        given(imageUploader.upload(file)).willReturn(imageUrl);

        AuthMember authMember = new AuthMember(memberId, houseId);

        //when
        choreService.completeChore(authMember, choreId, file);

        //then
        assertThat(chore)
                .extracting("isCompleted", "completedImageUrl")
                .contains(true, imageUrl);
    }

    @DisplayName("집안일을 완료하면 집안일 완료 이벤트를 발행한다.")
    @Test
    void completeChore_publishEvent() {
        //given
        Long memberId = 1L;
        Long choreId = 1L;
        long houseId = 1L;
        Chore chore = Chore.builder().id(choreId).isCompleted(false).build();
        given(choreReader.readById(choreId)).willReturn(chore);

        MultipartFile file = new MockMultipartFile("file", "test.png", "image/png", new byte[]{});
        String imageUrl = "imageUrl";
        given(imageUploader.upload(file)).willReturn(imageUrl);

        AuthMember authMember = new AuthMember(memberId, houseId);

        //when
        choreService.completeChore(authMember, choreId, file);

        //then
        verify(eventPublisher).publishEvent(any(ChoreCompleteEvent.class));
    }

    @DisplayName("집안을을 완료할떄 해당 집안일을 담당하는 멤버가 아니라면 에러를 던진다.")
    @Test
    void completeChoreWithNoAssignedChoreMember() {
        //given
        Long memberId = 1L;
        Long choreId = 1L;
        MultipartFile file = new MockMultipartFile("file", "test.png", "image/png", new byte[]{});
        AuthMember authMember = new AuthMember(memberId);

        doThrow(new NotFoundEntityException()).when(choreValidator).validateChoreAssignedToMember(choreId, memberId);

        //when //then
        assertThatThrownBy(() -> choreService.completeChore(authMember, choreId, file))
                .isInstanceOf(NotFoundEntityException.class);
    }

    @DisplayName("집안일을 완료할때 choreId에 해당하는 chore이 존재하지 않을때 에러를 던진다.")
    @Test
    void completeChoreWithNoChoreId() {
        //given
        Long memberId = 1L;
        Long choreId = 1L;
        MultipartFile file = new MockMultipartFile("file", "test.png", "image/png", new byte[]{});
        AuthMember authMember = new AuthMember(memberId);

        doThrow(new NotFoundEntityException()).when(choreReader).readById(choreId);

        //when //then
        assertThatThrownBy(() -> choreService.completeChore(authMember, choreId, file))
                .isInstanceOf(NotFoundEntityException.class);

    }

    @DisplayName("집안일을 완료할때 이미 완료된 chore이라면 에러를 던진다.")
    @Test
    void completeChoreWithAlreadyCompletedChore() {
        //given
        Long memberId = 1L;
        Long choreId = 1L;
        Chore chore = Chore.builder().id(choreId).isCompleted(true).build();
        MultipartFile file = new MockMultipartFile("file", "test.png", "image/png", new byte[]{});
        String imageUrl = "imageUrl";
        AuthMember authMember = new AuthMember(memberId);

        given(choreReader.readById(choreId)).willReturn(chore);
        doThrow(new AlreadyCompletedException()).when(choreValidator).validateCompleteChore(chore);

        //when //then
        assertThatThrownBy(() -> choreService.completeChore(authMember, choreId, file))
                .isInstanceOf(AlreadyCompletedException.class);

    }

    @DisplayName("집안일을 완료할때 완료 이미지 업로드에 문제가 생기면 에러를 던진다.")
    @Test
    void completeChoreWithProblemImageUpload() {
        //given
        Long memberId = 1L;
        Long choreId = 1L;
        Chore chore = Chore.builder().id(choreId).isCompleted(true).build();
        MultipartFile file = new MockMultipartFile("file", "test.png", "image/png", new byte[]{});
        AuthMember authMember = new AuthMember(memberId);

        given(choreReader.readById(choreId)).willReturn(chore);
        doThrow(new CoreException(ImageErrorCode.UPLOAD_ERROR)).when(imageUploader).upload(file);

        //when //then
        assertThatThrownBy(() -> choreService.completeChore(authMember, choreId, file))
                .isInstanceOf(CoreException.class)
                .hasMessage(ImageErrorCode.UPLOAD_ERROR.getMessage());
    }


}
