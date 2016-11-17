package mentorship.program.repository;

import mentorship.program.model.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

public interface AuthorRepository extends CrudRepository<Author,BigInteger>{

    public Author findByName(String name);

}
