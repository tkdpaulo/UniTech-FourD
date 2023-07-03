# Descrição do Projeto

Este projeto é um sistema de gerenciamento de professores e aulas, desenvolvido com o objetivo de facilitar o processo de cadastro, aprovação e gerenciamento de professores, além de permitir o cadastro e manipulação de aulas relacionadas a cada professor.

# Configuração do Ambiente
## Docker
O projeto já está configurado para ser executado em um ambiente Docker. Para executar o projeto, siga os passos abaixo:

1. Certifique-se de ter o Docker instalado e em execução na sua máquina.
2. Abra o terminal na pasta raiz do projeto.
3. Execute o seguinte comando:
```bash
docker-compose up --build
```

Esse comando irá criar e executar os containers necessários para o projeto, incluindo um banco de dados MongoDB.

O projeto será executado na porta 8080 do seu localhost, e a URI base para as requisições será **`http://localhost:8080`**.

# Autenticação
Para acessar as funcionalidades do sistema, é necessário autenticar-se. A autenticação é realizada através do endpoint `/api/auth/login` utilizando o método HTTP POST.

Exemplo de requisição usando cURL:

```bash
curl --location 'http://localhost:8080/api/auth/login' \
--header 'Content-Type: application/json' \
--data '{
    "username":"admin",
    "password": "admin"
}'
```
O payload do token JWT será retornado no cookie da resposta. Para realizar as outras chamadas, é necessário incluir esse cookie nas requisições.

# Funcionalidades
## Cadastro de Professor
Para cadastrar um professor, faça uma requisição HTTP POST para o endpoint `/teachers`. É necessário fornecer os seguintes campos:

* **name**: nome do professor.
* **username**: nome de usuário do professor.
* **password**: senha do professor.
* **admin**: define o nível de acesso do usuário (true para administrador, false para usuário comum).
* **authorities**: lista de permissões do professor. As opções disponíveis são ["ADMIN", "USER"].

Exemplo de requisição usando cURL:

```bash
curl --location 'http://localhost:8080/teachers' \
--header 'Cookie: ${TOKEN_AUTH}' \
--header 'Content-Type: application/json' \
--data '{
    "name": "${NAME}",
    "username": "${USERNAME}",
    "password": "${PASSWORD}",
    "admin": ${BOOLEAN},
    "authorities": [
      "${ROLE}"
    ]
}'
```

## Edição de Professor
Para editar um professor, faça uma requisição HTTP PUT para o endpoint `/teachers`. É necessário fornecer os mesmos campos descritos acima.

Exemplo de requisição usando cURL:

```bash
curl --location --request PUT 'http://localhost:8080/teachers' \
--header 'Content-Type: application/json' \
--header 'Cookie: ${TOKEN_AUTH}' \
--data '{
"name": "${NAME}",
"username": "${USERNAME}",
"password": "${PASSWORD}",
"admin": ${BOOLEAN},
"authorities": [
"${ROLE}"
]
}'
```

## Aprovação ou Rejeição de Professor
Para aprovar ou rejeitar um professor, faça uma requisição HTTP PUT para o endpoint `/teachers/{teacherId}/approve?approve={boolean}`. Substitua **{teacherId}** pelo ID do professor e **{boolean}** por **true** para aprovar ou **false** para rejeitar.

Exemplo de requisição usando cURL:

```bash
curl --location --request PUT 'http://localhost:8080/teachers/{teacherId}/approve?approve={boolean}' \
--header 'Cookie: ${TOKEN_AUTH}'
```

## Consulta de Professores
Para retornar todos os professores cadastrados, faça uma requisição HTTP GET para o endpoint `/teachers/getAll`.

Exemplo de requisição usando cURL:

```bash
curl --location 'http://localhost:8080/teachers/getAll' \
--header 'Cookie: ${TOKEN_AUTH}'
```

## Cadastro de Aula
Para cadastrar uma aula para o professor autenticado, faça uma requisição HTTP POST para o endpoint `/lessons`. É necessário fornecer os seguintes campos:

* **title**: título da aula.
* **description**: descrição da aula.
* **expectedDate**: data esperada para a aula no formato "YYYY-MM-DD".
Exemplo de requisição usando cURL:

```bash
curl --location 'http://localhost:8080/lessons' \
--header 'Cookie: ${TOKEN_AUTH}' \
--header 'Content-Type: application/json' \
--data '{
"title":"${TITLE}",
"description":"${DESCRIPTION}",
"expectedDate":"${YYYY-MM-DD}"
}'
```

## Edição de Aula
Para editar uma aula, faça uma requisição HTTP PUT para o endpoint `/lessons/{lessonId}`. Substitua {lessonId} pelo ID da aula e forneça os mesmos campos descritos acima.

Exemplo de requisição usando cURL:

```bash
curl --location --request PUT 'http://localhost:8080/lessons/{lessonId}' \
--header 'Cookie: ${TOKEN_AUTH}' \
--header 'Content-Type: application/json' \
--data '{
"title":"${TITLE}",
"description":"${DESCRIPTION}",
"expectedDate":"${YYYY-MM-DD}"
}'
```

## Consulta de Aulas
Para retornar todas as aulas do professor autenticado, faça uma requisição HTTP GET para o endpoint `/lessons`.

Exemplo de requisição usando cURL:

```bash
curl --location 'http://localhost:8080/lessons' \
--header 'Cookie: ${TOKEN_AUTH}'
```

## Consulta de Aulas por Professor
Para retornar todas as aulas de um professor específico, faça uma requisição HTTP GET para o endpoint `/lessons/teacher/{teacherId}`. Substitua {teacherId} pelo ID do professor desejado.

Exemplo de requisição usando cURL:

```bash
curl --location 'http://localhost:8080/lessons/teacher/{teacherId}' \
--header 'Cookie: ${TOKEN_AUTH}'
```

# Observações
* O payload do token JWT é retornado no cookie da requisição de login.
* Durante a inicialização do sistema, um usuário com as seguintes credenciais é criado:
  * Usuário: admin
  * Senha: admin
* Certifique-se de substituir as variáveis entre chaves (${VARIAVEL}) pelos valores corretos ao realizar as requisições.
* Utilize o endpoint de login para obter a autenticação antes de fazer as demais chamadas.