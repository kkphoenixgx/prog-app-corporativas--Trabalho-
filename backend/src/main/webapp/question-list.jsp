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

        <h2>Perguntas do Quiz ID: <%= request.getAttribute("quizId") %></h2>
        <table border="1">
          <tr>
            <th>ID</th>
            <th>Descrição</th>
            <th>Opções</th>
            <th>Correta</th>
          </tr>
          <%
            List<Question> questions = (List<Question>) request.getAttribute("questions");
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
                  
                </tr>
              <%
            }
          %>
        </table>
        
      </section>



      <script type="text/javascript" src="./js/index.js" ></script>
    </body>
</html>