document.addEventListener('DOMContentLoaded', function() {
  fetch('/backend/random-quiz?listSubjects=1')
    .then(resp => resp.text())
    .then(text => {
      const select = document.getElementById('subject-select');
      select.innerHTML = '';
      const subjects = text.split('\n').map(s => s.trim()).filter(s => s.length > 0);
      if (subjects.length === 0) {
        select.innerHTML = '<option value="">Nenhum assunto encontrado</option>';
        select.disabled = true;
      } else {
        select.disabled = false;
        select.innerHTML = '<option value="">Selecione um assunto</option>';
        subjects.forEach(subj => {
          const opt = document.createElement('option');
          opt.value = subj;
          opt.textContent = subj;
          select.appendChild(opt);
        });
      }
    })
    .catch(() => {
      const select = document.getElementById('subject-select');
      select.innerHTML = '<option value="">Erro ao carregar assuntos</option>';
      select.disabled = true;
    });
});