import com.noname.NoNameConfigure;

/**
 * Created by zhuyichen on 2017/7/12.
 */

public class Configure implements NoNameConfigure{
    public String[] beansPackage() {
        return new String[]{"web"};
    }

}
