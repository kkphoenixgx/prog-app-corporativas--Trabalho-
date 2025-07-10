package controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI();
        
        System.out.println("[AuthFilter] Verificando rota: " + path);

        if (path.endsWith("login-form.jsp") || path.endsWith("subscribe-form.jsp") ||
            path.endsWith("credentials") || path.contains("/assets/") ||
            path.contains("/js/") || path.contains("/styles/") ||
            path.endsWith("index.html") || path.endsWith("favicon.ico") ||
            path.endsWith("random-quiz.jsp") || path.contains("/random-quiz") ||
            path.endsWith("quiz-json") || path.endsWith("calc-score") ||
            path.endsWith("test-auth.jsp") || path.endsWith("test-colors.jsp")) {
            System.out.println("[AuthFilter] Rota pública liberada: " + path);
            chain.doFilter(req, res);
            
            return;
        }

        HttpSession session = request.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("token") != null);
        System.out.println("[AuthFilter] Logado: " + loggedIn + ", Session: " + (session != null) + ", Token: " + (session != null ? session.getAttribute("token") : "null"));
        
        //? Controle rotas
        if (!loggedIn) {
            System.out.println("[AuthFilter] Acesso negado para: " + path);
            String accept = request.getHeader("Accept");

            if (accept != null && accept.contains("application/json")) {
                response.setStatus(401);
                response.getWriter().write("{\"error\":\"Não autorizado\"}");
            } else {
                response.sendRedirect("login-form.jsp");
            }
            return;
        }
        
        System.out.println("[AuthFilter] Acesso permitido para: " + path);
        
        chain.doFilter(req, res);
    }
} 