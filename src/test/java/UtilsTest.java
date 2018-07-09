import org.junit.Test;
import utils.DatabaseUtils;

import java.sql.Connection;

public class UtilsTest {

    @Test
    public void testDatabaseUtil(){
        Connection conn = DatabaseUtils.getConnection();
        System.out.println(conn);
    }
}
