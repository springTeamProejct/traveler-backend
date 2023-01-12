package traveler.travel.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import traveler.travel.domain.account.entity.User;
import traveler.travel.domain.post.entity.Comment;
import traveler.travel.domain.post.entity.Post;
import traveler.travel.domain.post.repository.CommentRepository;
import traveler.travel.global.dto.CommentRequestDto;
import traveler.travel.global.exception.BadRequestException;
import traveler.travel.global.exception.ForbiddenException;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;

    // 댓글 작성
    @Transactional
    public Comment addComment(CommentRequestDto commentDto, User writer, Post post) {
        Comment comment = new Comment(writer, post, commentDto.getContent());
        if (commentDto.getParentId() != null) {
            Comment parent = getComment(commentDto.getParentId());
            comment.setParent(parent);
        }
        return commentRepository.save(comment);
    }

    // 댓글 수정
    @Transactional
    public Comment modifyComment(Long commentId, String content, User user) {
        Comment comment = getComment(commentId);

        if (!isWriter(user, comment)) {
            throw new ForbiddenException("C01");
        }

        comment.update(content);
        return comment;
    }

    // 댓글 삭제
    @Transactional
    public Comment deleteComment(Long commentId, User user) {

        Comment comment = getComment(commentId);

        if (!isWriter(user, comment)) {
            throw new ForbiddenException("C01");
        }

        comment.delete();
        return comment;
    }

    // 댓글 찾기
    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new BadRequestException("C00"));
    }

    public boolean isWriter(User user, Comment comment) {
        if (user == null) return false;
        return comment.getWriter().equals(user);
    }
}
