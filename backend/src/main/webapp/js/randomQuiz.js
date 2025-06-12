document.addEventListener("DOMContentLoaded", function() {
  const btnScore = document.getElementById("btn-generate-score");
  const elValue = document.getElementById("el-value");
  const elTotal = document.getElementById("el-total");

  if (btnScore) {
    btnScore.addEventListener("click", function() {
      const form = document.getElementById("quiz-form");
      if (!form) return;
      let correct = 0, total = 0;

      const questions = form.querySelectorAll('[data-correct]');
      questions.forEach((questionDiv, i) => {
        total++;
        const correctOption = questionDiv.getAttribute('data-correct');
        const radios = questionDiv.querySelectorAll('input[type="radio"]');
        let userSelected = null;
        radios.forEach((radio, idx) => {
          const label = radio.parentElement;
          label.style.color = '';
          if (radio.checked) userSelected = idx;
        });


        radios.forEach((radio, idx) => {
          const label = radio.parentElement;
          if (idx == correctOption) {
            label.style.color = '#06bb06';
          } else if (userSelected === idx && idx != correctOption) {
            label.style.color = '#d32f2f';
          }
        });

        if (userSelected !== null && String(userSelected) === correctOption) {
          correct++;
        }
      });

      if (total === 0) {
        alert("Nenhuma pergunta encontrada.");
      } else {
        // Chama o backend para calcular a média
        fetch('/backend/calc-score', {
          method: 'POST',
          headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
          body: `correctAnswers=${correct}&totalQuestions=${total}`
        })
        .then(resp => resp.text())
        .then(score => {
          elValue.textContent = score;
          elTotal.textContent = `${total}`;
        })
        .catch(() => {
          elValue.textContent = '?';
          elTotal.textContent = `${total}`;
        });
      }
    });
  }
});