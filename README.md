**still working on**
------

### Example 


```java
@Controller
public class HelloController {
    @GET("/persons")
    public Response getUsers() {
        return new Response.Builder().entity(new ArrayList<User>() {{
            add(new User("zhuyichen", "hello", "cdscds"));
            add(new User("maoshumin", "hello", "cdscds"));
        }}).status(HttpResponseStatus.OK).build();
    }

    @GET("/index")
    public Response getIndex(@RequestParam String name,
                             @RequestParam String password) {
        return new Response.Builder().entity(name + " " + password).status(HttpResponseStatus.OK).build();
    }

    @POST("/new")
    public Response newPerson(@RequestJson User user) {
        System.out.println(user);
        return new Response.Builder().build();
    }

    public static void main(String[] args) {
        new NoName(8091, HelloController.class).start();
    }
}

```