async function fetchHosts(filterType = '', filterValue = '') {
  let url = '/backend/host';
  if (filterType && filterValue) {
    url += `?${filterType}=${encodeURIComponent(filterValue)}`;
  }
  const resp = await fetch(url);
  if (!resp.ok) return [];
  return await resp.json();
}

function renderHosts(hosts) {
  const tbody = document.getElementById('hostTableBody');
  tbody.innerHTML = '';
  hosts.forEach(host => {
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${host.id}</td>
      <td>${host.name}</td>
      <td>${host.email}</td>
      <td><span style="background:${host.fontColor};color:${host.backgroundColor};padding:2px 8px;border-radius:4px;">${host.fontColor}</span></td>
      <td><span style="background:${host.backgroundColor};color:${host.fontColor};padding:2px 8px;border-radius:4px;">${host.backgroundColor}</span></td>
      <td>
        <button class="btn-modern" onclick="editHost(${host.id})">Editar</button>
        <button class="btn-delete" onclick="deleteHost(${host.id})">Deletar</button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

async function loadHosts() {
  const hosts = await fetchHosts();
  renderHosts(hosts);
}

async function searchHosts() {
  const filterType = document.getElementById('filterType').value;
  const filterValue = document.getElementById('filterInput').value.trim();
  const hosts = await fetchHosts(filterType, filterValue);
  renderHosts(Array.isArray(hosts) ? hosts : [hosts]);
}

async function deleteHost(id) {
  if (!confirm('Tem certeza que deseja deletar este host?')) return;
  
  const currentHostId = sessionStorage.getItem('hostId');
  const isCurrentUser = currentHostId && parseInt(currentHostId) === id;
  
  if (isCurrentUser) {
    if (!confirm('Você está deletando sua própria conta. Isso irá fazer logout automaticamente. Continuar?')) {
      return;
    }
  }
  
  const resp = await fetch(`/backend/host?id=${id}`, { method: 'DELETE' });
  
  if (resp.ok) {
    console.log('[hostList] Host deletado com sucesso');
    
    if (isCurrentUser) {
      console.log('[hostList] Host deletado era o usuário atual, fazendo logout...');
      
      if (typeof logout === 'function') {
        logout();
      } 
      else {
        sessionStorage.clear();
        window.location.href = 'login-form.jsp';
      }
    } 
    else {
      loadHosts();
    }
  } 
  else {
    alert('Erro ao deletar host');
  }
}

function editHost(id) {
  fetch(`/backend/host?id=${id}`)
    .then(resp => resp.json())
    .then(host => showHostForm(host));
}

function showHostForm(host = null) {
  const container = document.getElementById('hostFormContainer');
  container.style.display = 'block';
  container.innerHTML = `
    <form id="hostEditForm" class="form-modern" style="margin-bottom:1em;">
      <input type="hidden" name="id" value="${host ? host.id : ''}">
      <label>Nome: <input class="input-modern" name="name" value="${host ? host.name : ''}" required></label>
      <label>Email: <input class="input-modern" name="email" value="${host ? host.email : ''}" required></label>
      <label>Senha: <input class="input-modern" name="password" type="password" placeholder="${host ? 'Nova senha (opcional)' : ''}"></label>
      <label>Cor da Fonte: <input class="input-modern" name="fontColor" type="color" value="${host ? host.fontColor : '#000000'}"></label>
      <label>Cor de Fundo: <input class="input-modern" name="backgroundColor" type="color" value="${host ? host.backgroundColor : '#7b8fa1'}"></label>
      <div class="buttons">
        <button class="btn-modern" type="submit">${host ? 'Salvar' : 'Criar'}</button>
        <button class="btn-default" type="button" onclick="closeHostForm()">Cancelar</button>
      </div>
    </form>
  `;

  document.getElementById('hostEditForm').onsubmit = async function(e) {
    e.preventDefault();
    const form = e.target;
    const data = Object.fromEntries(new FormData(form));
    const method = host ? 'PUT' : 'POST';
    
    if (!host) { delete data.id; }
    else {
      data.id = host.id;
      if (!data.password) delete data.password;
    }
    
    console.log('[hostList] Salvando host:', data);
    console.log('[hostList] Método:', method);
    
    const resp = await fetch('/backend/host', {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    
    if (resp.ok) {
      const responseData = await resp.json();
      console.log('[hostList] Host salvo com sucesso:', responseData);
      
      if (host && responseData.isCurrentUser) {
        console.log('[hostList] Atualizando cores para o usuário atual');
        
        const fontColorCookie = `font_color_${host.id}=${data.fontColor};path=/;max-age=${365 * 24 * 60 * 60}`;
        const bgColorCookie = `background_color_${host.id}=${data.backgroundColor};path=/;max-age=${365 * 24 * 60 * 60}`;
        
        document.cookie = fontColorCookie;
        document.cookie = bgColorCookie;
        
        console.log('[hostList] Cookies atualizados:', fontColorCookie, bgColorCookie);
        
        // Aplicar as novas cores imediatamente
        applyUpdatedColors(data.fontColor, data.backgroundColor);
        
        // Mostrar feedback visual de sucesso
        showColorUpdateFeedback(data.fontColor, data.backgroundColor);
      }
      
      closeHostForm();
      loadHosts();
      
      // Mostrar mensagem de sucesso
      if (host) {
        alert('Host atualizado com sucesso!');
      } else {
        alert('Host criado com sucesso!');
      }
      
      // Forçar aplicação das cores após um pequeno delay para garantir que tudo foi atualizado
      setTimeout(() => {
        if (typeof applyColorPreferences === 'function') {
          applyColorPreferences();
        }
      }, 500);
    } else {
      const errorText = await resp.text();
      console.error('[hostList] Erro ao salvar host:', errorText);
      alert('Erro ao salvar host: ' + errorText);
    }
  };
}


function applyUpdatedColors(fontColor, backgroundColor) {
  console.log('[hostList] Aplicando cores atualizadas:', fontColor, backgroundColor);
  
  //* Aplicando pela função
  if (typeof applySpecificColors === 'function') {
    applySpecificColors(fontColor, backgroundColor);
  } 
  else {
    
    //* Aplicando manualmente
    document.body.style.setProperty('color', fontColor, 'important');
    document.body.style.setProperty('background', backgroundColor, 'important');
    

    if (typeof applyColorPreferences === 'function') {
      applyColorPreferences();
    }
  }
  
  console.log('[hostList] Cores aplicadas com sucesso');
}

function showColorUpdateFeedback(fontColor, backgroundColor) {

  const originalBg = document.body.style.background;
  const originalColor = document.body.style.color;
  
  setTimeout(() => {

    document.body.style.setProperty('background', '#4CAF50', 'important');
    document.body.style.setProperty('color', '#FFFFFF', 'important');
    
    setTimeout(() => {

      document.body.style.setProperty('background', backgroundColor, 'important');
      document.body.style.setProperty('color', fontColor, 'important');
    }, 300);

  }, 100);

}

function closeHostForm() {
  const container = document.getElementById('hostFormContainer');
  container.style.display = 'none';
  container.innerHTML = '';
}

document.getElementById('btn-search').onclick = searchHosts;

document.getElementById('btn-clear').onclick = function() {
  document.getElementById('filterInput').value = '';
  loadHosts();
};

document.getElementById('btn-new-host').onclick = function() {
  showHostForm();
};

window.editHost = editHost;
window.deleteHost = deleteHost;
window.closeHostForm = closeHostForm;

loadHosts();