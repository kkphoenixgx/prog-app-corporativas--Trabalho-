<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
  <link rel="icon" type="image/png" href="./assets/icon.png">
  <link rel="stylesheet" href="./styles/index.css">
  <title>Editar Quiz</title>
</head>
<body>
  <header>
    <div class="hamburguer-menu" id="btn-hamburguer-menu">
      <span></span><span></span><span></span>
    </div>
    <a class="header-a" href="/backend/index.html">
      <h1>Random Quiz</h1>
    </a>
    <div class="container-menu" id="container-menu">
      <div class="container-menu-center">
        <button class="btn-close-menu btn-default" id="btn-close-menu">Fechar</button>
      </div>
      <a href="/backend/quiz" class="btn-modern">Ver Quizzes</a>
      <a href="/backend/quiz-form.jsp" class="btn-modern">Criar Quiz</a>
      <a href="/backend/login-form.jsp" class="btn-modern">Login / Subscribe</a>
      <a href="/backend/host-list.jsp" class="btn-modern">Listar Hosts</a>
      <button id="btn-logout" class="btn-modern" onclick="logout()">Logout</button>
    </div>
  </header>
  <main class="main-content">
    <div class="quiz-card" style="width: 100%; max-width: 900px;">
      <h2>Editar Quiz</h2>
      <form id="editQuizForm" class="quiz-form">
        <input type="hidden" name="id" id="quizId">
        <label>
          <strong>Assunto do Quiz:</strong>
          <input class="input-modern" type="text" name="subject" id="quizSubject" required>
        </label>
        <div id="questions"></div>
        <div class="quiz-btns">
          <button class="btn-default" type="button" id="add-question">Adicionar Pergunta</button>
          <button class="btn-modern" type="submit">Salvar Alterações</button>
        </div>
      </form>
    </div>
  </main>
  <script type="text/javascript" src="./js/editQuiz.js"></script>
  <script src="./js/applyCustomization.js"></script>
</body>
</html> 