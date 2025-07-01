<%@ page import="java.util.List" %>
<%@ page import="model.Quiz" %>
<%@ page import="model.Question" %>
<%@ page import="model.Option" %>

<%@ pagecontentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglibprefix="c" uri="jakarta.tags.core" %>
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
          <button class="btn-close-menu btn-default" id="btn-close-menu" >close</button>
        </div>
        
        <a href="/backend/quiz">Ver Quizzes</a><br>
        <a href="/backend/quiz-form.jsp">Criar Quiz</a>
      </div>
    
    </header>

    <section>
   
      <table border="1">
        <tr>
          <th>ID</th>
          <th>Assunto</th>
          <th>Respondido em</th>
          <th>Ações</th>
        </tr>
  
        <%
          List<Quiz> quizzes = (List<Quiz>) request.getAttribute("quizzes");
          for (Quiz quiz : quizzes) {
        %>
          <tr>
            <td><%= quiz.getId() %></td>
            <td><%= quiz.getSubject() %></td>
            <td><%= quiz.getAnswaredAt() %></td>
            <td>
              <a href="question?quizId=<%= quiz.getId() %>">Ver Perguntas</a>
              <form method="post" action="/backend/quiz" style="display:inline; margin-left:10px;">
                <input type="hidden" name="delete" value="1" />
                <input type="hidden" name="id" value="<%= quiz.getId() %>" />
                <button type="submit" class="btn-delete" onclick="return confirm('Tem certeza que deseja deletar este quiz?');">Deletar</button>
              </form>
            </td>
          </tr>
        <%
          }
        %>
  
      </table>

    </section>


    <script type="text/javascript" src="./js/index.js" ></script>
    <script>
      // Exibe alerta se houver erro de integridade referencial ao deletar quiz
      if (window.location.search.includes('deleteError=questions')) {
        alert('Não é possível deletar o quiz porque ainda existem questões vinculadas a ele. Delete as questões primeiro.');
      }
    </script>
  </body>
</html>