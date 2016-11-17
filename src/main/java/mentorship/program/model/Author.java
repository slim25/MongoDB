package mentorship.program.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection="authors")
@TypeAlias("author")
@Data
@AllArgsConstructor
public class Author extends AbstractDocument{

    private String name;

    private List<Article> articles;

}
