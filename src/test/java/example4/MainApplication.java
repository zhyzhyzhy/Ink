package example4;

import org.ink.NoName;
import org.ink.NoNameConfigure;

public class MainApplication extends NoNameConfigure{

    public static void main(String[] args) {
        new NoName(8091, MainApplication.class).start();
    }

    @Override
    public String[] beansPackage() {
        return new String[]{"example4"};
    }

}
