import com.noname.NoNameConfigure;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;

/**
 * Created by zhuyichen on 2017/7/12.
 */

public class Configure implements NoNameConfigure{
    public String[] beansPackage() {
        return new String[]{"web"};
    }
}
