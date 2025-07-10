<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
  <link rel="icon" type="image/png" href="./assets/icon.png">
  <link rel="stylesheet" href="./styles/index.css">
  <title>Lista de Hosts</title>
</head>
<body>
  <header class="main-header">
    <div class="header-content">
      <div class="hamburguer-menu" id="btn-hamburguer-menu">
        <span></span><span></span><span></span>
      </div>
      <a class="header-a" href="/backend/index.html">
        <h1>Random Quiz</h1>
      </a>
    </div>
    <nav class="container-menu" id="container-menu">
      <div class="container-menu-center">
        <button class="btn-close-menu btn-default" id="btn-close-menu">Fechar</button>
      </div>
      <a href="/backend/quiz" class="btn-modern">Ver Quizzes</a>
      <a href="/backend/quiz-form.jsp" class="btn-modern">Criar Quiz</a>
      <a href="/backend/host-list.jsp" class="btn-modern">Listar Hosts</a>
      <a href="/backend/login-form.jsp" class="btn-modern">Login / Subscribe</a>
      <button id="btn-logout" class="btn-modern" onclick="logout()">Logout</button>
    </nav>
  </header>
  <main class="main-content">
    <div class="quiz-card host-list-card" style="width: 100%; max-width: 900px;">
      <h2>Lista de Hosts</h2>
      <div style="display: flex; gap: 1em; align-items: center; margin-bottom: 1em; flex-wrap: wrap;">
        <label for="filterType"><strong>Filtrar por:</strong></label>
        <select id="filterType" class="option-list">
          <option value="name">Nome</option>
          <option value="id">ID</option>
          <option value="email">Email</option>
        </select>
        <input id="filterInput" class="input-modern" type="text" placeholder="Buscar..." style="min-width: 180px;">
        <button class="btn-modern" id="btn-search">Buscar</button>
        <button class="btn-modern" id="btn-clear">Limpar</button>
        <button class="btn-modern" id="btn-new-host">Novo Host</button>

      </div>
      <div id="hostFormContainer" class="host-form-container" style="display:none;"></div>
      <table id="hostTable">
        <thead>
          <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Email</th>
            <th>Cor Fonte</th>
            <th>Cor Fundo</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody id="hostTableBody">
          <!--? Hosts serão inseridos aqui via JS -->
        </tbody>
      </table>
    </div>
  </main>
  <script type="text/javascript" src="./js/index.js"></script>
  <script src="./js/applyCustomization.js"></script>
  <script type="text/javascript" src="./js/hostList.js"></script>
  <script>
    document.addEventListener('DOMContentLoaded', function() {
      document.getElementById('btn-logout').style.display = '';
    });
    

      }
    }
    

    </script>
  </body>
</html>
