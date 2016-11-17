package mentorship.program.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@AllArgsConstructor
@Document(collection="articles")
public class Article{
    private String articleName;
    private List<Comment> comments;

}
