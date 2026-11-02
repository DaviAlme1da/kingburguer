# KingBurguer

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Material 3](https://img.shields.io/badge/Material%203-757575?style=for-the-badge&logo=materialdesign&logoColor=white)
![Hilt](https://img.shields.io/badge/Hilt-4285F4?style=for-the-badge&logo=dagger&logoColor=white)
![Retrofit](https://img.shields.io/badge/Retrofit-48B983?style=for-the-badge)

Aplicativo Android de uma hamburgueria com geração de cupons promocionais, feito como projeto de estudo e portfólio, com foco em **consumo de API REST, autenticação com sessão persistente e MVVM**.

---

## 📌 Sobre o projeto

O KingBurguer permite que o usuário crie uma conta, entre no app, veja os produtos da hamburgueria por categoria, abra os detalhes de um produto e **gere um cupom promocional** para ele. Os cupons ficam numa lista própria, com filtro entre ativos, expirados e todos.

O app **consome uma API REST** hospedada por terceiros, criada para fins didáticos. O back-end não faz parte deste repositório. Aqui está apenas o aplicativo Android.

---

## Funcionalidades

- **Cadastro:** nome, e-mail, senha, confirmação de senha, CPF e data de nascimento, com validação em tempo real e máscaras nos campos de CPF e data. O botão de cadastrar só é habilitado com o formulário válido.
- **Login:** e-mail e senha, com opção de **manter conectado**.
- **Sessão persistente:** ao abrir o app, uma verificação decide o destino. Com token válido, vai direto para a Home. Com token expirado, tenta **renovar o token** automaticamente. Sem sessão, vai para o login.
- **Home:** produto em destaque e lista de categorias com seus produtos e imagens.
- **Produto:** detalhes do produto e botão para **gerar cupom**.
- **Cupons:** lista **paginada** (carrega mais ao rolar) com filtro por **ativos, expirados ou todos**.
- **Perfil:** dados do usuário logado (nome, e-mail, CPF e data de nascimento).
- **Navegação:** barra inferior entre Home, Cupons e Perfil, e botão de sair na barra superior.
- **Tratamento de erros:** mensagens de erro retornadas pela API são exibidas ao usuário, com estados de carregamento nas telas.

---

## Arquitetura

**MVVM**, organizada por funcionalidade (*package by feature*). Cada funcionalidade separa as camadas que precisa (`presentation`, `data` e, quando faz sentido, `domain` e `di`).

```
UI (Compose) ──► ViewModel ──► Repository (interface) ──► RepositoryImpl ──► Retrofit / DataStore
```

```
app/src/main/java/dev/tiagoaguiar/kingburguer/
├── core/          # API (Retrofit), DI, armazenamento local, validações, navegação
├── compose/       # tema (cores, tipografia) e componentes reutilizáveis
├── common/        # utilitários (chamada de API, datas, formatação)
├── auth/          # login e cadastro
├── home/          # categorias e produto em destaque
├── product/       # detalhes do produto e geração de cupom
├── main/          # estrutura principal com navegação inferior
├── profile/       # perfil do usuário
└── viewmodels/    # ViewModel da lista de cupons
```

- **presentation:** telas em Compose e ViewModels com `StateFlow`.
- **data:** serviço Retrofit, modelos de requisição e resposta e implementação dos repositórios.
- **domain:** interfaces de repositório e casos de uso (como a verificação de sessão).
- **di:** módulos Hilt que ligam as implementações às interfaces.

### Decisões de projeto

- **Resultado tipado:** as chamadas de rede passam por uma função única (`apiCall`) que devolve `ApiResult.Success` ou `ApiResult.Error`, sem lançar exceções para a UI.
- **Estado de tela em um único objeto:** cada tela expõe um `UiState` via `StateFlow`, e a interface apenas reage a ele.
- **Validadores separados:** cada regra de formulário fica em uma classe própria (e-mail, nome, senha, confirmação, CPF e data de nascimento).
- **Credenciais no DataStore:** o token de acesso, o de renovação e o horário de expiração ficam guardados localmente, e o app decide a renovação comparando com o horário atual.
- **Paginação controlada pelo estado:** a lista de cupons guarda página atual, total e se há mais itens, evitando carregar a mesma página duas vezes.

---

## Regras de negócio

| Regra | Detalhe |
|-------|---------|
| Nome | Obrigatório, mínimo de 3 caracteres |
| E-mail | Obrigatório e em formato válido |
| Senha | Obrigatória, mínimo de 8 caracteres |
| Confirmação de senha | Deve ser igual à senha |
| CPF | Obrigatório, com máscara `###.###.###-##` completa |
| Data de nascimento | Formato `dd/MM/yyyy`, data válida e não pode ser futura |
| Sessão | Token válido entra direto. Token expirado tenta renovar. Falha na renovação volta ao login |
| Cupom | Gerado por produto através da API. A validade é definida pela API |
| Lista de cupons | Paginada, com filtro entre ativos, expirados e todos |

---

## Design

- Interface com **Jetpack Compose** e **Material 3**, com tema e tipografia centralizados em `compose/theme`.
- Componentes reutilizáveis (botão, campo de texto, título e alerta) em `compose/component`.
- Imagens dos produtos carregadas da rede com **Coil**.

---

## Tecnologias

| Área | Uso |
|------|-----|
| Linguagem | Kotlin |
| Interface | Jetpack Compose, Material 3, Navigation Compose |
| Injeção de dependência | Hilt |
| Rede | Retrofit, OkHttp (com log de requisições) e Gson |
| Persistência local | DataStore Preferences |
| Assíncrono | Coroutines e Flow |
| Imagens | Coil |

---

## Próximos passos

Ideias para evoluir o projeto:

- Testes unitários para os validadores, os ViewModels e a verificação de sessão
- Validação real do CPF (dígitos verificadores), e não apenas da máscara
- Camada `domain` completa, com casos de uso para as demais funcionalidades
- Cache local para abrir a Home sem conexão
- Tela de detalhe do cupom, com opção de copiar o código

---

## Autor

Desenvolvido por **Davi Lucas de Almeida**.
Estudante de Análise e Desenvolvimento de Sistemas (ADS).
