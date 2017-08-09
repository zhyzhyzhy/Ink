package example4;

import com.noname.NoName;
import com.noname.NoNameConfigure;
import com.noname.ioc.annotation.Inject;
import com.noname.web.annotation.*;

public class MainApplication extends NoNameConfigure{

    public static void main(String[] args) {
        new NoName(8091, MainApplication.class).start();
    }

    @Override
    public String[] beansPackage() {
        return new String[]{"example4"};
    }

}
