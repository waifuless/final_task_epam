import by.epam.finaltask.model.AuctionType;
import by.epam.finaltask.security.PasswordEncoder;
import org.junit.jupiter.api.Test;

public class SecTest {

    @Test
    public void testSome(){
        System.out.println(AuctionType.valueOf("popa"));
    }
}
