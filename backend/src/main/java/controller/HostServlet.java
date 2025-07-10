package controller;

import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

import com.google.gson.Gson;

import model.Host;
import dao.HostDao;

@WebServlet("/host")
public class HostServlet extends HttpServlet {
    private HostDao hostDao;
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        hostDao = new HostDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        
        String idParam = request.getParameter("id");
        String emailParam = request.getParameter("email");
        String nameParam = request.getParameter("name");
        

        if (idParam != null) {             //? -- GET by id --
            
            try {
                int id = Integer.parseInt(idParam);
                Host host = hostDao.findById(id);
                
                if (host != null) {
                    response.getWriter().write(gson.toJson(host));
                } else {
                    response.sendError(404, "Host não encontrado");
                }

            } catch (NumberFormatException e) { response.sendError(400, "ID inválido"); }

        } else if (emailParam != null) {   //? -- GET by email (parcial) --
            List<Host> hosts = hostDao.findByEmailLike(emailParam);

            if (hosts != null) response.getWriter().write(gson.toJson(hosts));
            else response.sendError(404, "Host não encontrado");

        } else if (nameParam != null) {    //? -- GET by name --
            List<Host> hosts = hostDao.findByName(nameParam);
            response.getWriter().write(gson.toJson(hosts));
        } 
        else {                             //? -- GET all --
            List<Host> hosts = hostDao.findAll();
            response.getWriter().write(gson.toJson(hosts));
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("[HostServlet] Iniciando criação de host...");
        
        BufferedReader reader = request.getReader();
        String jsonBody = reader.lines().collect(java.util.stream.Collectors.joining());
        System.out.println("[HostServlet] JSON recebido: " + jsonBody);
        
        //* Host já vem com a senha encriptada do servlet
        Host host = gson.fromJson(jsonBody, Host.class);

        System.out.println("[HostServlet] Host parseado: " + (host != null ? host.getName() + " (" + host.getEmail() + ")" : "null"));

        //? -- Exeptions --

        if (host == null || host.getEmail() == null || host.getPassword() == null || host.getName() == null) {
            System.out.println("[HostServlet] Dados inválidos - host: " + (host != null) + 
                             ", email: " + (host != null ? host.getEmail() : "null") + 
                             ", password: " + (host != null ? (host.getPassword() != null ? "presente" : "null") : "null") + 
                             ", name: " + (host != null ? host.getName() : "null"));
            response.sendError(400, "Dados inválidos");
            return;
        }

        System.out.println("[HostServlet] Verificando se email já existe: " + host.getEmail());
        Host existing = hostDao.findByEmail(host.getEmail());
        if (existing != null) {
            System.out.println("[HostServlet] Email já cadastrado: " + host.getEmail());
            response.sendError(409, "Email já cadastrado");
            return;
        }

        //? -- Create Host --
        System.out.println("[HostServlet] Inserindo novo host...");
        try {
            hostDao.insert(host);
            System.out.println("[HostServlet] Host criado com sucesso - ID: " + host.getId());
            response.setContentType("application/json");
            response.getWriter().write("{\"success\":true,\"message\":\"Host criado com sucesso\"}");
        } catch (Exception e) {
            System.err.println("[HostServlet] Erro ao criar host: " + e.getMessage());
            e.printStackTrace();
            response.sendError(500, "Erro interno ao criar host: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        
        Host host = gson.fromJson(reader, Host.class);
        
        if (host == null || host.getId() == 0) { response.sendError(400, "Host inválido"); return; }
        
        jakarta.servlet.http.HttpSession session = request.getSession(false);
        boolean isCurrentUser = false;
        
        if (session != null) {
            Host currentHost = (Host) session.getAttribute("host");
            if (currentHost != null && currentHost.getId() == host.getId()) {
                isCurrentUser = true;
                System.out.println("[HostServlet] Atualizando host logado, atualizando cookies...");
                

                //? -- Cookies --
                jakarta.servlet.http.Cookie fontColor = new jakarta.servlet.http.Cookie("font_color_" + host.getId(), host.getFontColor());
                jakarta.servlet.http.Cookie bgColor = new jakarta.servlet.http.Cookie("background_color_" + host.getId(), host.getBackgroundColor());
                
                fontColor.setPath("/");
                bgColor.setPath("/");
                fontColor.setMaxAge(365 * 24 * 60 * 60); //* 1 ano
                bgColor.setMaxAge(365 * 24 * 60 * 60); //* 1 ano
                response.addCookie(fontColor);
                response.addCookie(bgColor);
                
                System.out.println("[HostServlet] Cookies atualizados - Font: " + host.getFontColor() + ", Background: " + host.getBackgroundColor());
            }
        }
        
        hostDao.update(host);
        response.setContentType("application/json");
        
        String jsonResponse = "{\"success\":true,\"isCurrentUser\":" + isCurrentUser + ",\"fontColor\":\"" + host.getFontColor() + "\",\"backgroundColor\":\"" + host.getBackgroundColor() + "\"}";
        response.getWriter().write(jsonResponse);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        
        if (idParam == null) { response.sendError(400, "ID não informado"); return; }
        
        try {
            int id = Integer.parseInt(idParam);
            hostDao.delete(id);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\":true}");
        } 
        catch (NumberFormatException e) {
            response.sendError(400, "ID inválido");
        }

    }

}