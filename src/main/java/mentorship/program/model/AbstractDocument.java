package mentorship.program.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigInteger;

@Data
public class AbstractDocument {

    @Id private BigInteger documentId;
}
