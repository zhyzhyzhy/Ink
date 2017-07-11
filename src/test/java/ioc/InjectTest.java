package ioc;

import com.noname.ioc.annotation.ComponentScan;
import com.noname.ioc.context.AnnotationApplicationContext;
import com.noname.ioc.context.ApplicationContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by zhuyichen on 2017/7/11.
 */
@RunWith(JUnit4.class)
public class InjectTest {
    @Test
    public void IocTest() {
        ApplicationContext applicationContext = new AnnotationApplicationContext(Configuration.class);
        Boy boy = applicationContext.getBean(Boy.class);
        Girl girl = applicationContext.getBean(Girl.class);
        if (boy.getNioEventLoopGroup() == null) {
            System.out.println("cdscdscds");
        }
        System.out.println(girl.getBoy());
        System.out.println(boy.getGirl());
    }
    @Test
    public void newInstance() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        Class.forName("ioc.Boy").newInstance();
    }

}

@ComponentScan("ioc")
class Configuration {

}


