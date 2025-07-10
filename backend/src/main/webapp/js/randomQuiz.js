document.addEventListener("DOMContentLoaded", function() {
  console.log('[randomQuiz] DOM carregado');
  
  const btnScore = document.getElementById("btn-generate-score");
  const elValue = document.getElementById("el-value");
  const elTotal = document.getElementById("el-total");

  if (btnScore) {
    console.log('[randomQuiz] Botão de score encontrado');
    btnScore.addEventListener("click", function() {
      console.log('[randomQuiz] Botão de score clicado');
      
      try {
        const form = document.getElementById("quiz-form");
        if (!form) {
          console.log('[randomQuiz] Formulário não encontrado');
          alert("Formulário de quiz não encontrado");
          return;
        }
        
        let correct = 0, total = 0;

        const questions = form.querySelectorAll('[data-correct]');
        console.log('[randomQuiz] Questões encontradas:', questions.length);
        
        if (questions.length === 0) {
          console.log('[randomQuiz] Nenhuma questão encontrada no formulário');
          alert("Nenhuma pergunta encontrada no formulário.");
          return;
        }
        
        questions.forEach((questionDiv, i) => {
          total++;
          const correctOption = questionDiv.getAttribute('data-correct');
          const radios = questionDiv.querySelectorAll('input[type="radio"]');
          let userSelected = null;
          
          console.log(`[randomQuiz] Questão ${i}: opção correta = ${correctOption}`);
          
          // Verificar se o usuário selecionou alguma opção
          radios.forEach((radio, idx) => {
            const label = radio.parentElement;
            label.style.color = '';
            if (radio.checked) {
              userSelected = idx;
              console.log(`[randomQuiz] Questão ${i}: usuário selecionou ${idx}`);
            }
          });

          // Colorir as opções
          radios.forEach((radio, idx) => {
            const label = radio.parentElement;
            if (idx == correctOption) {
              label.style.color = '#06bb06'; // Verde para correta
            } else if (userSelected === idx && idx != correctOption) {
              label.style.color = '#d32f2f'; // Vermelho para incorreta selecionada
            }
          });

          // Verificar se acertou
          if (userSelected !== null && String(userSelected) === correctOption) {
            correct++;
            console.log(`[randomQuiz] Questão ${i}: CORRETA`);
          } else {
            console.log(`[randomQuiz] Questão ${i}: INCORRETA (selecionou: ${userSelected}, correto: ${correctOption})`);
          }
        });

        console.log(`[randomQuiz] Resultado: ${correct}/${total} corretas`);

        if (total === 0) {
          console.log('[randomQuiz] Nenhuma pergunta encontrada');
          alert("Nenhuma pergunta encontrada.");
          return;
        }
        
        // Calcular porcentagem localmente
        const percentage = Math.round((correct / total) * 100);
        console.log(`[randomQuiz] Porcentagem calculada: ${percentage}%`);
        
        // Atualizar a interface
        if (elValue) {
          elValue.textContent = percentage + '%';
          console.log('[randomQuiz] Score atualizado na interface:', percentage + '%');
        } else {
          console.log('[randomQuiz] Elemento el-value não encontrado');
        }
        
        if (elTotal) {
          elTotal.textContent = '100%';
          console.log('[randomQuiz] Total atualizado na interface: 100%');
        } else {
          console.log('[randomQuiz] Elemento el-total não encontrado');
        }
        
        // Mostrar resultado final
        const message = `Você acertou ${correct} de ${total} questões (${percentage}%)`;
        console.log('[randomQuiz] Resultado final:', message);
        
        // Opcional: mostrar alerta com o resultado
        if (percentage >= 80) {
          alert(`🎉 Excelente! ${message}`);
        } else if (percentage >= 60) {
          alert(`👍 Bom trabalho! ${message}`);
        } else {
          alert(`📚 Continue estudando! ${message}`);
        }
        
      } catch (error) {
        console.error('[randomQuiz] Erro ao calcular score:', error);
        alert('Erro ao calcular o score. Verifique o console para mais detalhes.');
      }
    });
  } else {
    console.log('[randomQuiz] Botão de score NÃO encontrado');
  }
});