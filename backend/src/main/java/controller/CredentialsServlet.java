package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;

import java.util.UUID;

import model.Host;
import dao.HostDao;
import utils.Encrypt;

@WebServlet("/credentials")
public class CredentialsServlet extends HttpServlet {
  
  private HostDao hostDao;
  private Gson gson = new Gson();

  @Override
  public void init() throws ServletException {
    hostDao = new HostDao();
  }

  //? ----------- Classes auxiliares -----------

  private static class HostJson {
    String name;
    String email;
    String password;
    String fontColor;
    String backgroundColor;
  }
  private static class LoginJson {
    String email;
    String password;
  }

  //? ----------- Endpoint-----------
  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String op = request.getParameter("op");
    
    if (op == null) op = "login";
    
    if (op.equals("login")) handleLogin(request, response);
    else if (op.equals("subscribe")) handleSubscribe(request, response);
    else response.sendError(400, "Operação inválida");
  }

  //? ----------- Login / Subscribe -----------
  /** 
   //** Primeiro deve fazer subscribe 
   //** Não necessariamente fazer subscribe significa querer login 
  **/

  private void handleSubscribe(HttpServletRequest request, HttpServletResponse response) throws IOException {
    BufferedReader reader = request.getReader();
    HostJson hostJson = gson.fromJson(reader, HostJson.class);

    response.setContentType("application/json");

    //? -- Exeptions --
    if (hostJson == null || hostJson.email == null || hostJson.password == null || hostJson.name == null) {
      response.setStatus(400);
      response.getWriter().write("{\"success\":false,\"error\":\"Dados inválidos\"}");
      return;
    }

    Host existing = hostDao.findByEmail(hostJson.email);
    if (existing != null) {
      response.setStatus(409);
      response.getWriter().write("{\"success\":false,\"error\":\"Email já cadastrado\"}");
      return;
    }

    //? -- Create Host --
    Host host = new Host( 
      0, //* auto generated id
      hostJson.name, 
      hostJson.email, 
      hostJson.password,
      hostJson.fontColor != null ? hostJson.fontColor : "#000000",
      hostJson.backgroundColor != null ? hostJson.backgroundColor : "#FFFFFF"
    );

    //? -- Post Host --
    try {
      hostDao.insert(host);

      System.out.println("[CredentialsServlet] Host cadastrado com sucesso: " + host.getName() + " (ID: " + host.getId() + ")");
      response.getWriter().write("{\"success\":true}");
    } 
    catch (Exception e) {
      System.err.println("[CredentialsServlet] Erro ao cadastrar host: " + e.getMessage());
      response.setStatus(500);
      response.getWriter().write("{\"success\":false,\"error\":\"Erro ao cadastrar usuário\"}");
    }
  }

  //* Cria os cookies, o token e a session
  private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
    BufferedReader reader = request.getReader();
    LoginJson loginJson = gson.fromJson(reader, LoginJson.class);
    
    response.setContentType("application/json");
    
    //? Forced cache refresh
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Expires", "0");
    
    if (loginJson == null || loginJson.email == null || loginJson.password == null) {
      response.setStatus(400);
      response.getWriter().write("{\"success\":false,\"error\":\"Dados inválidos\"}");
      return;
    }

    //? ----------- Verificação Login e Cookie -----------
    Host host = hostDao.findByEmail(loginJson.email);
    if (host != null && utils.Encrypt.compare(loginJson.password, host.getPassword())) {
      String token = java.util.UUID.randomUUID().toString();
      
      // ----------- Session + token -----------
      request.getSession().setAttribute("token", token);
      request.getSession().setAttribute("host", host);
      
      System.out.println("[CredentialsServlet] Login bem-sucedido para host ID: " + host.getId());
      System.out.println("[CredentialsServlet] Token gerado: " + token);
      
      // ----------- Cookie -----------
      jakarta.servlet.http.Cookie fontColor = new jakarta.servlet.http.Cookie("font_color_" + host.getId(), host.getFontColor());
      jakarta.servlet.http.Cookie bgColor = new jakarta.servlet.http.Cookie("background_color_" + host.getId(), host.getBackgroundColor());
      
      fontColor.setPath("/");
      bgColor.setPath("/");
      
      fontColor.setMaxAge(365 * 24 * 60 * 60); //* 1 ano
      bgColor.setMaxAge(365 * 24 * 60 * 60); //* 1 ano
      
      response.addCookie(fontColor);
      response.addCookie(bgColor);
      
      System.out.println("[CredentialsServlet] Cookies definidos - Font: " + host.getFontColor() + ", Background: " + host.getBackgroundColor());
      
      String jsonResponse = "{\"token\":\"" + token + "\",\"success\":true,\"hostId\":" + host.getId() + "}";
      System.out.println("[CredentialsServlet] Resposta JSON: " + jsonResponse);
      response.getWriter().write(jsonResponse);
    
    } 
    else {
      response.setStatus(401);
      response.getWriter().write("{\"success\":false,\"error\":\"Email ou senha inválidos\"}");
    }
  }

}
