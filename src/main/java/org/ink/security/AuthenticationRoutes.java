package org.ink.security;

import org.ink.web.annotation.Controller;
import org.ink.web.annotation.POST;
import org.ink.web.annotation.RequestParam;
import org.ink.web.http.Response;

import java.util.Collections;

@Controller
public class AuthenticationRoutes {

    @POST("/auth")
    public Response auth(@RequestParam String userName, @RequestParam String password) {
        User user = org.ink.security.SecurityManager.login(userName, password);
        if (user == null) {
           return Response.badRequest().body(Collections.singletonMap("message", "wrong password or user is not exist")).build();
        }
        else {
            return Response.ok().body(JwtUtil.generateToken(user)).build();
        }
    }
}
