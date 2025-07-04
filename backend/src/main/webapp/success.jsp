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

        <div class="success-operation">
          <p style=" color: #06bb06 " >Operação realizada com sucesso!</p>
          <a href="index.html">Voltar</a>
        </div>

      </section>



      <script type="text/javascript" src="./js/index.js" ></script>
    </body>
</html>