document.addEventListener('DOMContentLoaded', function() {
  
  const questionsDiv = document.getElementById('questions');
  const addBtn = document.getElementById('add-question');

  // Clona o atual input
  addBtn.addEventListener('click', function() {
    
    const allQuestions = questionsDiv.querySelectorAll('.question');
    const last = allQuestions[allQuestions.length - 1];
    const clone = last.cloneNode(true);

    clone.querySelectorAll('input').forEach(input => input.value = '');

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
        
        e.target.closest('.question').remove();
        if (prev && prev.tagName === 'HR') prev.remove();
      }
      
    }

  });

});