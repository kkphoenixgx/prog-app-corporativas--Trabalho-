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
      <header> 
        <div class="hamburguer-menu" id="btn-hamburguer-menu">
          <span></span>
          <span></span>
          <span></span>
        </div>
        <a class="header-a" href="/backend/index.html">
          <h1>Random Quiz</h1>
        </a>
        
        <div class="container-menu" id="container-menu" >
          <div class="container-menu-center">
            <button class="btn-close-menu btn-default" id="btn-close-menu" >Fechar</button>
          </div>
          <a href="/backend/quiz" class="btn-modern">Ver Quizzes</a>
          <a href="/backend/quiz-form.jsp" class="btn-modern">Criar Quiz</a>
          <a href="/backend/login-form.jsp" class="btn-modern">Login / Subscribe</a>
          <a href="/backend/host-list.jsp" class="btn-modern">Listar Hosts</a>
          <button id="btn-logout" class="btn-modern" onclick="logout()">Logout</button>
        </div>
      
      </header>

      <section>

        <h2>Perguntas do Quiz ID: <%= request.getAttribute("quizId") %></h2>
        <table border="1">
          <tr>
            <th>ID</th>
            <th>Descrição</th>
            <th>Opções</th>
            <th>Correta</th>
            <th>Ações</th>
          </tr>
          <%
            List<Question> questions = (List<Question>) request.getAttribute("questions");
            int quizId = (request.getAttribute("quizId") != null) ? Integer.parseInt(request.getAttribute("quizId").toString()) : 0;
            for (Question q : questions) {
          %>
            <tr>
              <td><%= q.getId() %></td>
              <td><%= q.getDescription() %></td>
              <td>
                <ul>
                  <%
                    List<Option> opts = q.getOptions();
                    for (int i = 0; i < opts.size(); i++) {
                      %>
                        <li><%= i %>: <%= opts.get(i).getDescription() %></li>
                      <%
                    }
                  %>
                </ul>
              </td>
              <td><%= q.getCorrectOption() %></td>
              <td>
                <a href="option?questionId=<%= q.getId() %>" style="margin-right:8px;">Ver Opções</a>
                <form method="post" action="/backend/question" style="display:inline;">
                  <input type="hidden" name="delete" value="1" />
                  <input type="hidden" name="id" value="<%= q.getId() %>" />
                  <input type="hidden" name="quizId" value="<%= quizId %>" />
                  <button type="submit" class="btn-delete" onclick="return confirm('Tem certeza que deseja deletar esta questão?');">Deletar</button>
                </form>
              </td>
            </tr>
          <%
            }
          %>
        </table>
        
      </section>



      <script type="text/javascript" src="./js/index.js" ></script>
      <script src="./js/applyCustomization.js"></script>
      <script>
        // Exibe alerta se houver erro de integridade referencial ao deletar questão
        if (window.location.search.includes('deleteError=options')) {
          alert('Não é possível deletar a questão porque ainda existem opções vinculadas a ela. Delete as opções primeiro.');
        }
      </script>
    </body>
</html>