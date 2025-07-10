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
      <div class="subscribe-container">
        <h2>Cadastro de Host</h2>
        <form id="subscribeForm">
          <label for="name">Nome:</label>
          <input class="input-modern" type="text" id="name" name="name" required><br>
          <label for="email">Email:</label>
          <input class="input-modern" type="email" id="email" name="email" required><br>
          <label for="password">Senha:</label>
          <input class="input-modern" type="password" id="password" name="password" required><br>
          <label for="fontColor">Cor da Fonte:</label>
          <input class="input-modern" type="color" id="fontColor" name="fontColor" value="#000000"><br>
          <label for="backgroundColor">Cor de Fundo:</label>
          <input class="input-modern" type="color" id="backgroundColor" name="backgroundColor" value="#FFFFFF"><br>
          <button class="btn-modern" type="submit">Cadastrar</button>
          
          <div id="subscribeError" class="error-message"></div>
          <div id="subscribeSuccess" class="success-message"></div>
        </form>
          
      </div>

    </section>
    <script>

      const submitFrm = document.getElementById('subscribeForm');
      submitFrm.addEventListener('submit', handleSubscribe)

      async function handleSubscribe(e){
        e.preventDefault();
        
        const name = document.getElementById('name').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const fontColor = document.getElementById('fontColor').value;
        const backgroundColor = document.getElementById('backgroundColor').value;
        const errorDiv = document.getElementById('subscribeError');
        const successDiv = document.getElementById('subscribeSuccess');
        
        errorDiv.textContent = '';
        successDiv.textContent = '';
        
        try {
          
          const resp = await fetch('/backend/credentials?op=subscribe', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, email, password, fontColor, backgroundColor })
          });

          if (resp.ok) {
            successDiv.textContent = 'Cadastro realizado com sucesso!';
            document.getElementById('subscribeForm').reset();
                        // Após cadastro bem-sucedido, fazer login automático
            setTimeout(() => {
              // Fazer login automático após cadastro
              fetch('/backend/credentials?op=login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
              })
              .then(resp => resp.json())
              .then(data => {
                if (data.success) {
                  // Salvar token no sessionStorage
                  sessionStorage.setItem('token', data.token);
                  sessionStorage.setItem('hostId', data.hostId);
                  console.log('[subscribe-form] Token salvo no sessionStorage:', data.token);
                  console.log('[subscribe-form] HostId salvo no sessionStorage:', data.hostId);
                  // Forçar refresh completo para limpar cache
                  window.location.href = '/backend/index.html?t=' + Date.now();
                } else {
                  console.log('[subscribe-form] Falha no login automático:', data);
                  // Se falhar o login automático, apenas redireciona
                  window.location.href = '/backend/index.html?t=' + Date.now();
                }
              })
              .catch((error) => {
                console.log('[subscribe-form] Erro no login automático:', error);
                // Se falhar o login automático, apenas redireciona
                window.location.href = '/backend/index.html?t=' + Date.now();
              });
            }, 1200); // Redireciona após 1.2 segundos
          } 
          else {
            const msg = await resp.text();
            errorDiv.textContent = msg || 'Erro ao cadastrar';
          }

        } 
        catch (err) { errorDiv.textContent = 'Erro de conexão'; }
        
      }

    </script>
    <script type="text/javascript" src="./js/index.js" ></script>
    <script src="./js/applyCustomization.js"></script>
  </body>
</html>