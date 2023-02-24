package vttp2022.csf.assessment.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.repositories.CommentsRepo;

@Service
public class CommentService {
    
    @Autowired
    private CommentsRepo commentsRepo;

    public Boolean saveComment(Comment comment) {
        commentsRepo.saveComment(comment);
        return true;
    }
}
