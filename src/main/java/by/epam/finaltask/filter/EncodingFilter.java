package by.epam.finaltask.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter(filterName = "EncodingFilter")
public class EncodingFilter implements Filter {

    private final static String COMMON_ENCODING = "UTF-8";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        String currentEncoding = request.getCharacterEncoding();
        if(!COMMON_ENCODING.equalsIgnoreCase(currentEncoding)){
            request.setCharacterEncoding(COMMON_ENCODING);
            response.setCharacterEncoding(COMMON_ENCODING);
        }
        chain.doFilter(request, response);
    }
}
