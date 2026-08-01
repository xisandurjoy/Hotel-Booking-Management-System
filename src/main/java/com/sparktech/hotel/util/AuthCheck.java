package com.sparktech.hotel.util;

import jakarta.servlet.http.HttpSession;

// Plain Java check - each controller method calls this itself instead of
// relying on a Spring HandlerInterceptor running behind the scenes.
public class AuthCheck {

    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loggedInUser") != null;
    }
}
