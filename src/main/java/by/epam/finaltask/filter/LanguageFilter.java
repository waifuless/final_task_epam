package by.epam.finaltask.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

@WebFilter(filterName = "LanguageFilter")
public class LanguageFilter implements Filter {

    private final static Logger LOG = LoggerFactory.getLogger(LanguageFilter.class);

    private static final String LANGUAGE_COOKIE_NAME = "lang";
    private static final String DEFAULT_LANGUAGE = "ru_RU";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        LOG.debug("In lang filter");
        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        if (cookies == null || Arrays.stream(cookies).noneMatch(this::isLanguageCookie)) {
            LOG.debug("lang cookie not found");
            addDefaultLanguageCookie((HttpServletResponse) response);
        }
        chain.doFilter(request, response);
    }

    private boolean isLanguageCookie(Cookie cookie) {
        return cookie.getName().equals(LANGUAGE_COOKIE_NAME);
    }

    private void addDefaultLanguageCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(LANGUAGE_COOKIE_NAME, DEFAULT_LANGUAGE);
        response.addCookie(cookie);
    }
}
