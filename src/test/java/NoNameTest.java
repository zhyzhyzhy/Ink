import com.noname.NoName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by zhuyichen on 2017/7/11.
 */
@RunWith(JUnit4.class)
public class NoNameTest {
    @Test
    public void start() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        NoName.NoName(8091).start();
    }
}
