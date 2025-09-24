package com.dprol.post_service.service;

import com.dprol.post_service.dto.CommentDto;
import com.dprol.post_service.entity.Comment;
import com.dprol.post_service.exception.CommentNotFoundException;
import com.dprol.post_service.kafka.event.Status;
import com.dprol.post_service.kafka.producer.CommentProducer;
import com.dprol.post_service.mapper.CommentMapper;
import com.dprol.post_service.repository.CommentRepository;
import com.dprol.post_service.service.comment.CommentServiceImpl;
import com.dprol.post_service.validator.comment.CommentValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private CommentValidator commentValidator;
    @Mock
    private CommentProducer commentProducer;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {

        comment = new Comment();
        comment.setId(1L);
        comment.setAuthorId(100L);
        comment.setContent("test comment");

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setAuthorId(100L);
        commentDto.setContent("test comment");
    }

    @Test
    void createComment_ShouldSaveAndSendKafka() {
        when(commentMapper.toEntity(commentDto)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        CommentDto result = commentService.createComment(commentDto);

        verify(commentValidator).validateCommentById(1L);
        verify(commentRepository).save(comment);
        verify(commentProducer).produce(any());
        assertEquals(commentDto, result);
    }

    @Test
    void deleteComment_ShouldDeleteAndSendKafka() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        commentService.deleteComment(1L);

        verify(commentValidator).validateCommentById(1L);
        verify(commentRepository).delete(comment);
        verify(commentProducer).produce(any());
    }

    @Test
    void updateComment_ShouldValidateAndSendKafka() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        CommentDto result = commentService.updateComment(1L);

        verify(commentValidator).validateCommentByVerificationStatus(comment);
        verify(commentRepository).save(comment);
        verify(commentProducer).produce(any());
        assertEquals(commentDto, result);
    }

    @Test
    void getListCommentByUserId_ShouldReturnDtos() {
        when(commentRepository.getListCommentByUserId(100L)).thenReturn(List.of(comment));
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        List<CommentDto> result = commentService.getListCommentByUserId(100L);

        assertEquals(1, result.size());
        assertEquals(commentDto, result.get(0));
    }

    @Test
    void findCommentById_ShouldReturnComment() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        Comment result = commentService.findCommentById(1L);

        assertEquals(comment, result);
    }

    @Test
    void findCommentById_ShouldThrow_WhenNotFound() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.findCommentById(1L));
    }

    @Test
    void sendToKafka_ShouldNotSend_WhenAuthorIdNull() {
        Comment noAuthor = new Comment();
        noAuthor.setId(2L);
        noAuthor.setAuthorId(null);

        // приватный метод проверим через публичный createComment
        when(commentMapper.toEntity(any())).thenReturn(noAuthor);
        when(commentMapper.toDto(noAuthor)).thenReturn(new CommentDto());

        CommentDto dto = new CommentDto();
        dto.setId(2L);

        commentService.createComment(dto);

        verify(commentProducer, never()).produce(any());
    }
}
