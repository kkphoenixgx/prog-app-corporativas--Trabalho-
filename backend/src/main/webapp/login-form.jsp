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

      <section>
        <div class="login-container">
          <h2>Login</h2>
          <form id="loginForm">
            <label for="email">Email:</label>
            <input class="input-modern" type="email" id="email" name="email" required><br>
            <label for="password">Senha:</label>
            <input class="input-modern" type="password" id="password" name="password" required><br>
            <button class="btn-modern" type="submit">Entrar</button>
            <div id="loginError" class="error-message"></div>
          </form>
          <a href="/backend/subscribe-form.jsp">Subscribe</a>
        </div>

      </section>
      <script>
        document.getElementById('loginForm').addEventListener('submit', async function(e) {
          e.preventDefault();
          const email = document.getElementById('email').value;
          const password = document.getElementById('password').value;
          const errorDiv = document.getElementById('loginError');
          errorDiv.textContent = '';
          try {
            const resp = await fetch('/backend/credentials?op=login', {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify({ email, password })
            });
            const data = await resp.json();
            if (resp.ok && data.success) {
              // Salvar token no sessionStorage
              sessionStorage.setItem('token', data.token);
              sessionStorage.setItem('hostId', data.hostId);
              console.log('[login-form] Token salvo no sessionStorage:', data.token);
              console.log('[login-form] HostId salvo no sessionStorage:', data.hostId);
              // Forçar refresh completo para limpar cache
              window.location.href = '/backend/index.html?t=' + Date.now();
            } else {
              errorDiv.textContent = data.error || 'Erro ao fazer login';
            }
          } catch (err) {
            errorDiv.textContent = 'Erro de conexão';
          }
        });
      </script>
      <script type="text/javascript" src="./js/index.js" ></script>
      <script src="./js/applyCustomization.js"></script>
      <script>
        document.addEventListener('DOMContentLoaded', function() {
          document.getElementById('btn-logout').style.display = '';
        });
      </script>
    </body>
</html>


