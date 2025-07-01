# Notas do projeto

Se já subiu:

> docker-compose start

Para deletar tudo:

> docker container prune

Se tá subindo:

> `docker compose up --build --force-recreate`

[Backend Page](http://localhost:8080/backend/index.html)

## Notas parte 2

MENU JSON
Funcionalidade 1 (Json– Inserir): Considerando a Classe A, crie um form que envie um JSON para salvar os dados no BD;
Funcionalidade 2 (Json - Listar: Considerando a classe A, crie uma opção no menu para listar JSON de acordo com um filtro de seleção inicial;
Funcionalidade 3 (Login, Filtro, Session, Token e Cookie): Desenvolva o login e filtro para que apenas usuários registrados possam acessar o sistema. No cadastro inicial de perfil, o usuário registrará a cor de fundo e da fonte do texto. Assim, no menu do sistema as preferências de cor deverão ser consideradas. As informações de cores serão salvas em cookies. O token de acesso será salvo na session.

### Classe A: Quiz

No seu contexto, a Classe A pode ser o próprio **Quiz**. Assim, as operações de inserir/listar via JSON serão para quizzes.

---

### Implementação das Funcionalidades

#### 1. Inserir Quiz via JSON

- **Formulário:** Crie uma página (ex: `quiz-json-form.jsp`) com campos para título, assunto, etc.
- **Envio:** Ao submeter, use JavaScript para montar um objeto Quiz e enviar via `fetch` (POST, `Content-Type: application/json`) para `/backend/quiz-json`.
- **Servlet:** Implemente `QuizJsonServlet` para receber o JSON, converter para objeto Java (usando Gson ou Jackson), e salvar no banco via `QuizDao`.

#### 2. Listar Quizzes em JSON com Filtro

- **Menu:** Adicione no menu uma opção "Listar Quizzes (JSON)".
- **Filtro:** Exiba um campo para filtrar por assunto ou criador.
- **Listagem:** Ao filtrar, envie um GET para `/backend/quiz-json?assunto=...` e mostre o JSON retornado na tela.

---

#### 3. Login, Filtro, Session, Token e Cookie

- **Login:** Só o Host pode criar quizzes. Crie uma tela de login para o Host.
- **Preferências:** No cadastro/login, o Host escolhe cor de fundo e fonte. Salve essas preferências em cookies.
- **Session/Token:** Gere um token (UUID) e salve na session ao logar.
- **Filtro:** Implemente um Java Filter que só permite acesso às páginas de criação/listagem de quizzes se o token estiver na session.
- **Menu:** O menu lê os cookies de cor e aplica no CSS.

### Resumindo Possível Solução

- **Classe A:** Quiz
- **Inserir/Listar:** Via JSON, usando QuizJsonServlet
- **Login:** Apenas Host, com preferências salvas em cookies
- **Filtro:** Java Filter para proteger rotas
- **Menu:** Aplica preferências de cor do cookie

Se quiser, posso começar pelo formulário de envio de Quiz via JSON e o servlet correspondente. Confirma seguir com Quiz como Classe A? Se sim, posso gerar o código inicial para o formulário e servlet!
