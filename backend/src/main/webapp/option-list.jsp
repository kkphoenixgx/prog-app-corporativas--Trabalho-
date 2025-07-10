<%@ page import="java.util.List" %>
<%@ page import="model.Quiz" %>
<%@ page import="model.Question" %>
<%@ page import="model.Option" %>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <link rel="icon" type="image/png" href="./assets/icon.png">

        <link rel="stylesheet" href="./styles/index.css">
        
        <title>Random Quiz</title>
      </head>
      <body>
        <header class="main-header"> 
          <div class="header-content">
          <div class="hamburguer-menu" id="btn-hamburguer-menu">
            <span></span>
            <span></span>
            <span></span>
          </div>
          <a class="header-a" href="/backend/index.html">
            <h1>Random Quiz</h1>
          </a>
          </div>
          <nav class="container-menu" id="container-menu" >
            <div class="container-menu-center">
              <button class="btn-close-menu btn-default" id="btn-close-menu" >Fechar</button>
            </div>
            <a href="/backend/quiz" class="btn-modern">Ver Quizzes</a>
            <a href="/backend/quiz-form.jsp" class="btn-modern">Criar Quiz</a>
            <a href="/backend/login-form.jsp" class="btn-modern">Login / Subscribe</a>
            <a href="/backend/host-list.jsp" class="btn-modern">Listar Hosts</a>
            <button id="btn-logout" class="btn-modern" onclick="logout()">Logout</button>
          </nav>
        </header>

        <main class="main-content">
          <div class="quiz-card" style="width: 100%; max-width: 900px;">
          <h2>Opções da Pergunta ID: <%= request.getAttribute("questionId") %></h2>
            <ul style="list-style: none; padding: 0;">
            <%
              List<Option> options = (List<Option>) request.getAttribute("options");
              int questionId = (request.getAttribute("questionId") != null) ? Integer.parseInt(request.getAttribute("questionId").toString()) : 0;
              for (Option opt : options) {
            %>
                <li style="padding: 10px; margin: 5px 0; border: 1px solid #ddd; border-radius: 5px; display: flex; justify-content: space-between; align-items: center;">
                  <span><%= opt.getDescription() %></span>
                  <form method="post" action="/backend/option" style="display:inline;">
                  <input type="hidden" name="delete" value="1" />
                  <input type="hidden" name="id" value="<%= opt.getId() %>" />
                  <input type="hidden" name="questionId" value="<%= questionId %>" />
                  <button type="submit" class="btn-delete" onclick="return confirm('Tem certeza que deseja deletar esta opção?');">Deletar</button>
                </form>
              </li>
            <%
              }
            %>
          </ul>
          </div>
        </main>

      <script type="text/javascript" src="./js/index.js" ></script>
      <script src="./js/applyCustomization.js"></script>
      <script>
        document.addEventListener('DOMContentLoaded', function() {
          document.getElementById('btn-logout').style.display = '';
        });
      </script>
      <script>
        // Exibe alerta se houver erro de integridade referencial ao deletar opção
        if (window.location.search.includes('deleteError=option')) {
          alert('Não é possível deletar a opção porque ainda existem vínculos. Delete as opções primeiro.');
        }
      </script>
    </body>
</html>