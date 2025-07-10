package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("[LogoutServlet] Iniciando logout...");
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            System.out.println("[LogoutServlet] Invalidando sessão: " + session.getId());
            session.invalidate();
        }
        
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (jakarta.servlet.http.Cookie cookie : cookies) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    System.out.println("[LogoutServlet] Cookie JSESSIONID removido");
                }
            }
        }
        
        response.setContentType("application/json");
        response.getWriter().write("{\"success\":true,\"message\":\"Logout realizado com sucesso\"}");
        
        System.out.println("[LogoutServlet] Logout concluído");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }
} 