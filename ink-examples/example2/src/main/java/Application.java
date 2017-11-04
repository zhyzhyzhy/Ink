import org.ink.NoName;
import org.ink.web.annotation.GET;
import org.ink.web.annotation.PathVariable;
import org.ink.web.http.Response;

import java.util.LinkedList;
import java.util.List;

public class Application {

    @GET("/index/{name}")
    public String getIndex(@PathVariable String name) {
        return name;
    }

    @GET("/persons")
    public Response getPersons() {
        class Person {
            private String name;
            private int age;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getAge() {
                return age;
            }

            public void setAge(int age) {
                this.age = age;
            }

            public Person(String name, int age) {
                this.name = name;
                this.age = age;
            }
        }

        final Person p1 = new Person("zhuyichen", 19);
        final Person p2 = new Person("guigui", 20);
        List<Person> list = new LinkedList<Person>() {{
            add(p1);
            add(p2);
        }};
        return Response.ok().body(list).build();
    }

    public static void main(String[] args) {
        String name = "hello,world";
        String age = "19";
        new NoName(8082, Application.class).start();
    }
}

