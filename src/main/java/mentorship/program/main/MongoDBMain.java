package mentorship.program.main;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

import com.mongodb.WriteResult;
import mentorship.program.config.MongoConfig;
import mentorship.program.model.Article;
import mentorship.program.model.Author;
import mentorship.program.model.Comment;
import mentorship.program.model.Tag;
import mentorship.program.repository.AuthorRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MongoDBMain {
    private static Author author1;
    private static Author curAuthor;
    private static AnnotationConfigApplicationContext ctx;
    private static List<Author> authors = new ArrayList<>();

    static {
        author1 = new Author("author1",Arrays.asList(new Article("article1",Arrays.asList(new Comment("comment1", Tag.SPORT),
                new Comment("comment2", Tag.UKRAINE))),
                new Article("article1",Arrays.asList(new Comment("comment3",Tag.BUSINESS)))));
        author1.setDocumentId(BigInteger.valueOf(1));
        authors.add(author1);

        for (int i = 2; i <= 10; i++) {

            curAuthor = new Author("author" + i,Arrays.asList(new Article("article1",Arrays.asList(new Comment("comment1", Tag.SPORT),
                    new Comment("comment2", Tag.UKRAINE))),
                    new Article("article2",Arrays.asList(new Comment("comment3",Tag.BUSINESS)))));

            authors.add(curAuthor);
        }
    }


    public static void main(String[] args) {
        ctx = new AnnotationConfigApplicationContext(MongoConfig.class);
        startTestWithSpringData();
        startTestWithMongoTemplate();
    }

    private static void startTestWithSpringData(){
        AuthorRepository repo = ctx.getBean(AuthorRepository.class);
        /* Create */
        repo.save(MongoDBMain.author1);
        long count = repo.count();
        System.out.println("Number of entities after create : " + count);
        /* Read */
        Author currentAuthor = repo.findOne(BigInteger.valueOf(1L));
        /* Update */
        currentAuthor.setName("updatedAuthorName");
        repo.save(currentAuthor);
        count = repo.count();
        System.out.println("Number of entities after update : " + count);
        /* Delete */
        currentAuthor = repo.findByName("updatedAuthorName");
        repo.delete(currentAuthor);
        count = repo.count();
        System.out.println("Number of entities after delete : " + count);

    }

    private static void startTestWithMongoTemplate(){
        MongoOperations mongoOps = ctx.getBean(MongoOperations.class);

        if (mongoOps.collectionExists(Author.class)) {
            mongoOps.dropCollection(Author.class);
        }
        /* Create */
        mongoOps.insert(authors,Author.class);
        long numberOfAuthors = mongoOps.count(null,Author.class);

        System.out.println("Number of authors after insert : " + numberOfAuthors);
        /* Read */
        Query articleNameIs2 = query(where("articles.articleName").is("article2"));
        List<Author> authorsThatHaveArticlesName2 = mongoOps.find(articleNameIs2, Author.class);

        System.out.println("Authors that have article name : article2");
        authorsThatHaveArticlesName2.stream().forEach(art -> System.out.println(art.getName()));
        /* Update */
        Update updateArticle2 = update("name","author3NewName");
        mongoOps.updateFirst(articleNameIs2,updateArticle2, Author.class);

        List<Author> allAuthors = mongoOps.findAll(Author.class);
        allAuthors.stream().filter(author -> author.getName().equals("author3NewName")).forEach(System.out::println);
        /* Delete */
        WriteResult result = mongoOps.remove(query(where("name").is("author3NewName")),Author.class);
        allAuthors = mongoOps.findAll(Author.class);

        long numberOfAuthorsAfterDelete = allAuthors.stream().filter(author -> author.getName().equals("author3NewName")).count();
        System.out.println("Number of authors that have name \"author3NewName\" after delete : " + numberOfAuthorsAfterDelete);

    }

}
