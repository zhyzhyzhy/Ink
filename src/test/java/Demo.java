import com.noname.NoName;
import com.noname.NoNameConfigure;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;

/**
 * Created by zhuyichen on 2017/7/12.
 */

public class Demo extends NoNameConfigure {

    @Override
    public String[] beansPackage() {
        return new String[]{"web", "service"};
    }

    @Override
    public String SecurityKey() {
        return "yes";
    }

    public static void main(String[] args) {
        new NoName(8091, Demo.class).start();
    }

}
