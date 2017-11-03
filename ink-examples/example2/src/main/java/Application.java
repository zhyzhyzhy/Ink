import org.ink.NoName;
import org.ink.web.annotation.GET;
import org.ink.web.annotation.PathVariable;

public class Application {

    @GET("/index/{name}")
    public String getIndex(@PathVariable String name) {
        return name;
    }

    public static void main(String[] args) {
        new NoName(8082, Application.class).start();
    }
}
