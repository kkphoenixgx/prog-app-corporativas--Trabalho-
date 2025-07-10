document.addEventListener('DOMContentLoaded', function() {
  const form = document.getElementById('quizCreateForm');
  const questionsDiv = document.getElementById('questions');
  const addBtn = document.getElementById('add-question');

  addBtn.addEventListener('click', function() {
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
  });
  
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
      subject: document.getElementById('quizSubject').value,
      answaredAt: new Date().toISOString(),
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
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    if (resp.ok) {
      alert('Quiz criado com sucesso!');
      window.location.href = 'quiz-list.jsp';
    } else {
      alert('Erro ao criar quiz');
    }
  };
});