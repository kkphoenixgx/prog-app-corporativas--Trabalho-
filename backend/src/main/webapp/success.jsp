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

        <div class="success-operation">
          <p style=" color: #06bb06 " >Operação realizada com sucesso!</p>
          <a href="index.html">Voltar</a>
        </div>

      </section>



      <script type="text/javascript" src="./js/index.js" ></script>
      <script src="./js/applyCustomization.js"></script>
      <script>
        document.addEventListener('DOMContentLoaded', function() {
          document.getElementById('btn-logout').style.display = '';
        });
      </script>
    </body>
</html>