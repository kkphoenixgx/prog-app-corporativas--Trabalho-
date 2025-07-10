async function fetchQuizzes(subject = '') {
  let url = '/backend/quiz-json';
  if (subject) {
    url += `?subject=${encodeURIComponent(subject)}`;
  }
  const resp = await fetch(url);
  if (!resp.ok) return [];
  return await resp.json();
}

function renderQuizzes(quizzes) {

  const tbody = document.getElementById('quizTableBody');
  tbody.innerHTML = '';

  quizzes.forEach(quiz => {
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${quiz.id}</td>
      <td>${quiz.subject}</td>
      <td>${new Date(quiz.answaredAt).toLocaleString()}</td>
      <td>
        <a href="question?quizId=${quiz.id}">Ver Perguntas</a>
        <button class="btn-modern" onclick="editQuiz(${quiz.id})">Editar</button>
        <button class="btn-delete" onclick="deleteQuiz(${quiz.id})">Deletar</button>
      </td>
    `;
    tbody.appendChild(tr);
  });
  
}

async function loadQuizzes() {
  const quizzes = await fetchQuizzes();
  renderQuizzes(quizzes);
}

async function searchQuizzes() {
  const subject = document.getElementById('filterSubject').value.trim();
  const quizzes = await fetchQuizzes(subject);
  renderQuizzes(quizzes);
}

async function deleteQuiz(id) {
  if (!confirm('Tem certeza que deseja deletar este quiz?')) return;
  const resp = await fetch(`/backend/quiz?id=${id}`, { method: 'POST', headers: { 'Content-Type': 'application/x-www-form-urlencoded' }, body: 'delete=1&id=' + id });
  if (resp.ok) loadQuizzes();
  else alert('Erro ao deletar quiz');
}

function editQuiz(id) {
  window.location.href = `edit-quiz-form.jsp?id=${id}`;
}

window.editQuiz = editQuiz;
window.deleteQuiz = deleteQuiz;

document.getElementById('btn-search-quiz').onclick = searchQuizzes;
document.getElementById('btn-clear-quiz').onclick = function() {
  document.getElementById('filterSubject').value = '';
  loadQuizzes();
};

loadQuizzes(); 