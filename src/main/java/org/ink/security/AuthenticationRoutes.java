package org.ink.security;

import org.ink.security.user.User;
import org.ink.web.WebContext;
import org.ink.web.annotation.Controller;
import org.ink.web.annotation.POST;
import org.ink.web.annotation.RequestParam;
import org.ink.web.http.Response;

import java.util.Collections;

@Controller
public class AuthenticationRoutes {

    @POST("/login")
    public Response login(@RequestParam String userName, @RequestParam String password) {
        User user = SecurityManager.login(userName, password);
        if (user == null) {
           return Response.badRequest().body(Collections.singletonMap("message", "wrong password or user is not exist")).build();
        }
        else {
            //set into session
            WebContext.currentSession().setUser(user);
            return Response.ok().build();
        }
    }
}
