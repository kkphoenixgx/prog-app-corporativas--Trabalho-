<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
  <link rel="icon" type="image/png" href="./assets/icon.png">
  <link rel="stylesheet" href="./styles/index.css">
  <title>Teste de Autenticação</title>
</head>
<body>
  <header>
    <div class="hamburguer-menu" id="btn-hamburguer-menu">
      <span></span><span></span><span></span>
    </div>
    <a class="header-a" href="/backend/index.html">
      <h1>Teste de Autenticação</h1>
    </a>
  </header>
  
  <main class="main-content">
    <div class="quiz-card" style="width: 100%; max-width: 800px;">
      <h2>Status da Autenticação</h2>
      
      <div style="margin-bottom: 2em;">
        <h3>Informações da Sessão</h3>
        <div id="sessionInfo" style="background: #f5f5f5; padding: 1em; border-radius: 8px; margin-bottom: 1em;">
          <p><strong>Session ID:</strong> <span id="sessionId">Carregando...</span></p>
          <p><strong>Token:</strong> <span id="tokenInfo">Carregando...</span></p>
          <p><strong>Host ID:</strong> <span id="hostIdInfo">Carregando...</span></p>
          <p><strong>Logado:</strong> <span id="loggedInInfo">Carregando...</span></p>
        </div>
      </div>
      
      <div style="margin-bottom: 2em;">
        <h3>Testes</h3>
        <div style="display: flex; gap: 1em; flex-wrap: wrap;">
          <button class="btn-modern" onclick="testRandomQuiz()">🎲 Testar Random Quiz</button>
          <button class="btn-modern" onclick="testAuth()">🔐 Testar Autenticação</button>
          <button class="btn-modern" onclick="clearSession()">🗑️ Limpar Sessão</button>
        </div>
      </div>
      
      <div>
        <h3>Log de Eventos</h3>
        <div id="logContainer" style="background: #000; color: #0f0; padding: 1em; border-radius: 8px; height: 200px; overflow-y: auto; font-family: monospace; font-size: 0.9em;">
          <div>Log iniciado...</div>
        </div>
      </div>
    </div>
  </main>
  
  <script src="./js/applyCustomization.js"></script>
  <script>
    function log(message) {
      const logContainer = document.getElementById('logContainer');
      const timestamp = new Date().toLocaleTimeString();
      logContainer.innerHTML += `<div>[${timestamp}] ${message}</div>`;
      logContainer.scrollTop = logContainer.scrollHeight;
      console.log(`[Teste] ${message}`);
    }
    
    function updateSessionInfo() {
      log('Atualizando informações da sessão...');
      
      const sessionId = '<%= session.getId() %>';
      const token = sessionStorage.getItem('token');
      const hostId = sessionStorage.getItem('hostId');
      const loggedIn = !!token;
      
      document.getElementById('sessionId').textContent = sessionId || 'não disponível';
      document.getElementById('tokenInfo').textContent = token || 'não encontrado';
      document.getElementById('hostIdInfo').textContent = hostId || 'não encontrado';
      document.getElementById('loggedInInfo').textContent = loggedIn ? 'SIM' : 'NÃO';
      
      log(`Sessão: ${sessionId}, Token: ${token}, HostId: ${hostId}, Logado: ${loggedIn}`);
    }
    
    function testRandomQuiz() {
      log('Testando acesso ao Random Quiz...');
      
      fetch('/backend/random-quiz?subject=teste')
        .then(resp => {
          log(`Resposta do Random Quiz: ${resp.status} ${resp.statusText}`);
          if (resp.ok) {
            return resp.text();
          } else {
            throw new Error(`HTTP ${resp.status}: ${resp.statusText}`);
          }
        })
        .then(html => {
          log('Random Quiz carregado com sucesso');
          log(`Tamanho da resposta: ${html.length} caracteres`);
          if (html.includes('login-form.jsp')) {
            log('ERRO: Página redirecionou para login!');
          } else if (html.includes('Random Quiz')) {
            log('SUCESSO: Página carregada corretamente');
          } else {
            log('AVISO: Resposta inesperada');
          }
        })
        .catch(err => {
          log(`ERRO ao testar Random Quiz: ${err.message}`);
        });
    }
    
    function testAuth() {
      log('Testando autenticação...');
      
      const token = sessionStorage.getItem('token');
      if (!token) {
        log('ERRO: Nenhum token encontrado');
        return;
      }
      
      fetch('/backend/host', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
        .then(resp => {
          log(`Resposta da autenticação: ${resp.status} ${resp.statusText}`);
          if (resp.ok) {
            log('SUCESSO: Autenticação válida');
          } else {
            log('ERRO: Autenticação inválida');
          }
        })
        .catch(err => {
          log(`ERRO ao testar autenticação: ${err.message}`);
        });
    }
    
    function clearSession() {
      log('Limpando sessão...');
      sessionStorage.clear();
      log('SessionStorage limpo');
      updateSessionInfo();
    }
    
    // Inicializar
    document.addEventListener('DOMContentLoaded', function() {
      log('Página carregada');
      updateSessionInfo();
    });
  </script>
</body>
</html> 