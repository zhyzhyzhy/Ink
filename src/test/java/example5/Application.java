package example5;

import org.ink.NoName;
import org.ink.NoNameConfigure;

public class Application extends NoNameConfigure {
    @Override
    public String[] beansPackage() {
        return new String[]{"example5"};
    }

    public static void main(String[] args) {
        new NoName(8091, Application.class).start();
    }
}
