document.addEventListener('DOMContentLoaded', async function() {
  const urlParams = new URLSearchParams(window.location.search);
  const quizId = urlParams.get('id');
  if (!quizId) {
    alert('Quiz não encontrado!');
    window.location.href = 'quiz-list.jsp';
    return;
  }

  const form = document.getElementById('editQuizForm');
  const questionsDiv = document.getElementById('questions');
  const addBtn = document.getElementById('add-question');

  //? Carrega quiz existente
  const resp = await fetch(`/backend/quiz-json?id=${quizId}`);
  const quizzes = await resp.json();
  const quiz = Array.isArray(quizzes) ? quizzes.find(q => q.id == quizId) : quizzes;
  if (!quiz) {
    alert('Quiz não encontrado!');
    window.location.href = 'quiz-list.jsp';
    return;
  }
  document.getElementById('quizId').value = quiz.id;
  document.getElementById('quizSubject').value = quiz.subject;

  // ----------- Renderização das Questions -----------

  function renderQuestions(questions) {
    questionsDiv.innerHTML = '';
    questions.forEach((q, idx) => {
      const div = document.createElement('div');
      div.className = 'question';
      div.innerHTML = `
        <label><strong>Pergunta:</strong><br>
          <textarea class="question-textarea" name="question" required>${q.description}</textarea>
        </label><br>
        <label><strong>Opções (separadas por |):</strong><br>
          <input class="question-input" type="text" name="options[]" value="${q.options.map(o => o.description).join('|')}" required>
        </label><br>
        <label><strong>Opção Correta (índice- primeiro é 0):</strong><br>
          <input class="question-input" type="number" name="correctOption" value="${q.correctOption}" required>
        </label><br>
        <button type="button" class="btn-remove btn-default" style="${idx === 0 ? 'display:none;' : 'display:inline-block;'}">Remover</button>
      `;
      questionsDiv.appendChild(div);
      if (idx > 0) {
        const hr = document.createElement('hr');
        hr.style.marginTop = '5vh';
        hr.style.marginBottom = '3vh';
        questionsDiv.insertBefore(hr, div);
      }
    });
  }

  renderQuestions(quiz.questions);

  addBtn.onclick = function() {
    const allQuestions = questionsDiv.querySelectorAll('.question');
    const last = allQuestions[allQuestions.length - 1];
    const clone = last.cloneNode(true);
    clone.querySelectorAll('input,textarea').forEach(input => input.value = '');
    clone.querySelector('.btn-remove').style.display = 'inline-block';
    const hr = document.createElement('hr');
    hr.style.marginTop = '5vh';
    hr.style.marginBottom = '3vh';
    questionsDiv.appendChild(hr);
    questionsDiv.appendChild(clone);
  };

  questionsDiv.addEventListener('click', function(e) {
    if (e.target.classList.contains('btn-remove')) {
      const allQuestions = questionsDiv.querySelectorAll('.question');
      if (allQuestions.length > 1) {
        const questionDiv = e.target.closest('.question');
        const prev = questionDiv.previousElementSibling;
        questionDiv.remove();
        if (prev && prev.tagName === 'HR') prev.remove();
      }
    }
  });

  form.onsubmit = async function(e) {
    e.preventDefault();
    const data = {
      id: quiz.id,
      subject: document.getElementById('quizSubject').value,
      answaredAt: quiz.answaredAt,
      questions: []
    };
    const questionDivs = questionsDiv.querySelectorAll('.question');
    questionDivs.forEach(div => {
      const description = div.querySelector('textarea[name="question"]').value;
      const options = div.querySelector('input[name="options[]"]').value.split('|').map(s => ({ description: s.trim() }));
      const correctOption = parseInt(div.querySelector('input[name="correctOption"]').value);
      data.questions.push({ description, options, correctOption });
    });
    const resp = await fetch('/backend/quiz-json', {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    if (resp.ok) {
      alert('Quiz atualizado com sucesso!');
      window.location.href = 'quiz-list.jsp';
    } else {
      alert('Erro ao atualizar quiz');
    }
  };
  
}); 