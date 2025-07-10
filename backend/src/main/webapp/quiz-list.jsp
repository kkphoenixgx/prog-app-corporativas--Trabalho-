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
      <div style="display: flex; gap: 1em; align-items: center; margin-bottom: 1em; flex-wrap: wrap;">
        <label for="filterSubject"><strong>Filtrar por assunto:</strong></label>
        <input id="filterSubject" class="input-modern" type="text" placeholder="Assunto..." style="min-width: 180px;">
        <button class="btn-modern" id="btn-search-quiz">Buscar</button>
        <button class="btn-modern" id="btn-clear-quiz">Limpar</button>
        <a class="btn-modern" href="quiz-form.jsp">Novo Quiz</a>
      </div>
      <table id="quizTable" border="1">
        <thead>
        <tr>
          <th>ID</th>
          <th>Assunto</th>
          <th>Respondido em</th>
          <th>Ações</th>
        </tr>
        </thead>
        <tbody id="quizTableBody">
          <!-- Quizzes serão inseridos aqui via JS -->
        </tbody>
      </table>
    </section>
    <script type="text/javascript" src="./js/quizList.js"></script>
    <script type="text/javascript" src="./js/index.js" ></script>
    <script src="./js/applyCustomization.js"></script>
    <script>
      document.addEventListener('DOMContentLoaded', function() {
        document.getElementById('btn-logout').style.display = '';
      });
      // Exibe alerta se houver erro de integridade referencial ao deletar quiz
      if (window.location.search.includes('deleteError=questions')) {
        alert('Não é possível deletar o quiz porque ainda existem questões vinculadas a ele. Delete as questões primeiro.');
      }
    </script>
  </body>
</html>