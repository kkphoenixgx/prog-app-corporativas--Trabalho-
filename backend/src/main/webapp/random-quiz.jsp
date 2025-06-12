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

        <div class="quizes">
          <% Quiz quiz = (Quiz) request.getAttribute("quiz"); %>
          <% if (quiz != null) { %>
            <h2><%= quiz.getSubject() %></h2>
            <form id="quiz-form">
              <%
                List<Question> questions = quiz.getQuestions();
                for (int i = 0; i < questions.size(); i++) {
                  Question q = questions.get(i);
              %>
                <div class="question-block" data-correct="<%= q.getCorrectOption() %>">
                  <p><%= q.getDescription() %></p>
                  <%
                    List<Option> options = q.getOptions();
                    for (int j = 0; j < options.size(); j++) {
                  %>
                    <label>
                      <input type="radio" name="q<%=i%>" value="<%=j%>">
                      <%= options.get(j).getDescription() %>
                    </label><br>
                  <%
                    }
                  %>
                </div>
              <%
                }
              %>
            </form>
          <% } %>

          <strong class="score" >
            Score: <p class="total" id="el-value"></p>/
            <p id="el-total"></p> 
          </strong>

          <div class="buttons">
            
            <button 
              class="btn-default" 
              id="btn-generate-score" 
            >
              GENERATE SCORE
            </button>
          
            <button 
              class="btn-default" 
              id="btn-save-pdf"
              style="margin-left: 10px;"
            >
              SALVAR COMO PDF
            </button>

          </div>

        </div>

        
      
      </section>

      <script type="text/javascript" src="./js/index.js" ></script>
      <script type="text/javascript" src="./js/randomQuiz.js" ></script>
      <script src="https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/0.10.1/html2pdf.bundle.min.js"></script>
      <script>
        document.getElementById('btn-save-pdf').addEventListener('click', function () {
          // Seleciona a seção que você quer salvar como PDF
          const element = document.querySelector('.quizes');
          html2pdf().from(element).save('quiz.pdf');
        });
      </script>
    </body>
</html>
