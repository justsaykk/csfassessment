package vttp2022.csf.assessment.server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import vttp2022.csf.assessment.server.models.Comment;

@Repository
public class CommentsRepo {

    @Autowired
    private MongoTemplate template;

    public Comment saveComment(Comment comment) {
        return template.insert(comment, "comments");
    }
    

}
