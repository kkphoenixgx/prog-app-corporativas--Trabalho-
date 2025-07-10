
function applyColorPreferences() {
  console.log('[applyColorPreferences] Iniciando aplicação de cores...');
  console.log('[applyColorPreferences] Cookies disponíveis:', document.cookie);
  
  const cookies = document.cookie.split(';').map(c => c.trim());
  let fontColor = null, bgColor = null;
  
  cookies.forEach(cookie => {
    console.log('[applyColorPreferences] Processando cookie:', cookie);
    const parts = cookie.split('=');
    if (parts.length >= 2) {
      const name = parts[0];
      const value = parts.slice(1).join('='); // Para valores que podem ter '=' dentro
      console.log('[applyColorPreferences] Nome do cookie:', name, 'Valor:', value);
      
      if (name.startsWith('font_color_')) {
        fontColor = value;
        console.log('[applyColorPreferences] Cor da fonte encontrada:', fontColor);
      }
      if (name.startsWith('background_color_')) {
        bgColor = value;
        console.log('[applyColorPreferences] Cor de fundo encontrada:', bgColor);
      }
    }
  });
  
  if (fontColor) {
    const decodedFontColor = decodeURIComponent(fontColor);
    document.body.style.setProperty('color', decodedFontColor, 'important');
    console.log('[applyColorPreferences] Cor da fonte aplicada:', decodedFontColor);
  }
  
  if (bgColor) {
    const decodedBgColor = decodeURIComponent(bgColor);
    document.body.style.setProperty('background', decodedBgColor, 'important');
    console.log('[applyColorPreferences] Cor de fundo aplicada:', decodedBgColor);
  }
  
  console.log('[applyColorPreferences] Cores aplicadas - Font:', fontColor, 'Background:', bgColor);
}

function applySpecificColors(fontColor, backgroundColor) {
  console.log('[applySpecificColors] Aplicando cores específicas:', fontColor, backgroundColor);
  
  if (fontColor) {
    document.body.style.setProperty('color', fontColor, 'important');
    console.log('[applySpecificColors] Cor da fonte aplicada:', fontColor);
  }
  
  if (backgroundColor) {
    document.body.style.setProperty('background', backgroundColor, 'important');
    console.log('[applySpecificColors] Cor de fundo aplicada:', backgroundColor);
  }
  
  //? Atualizar cookies também
  if (fontColor || backgroundColor) {
    const currentHostId = sessionStorage.getItem('hostId');
    if (currentHostId) {
      if (fontColor) {
        document.cookie = `font_color_${currentHostId}=${fontColor};path=/;max-age=${365 * 24 * 60 * 60}`;
      }
      if (backgroundColor) {
        document.cookie = `background_color_${currentHostId}=${backgroundColor};path=/;max-age=${365 * 24 * 60 * 60}`;
      }
      console.log('[applySpecificColors] Cookies atualizados para host ID:', currentHostId);
    }
  }

}


//! Debug
function forceApplyColors(fontColor, bgColor) {
  console.log('[forceApplyColors] Forçando aplicação de cores:', fontColor, bgColor);
  if (fontColor) {
    document.body.style.setProperty('color', fontColor, 'important');
    console.log('[forceApplyColors] Cor da fonte forçada:', fontColor);
  }
  if (bgColor) {
    document.body.style.setProperty('background', bgColor, 'important');
    console.log('[forceApplyColors] Cor de fundo forçada:', bgColor);
  }
}

function updateMenuByLogin() {

  let loggedIn = false;
  try {
    const token = sessionStorage.getItem('token');
    loggedIn = !!token;
    console.log('[applyCustomization] Token no sessionStorage:', token);
  } catch (e) {
    console.log('[applyCustomization] Erro ao verificar sessionStorage:', e);
  }
  if (!loggedIn) {

    loggedIn = document.cookie.includes('font_color_') && document.cookie.includes('background_color_');
    console.log('[applyCustomization] Fallback para cookies:', loggedIn);

  }
  console.log('[applyCustomization] Usuário logado:', loggedIn);

  const quizLinks = document.querySelectorAll('a[href*="quiz"], a[href*="host-list"], a[href*="quiz-form"]');
  const loginLinks = document.querySelectorAll('a[href*="login-form"]');
  quizLinks.forEach(link => link.style.display = loggedIn ? '' : 'none');
  loginLinks.forEach(link => link.style.display = loggedIn ? 'none' : '');
}


function logout() {
  console.log('[logout] Iniciando logout...');
  try { 
    const token = sessionStorage.getItem('token');
    console.log('[logout] Token antes do logout:', token);
    sessionStorage.removeItem('token'); 
    sessionStorage.removeItem('hostId');
    console.log('[logout] Token removido do sessionStorage');
  } catch (e) {
    console.log('[logout] Erro ao limpar sessionStorage:', e);
  }
  
  //* Limpa cookies de cor
  document.cookie.split(';').forEach(cookie => {
    if (cookie.trim().startsWith('font_color_') || cookie.trim().startsWith('background_color_')) {
      document.cookie = cookie.split('=')[0] + '=;expires=Thu, 01 Jan 1970 00:00:00 UTC;path=/;';
    }
  });
  console.log('[logout] Cookies de cor limpos');
  
  //* Limpar cookies de sessão Java
  document.cookie.split(';').forEach(cookie => {
    const cookieName = cookie.split('=')[0].trim();
    if (cookieName === 'JSESSIONID') {
      document.cookie = cookieName + '=;expires=Thu, 01 Jan 1970 00:00:00 UTC;path=/;';
      console.log('[logout] Cookie JSESSIONID removido');
    }
  });
  
  //* Fazer logout no backend para invalidar a sessão
  fetch('/backend/logout', {
    method: 'POST',
    credentials: 'include'
  })
   .catch(err => {
    console.log('[logout] Erro ao fazer logout no backend:', err);
   })
   .finally(() => { 
    window.location.href = 'login-form.jsp?t=' + Date.now();
   });
}
window.logout = logout;

window.applySpecificColors = applySpecificColors;
window.forceApplyColors = forceApplyColors;

document.addEventListener('DOMContentLoaded', function() {
  console.log('[applyCustomization] DOM carregado, aplicando customizações...');
  applyColorPreferences();
  updateMenuByLogin();
  
  setTimeout(() => {
    console.log('[applyCustomization] Re-aplicando cores após delay...');
    applyColorPreferences();
  }, 100);

});

window.addEventListener('load', function() {
  console.log('[applyCustomization] Página completamente carregada, aplicando cores...');
  applyColorPreferences();
});
