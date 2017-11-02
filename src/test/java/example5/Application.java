package example5;

import com.noname.NoName;
import com.noname.NoNameConfigure;
import com.noname.aop.annotation.After;

public class Application extends NoNameConfigure {
    @Override
    public String[] beansPackage() {
        return new String[]{"example5"};
    }

    public static void main(String[] args) {
        new NoName(8091, Application.class).start();
    }
}
