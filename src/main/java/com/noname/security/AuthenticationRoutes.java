package com.noname.security;

import com.noname.web.annotation.Controller;
import com.noname.web.annotation.POST;
import com.noname.web.annotation.RequestParam;
import com.noname.web.http.Response;

import java.util.Collections;

@Controller
public class AuthenticationRoutes {

    @POST("/auth")
    public Response auth(@RequestParam String userName, @RequestParam String password) {
        User user = SecurityManager.login(userName, password);
        if (user == null) {
           return Response.badRequest().body(Collections.singletonMap("message", "wrong password or user is not exist")).build();
        }
        else {
            return Response.ok().body(JwtUtil.generateToken(user)).build();
        }
    }
}
