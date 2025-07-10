<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
  <link rel="icon" type="image/png" href="./assets/icon.png">
  <link rel="stylesheet" href="./styles/index.css">
  <title>Teste de Cores</title>
</head>
<body>
  <header>
    <div class="hamburguer-menu" id="btn-hamburguer-menu">
      <span></span><span></span><span></span>
    </div>
    <a class="header-a" href="/backend/index.html">
      <h1>Teste de Cores</h1>
    </a>
    <div class="container-menu" id="container-menu">
      <div class="container-menu-center">
        <button class="btn-close-menu btn-default" id="btn-close-menu">Fechar</button>
      </div>
      <a href="/backend/host-list.jsp">Lista de Hosts</a>
      <a href="/backend/quiz-list.jsp">Lista de Quizzes</a>
      <a href="/backend/login-form.jsp">Login</a>
    </div>
  </header>
  
  <main class="main-content">
    <div class="quiz-card" style="width: 100%; max-width: 800px;">
      <h2>Teste de Sistema de Cores</h2>
      
      <div style="margin-bottom: 2em;">
        <h3>Informações Atuais</h3>
        <div id="currentInfo" style="background: #f5f5f5; padding: 1em; border-radius: 8px; margin-bottom: 1em;">
          <p><strong>Token:</strong> <span id="tokenInfo">Carregando...</span></p>
          <p><strong>Host ID:</strong> <span id="hostIdInfo">Carregando...</span></p>
          <p><strong>Cor da Fonte:</strong> <span id="fontColorInfo">Carregando...</span></p>
          <p><strong>Cor de Fundo:</strong> <span id="bgColorInfo">Carregando...</span></p>
          <p><strong>Cookies:</strong> <span id="cookiesInfo">Carregando...</span></p>
        </div>
      </div>
      
      <div style="margin-bottom: 2em;">
        <h3>Ações de Teste</h3>
        <div style="display: flex; gap: 1em; flex-wrap: wrap;">
          <button class="btn-modern" onclick="updateInfo()">🔄 Atualizar Info</button>
          <button class="btn-modern" onclick="applyColors()">🎨 Aplicar Cores</button>
          <button class="btn-modern" onclick="forceColors()">⚡ Forçar Cores</button>
          <button class="btn-modern" onclick="testRedGreen()">🔴🟢 Teste Vermelho/Verde</button>
          <button class="btn-modern" onclick="clearColors()">🗑️ Limpar Cores</button>
        </div>
      </div>
      
      <div style="margin-bottom: 2em;">
        <h3>Teste de Cores Específicas</h3>
        <div style="display: flex; gap: 1em; align-items: center; flex-wrap: wrap;">
          <label>Cor da Fonte: <input type="color" id="testFontColor" value="#000000"></label>
          <label>Cor de Fundo: <input type="color" id="testBgColor" value="#FFFFFF"></label>
          <button class="btn-modern" onclick="applyTestColors()">Aplicar Cores de Teste</button>
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
    
    function updateInfo() {
      log('Atualizando informações...');
      
      const token = sessionStorage.getItem('token');
      const hostId = sessionStorage.getItem('hostId');
      const cookies = document.cookie;
      const currentFontColor = document.body.style.color || 'não definida';
      const currentBgColor = document.body.style.background || 'não definida';
      
      document.getElementById('tokenInfo').textContent = token || 'não encontrado';
      document.getElementById('hostIdInfo').textContent = hostId || 'não encontrado';
      document.getElementById('fontColorInfo').textContent = currentFontColor;
      document.getElementById('bgColorInfo').textContent = currentBgColor;
      document.getElementById('cookiesInfo').textContent = cookies || 'nenhum cookie';
      
      log('Informações atualizadas');
    }
    
    function applyColors() {
      log('Aplicando cores via applyColorPreferences...');
      if (typeof applyColorPreferences === 'function') {
        applyColorPreferences();
        log('Cores aplicadas via applyColorPreferences');
      } else {
        log('ERRO: applyColorPreferences não encontrada');
      }
    }
    
    function forceColors() {
      log('Forçando aplicação de cores...');
      const hostId = sessionStorage.getItem('hostId');
      if (!hostId) {
        log('ERRO: Nenhum host ID encontrado');
        return;
      }
      
      fetch(`/backend/host?id=${hostId}`)
        .then(resp => resp.json())
        .then(host => {
          log(`Host encontrado: ${host.name} (ID: ${host.id})`);
          log(`Cores do host: Fonte=${host.fontColor}, Fundo=${host.backgroundColor}`);
          
          if (typeof applySpecificColors === 'function') {
            applySpecificColors(host.fontColor, host.backgroundColor);
            log('Cores aplicadas via applySpecificColors');
          } else {
            log('ERRO: applySpecificColors não encontrada');
          }
        })
        .catch(err => {
          log(`ERRO ao buscar host: ${err.message}`);
        });
    }
    
    function testRedGreen() {
      log('Testando cores vermelho/verde...');
      if (typeof applySpecificColors === 'function') {
        applySpecificColors('#FF0000', '#00FF00');
        log('Cores de teste aplicadas: Vermelho/Verde');
      } else {
        log('ERRO: applySpecificColors não encontrada');
      }
    }
    
    function clearColors() {
      log('Limpando cores...');
      document.body.style.removeProperty('color');
      document.body.style.removeProperty('background');
      log('Cores removidas do body');
    }
    
    function applyTestColors() {
      const fontColor = document.getElementById('testFontColor').value;
      const bgColor = document.getElementById('testBgColor').value;
      
      log(`Aplicando cores de teste: Fonte=${fontColor}, Fundo=${bgColor}`);
      
      if (typeof applySpecificColors === 'function') {
        applySpecificColors(fontColor, bgColor);
        log('Cores de teste aplicadas');
      } else {
        log('ERRO: applySpecificColors não encontrada');
      }
    }
    
    // Inicializar
    document.addEventListener('DOMContentLoaded', function() {
      log('Página carregada');
      updateInfo();
      
      // Aplicar cores iniciais
      setTimeout(() => {
        log('Aplicando cores iniciais...');
        applyColors();
      }, 500);
    });
  </script>
</body>
</html> 