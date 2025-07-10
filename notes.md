# Notas do projeto

Se já subiu:

> docker-compose start

Para deletar tudo:

> docker container prune

Se tá subindo:

> `docker compose up --build --force-recreate`

[Backend Page](http://localhost:8080/backend/index.html)

## Notas parte 2

- [X] HostDAO
- [X] Host Servlet JSON
- [X] Form login mandando JSON
- [X] Form Subscribe mandando JSON
- [X] host-list podendo editar, deletar e filtrar por nome, id e email selecionando forma de filtragem e podendo escrever num input buscar
- [X] Só permitir acessar os servlets e os botões de list hosts(host-list.jsp), ver quizes(quiz-list) e criar quizes (quiz-form) quando tiver logado. Assim como só poder acessar essas páginas quando estiver logado e as páginas de Questions e Options.
- [X] Atualizar os quizes para json
- [X] Permitir editar os Quizes no Dao
- [X] Permitir editar um quiz no quiz-list direcionando para um  edit-quiz-form passando os valores atuais do quiz e fazendo um put
