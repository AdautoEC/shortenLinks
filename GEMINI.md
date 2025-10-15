# GEMINI.md - Análise do Projeto ShortenLinks

## Visão Geral do Projeto

**ShortenLinks** é um aplicativo Android nativo desenvolvido em Kotlin que permite aos usuários encurtar e expandir URLs. O usuário pode inserir uma URL longa para gerar uma versão encurtada, ou inserir uma URL já encurtada para obter a original. O aplicativo interage com uma API externa para realizar essas operações e mantém um histórico dos links recém-processados, permitindo que o usuário copie tanto a URL original quanto a encurtada para a área de transferência.

A interface é construída com Jetpack Compose, proporcionando uma experiência de usuário moderna e reativa.

## Arquitetura

O projeto adota os princípios da **Clean Architecture** (Arquitetura Limpa), organizando o código em três camadas principais: **Data**, **Domain** e **Presentation**. Essa separação de responsabilidades aumenta a testabilidade, manutenibilidade e escalabilidade do código.

Para a camada de apresentação, o padrão de design **MVI (Model-View-Intent)** é utilizado para gerenciar o estado da interface de forma previsível e unidirecional.

- **Camada de Apresentação (Presentation):**
  - Construída inteiramente com **Jetpack Compose**.
  - A `ShortenScreen` emite eventos (Intents) para o `ShortenViewModel` (ex: `OnShortenClick`).
  - O `ShortenViewModel` processa os eventos, identifica se a URL deve ser encurtada ou expandida, executa a lógica de negócios (invocando o `UseCase` apropriado) e atualiza o `ShortenScreenState`.
  - A UI observa as mudanças no `ShortenScreenState` e se recompõe para refletir o novo estado (ex: exibir um loader, o resultado ou um erro).

- **Camada de Domínio (Domain):**
  - Contém a lógica de negócios central e é independente de frameworks.
  - `ShortenUrlUseCase`: Orquestra a ação de encurtar uma URL.
  - `GetOriginalUrlUseCase`: Orquestra a ação de obter a URL original a partir de um alias.
  - `ShortenRepository` (Interface): Define o contrato para as operações de dados, desacoplando o domínio da implementação da camada de dados.
  - Modelos de domínio como `Shorten` e `OriginalUrl`.

- **Camada de Dados (Data):**
  - `ShortenRepositoryImpl`: Implementação concreta do repositório. Utiliza o `ShortenService` para realizar as chamadas de rede.
  - `ShortenService`: Interface do **Retrofit** que define os endpoints da API (`/api/alias` para POST e `/api/alias/{alias}` for GET).
  - `model`: Contém os DTOs (Data Transfer Objects) como `ShortenRequest`, `ShortenResponse` e `GetUrlResponse` para serialização/deserialização com GSON.
  - `mapper`: Contém funções para mapear os DTOs da camada de dados para os modelos da camada de domínio.

## Tecnologias e Bibliotecas

- **Linguagem:** [Kotlin](https://kotlinlang.org/)
- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) para uma UI declarativa e moderna.
- **Injeção de Dependência:** [Hilt](https://dagger.dev/hilt/) para gerenciar dependências em todo o aplicativo.
- **Programação Assíncrona:** [Kotlin Coroutines & Flow](https://kotlinlang.org/docs/coroutines-guide.html) para gerenciar operações em background e fluxos de dados reativos.
- **Networking:** [Retrofit](https://square.github.io/retrofit/) para chamadas de API type-safe, com [OkHttp](https://square.github.io/okhttp/) como cliente HTTP.
- **Testes:**
  - **Unitários:** [JUnit](https://junit.org/junit5/), [MockK](https://mockk.io/) (para mocks).
  - **Fluxos (Flow):** [Turbine](https://github.com/cashapp/turbine) para testar `Flows` de forma robusta.
  - **UI (Instrumentados):** [Espresso](https://developer.android.com/training/testing/espresso) e `createComposeRule` para testar os Composables.
  - **Testes com Hilt:** `HiltAndroidTest` para injetar dependências em testes.

## Estrutura de Código

O código-fonte está organizado da seguinte forma:

```
app/src/
├── main/java/br/com/warehouse/shortenlinks/
│   ├── core/                 # Módulos e modelos compartilhados (network, presentation models)
│   ├── features/shorten/     # Feature principal de encurtamento de link
│   │   ├── data/             # Implementação do repositório, serviço de API e modelos de dados
│   │   ├── di/               # Módulo Hilt específico da feature
│   │   ├── domain/           # Use cases e interface do repositório
│   │   └── presentation/     # ViewModel, Screen (Compose), State, Event e Components
│   ├── ui/                   # Tema do Jetpack Compose (Cores, Tipografia, Espaçamento)
│   ├── MainActivity.kt       # Ponto de entrada da Activity
│   └── ShortenLinksApp.kt    # Classe Application anotada com @HiltAndroidApp
│
├── test/java/                # Testes unitários
└── androidTest/java/         # Testes instrumentados (UI)
```

## Como Executar

1.  Clone o repositório.
2.  Abra o projeto no Android Studio.
3.  Execute o aplicativo em um emulador ou dispositivo físico.

Alternativamente, você pode usar o Gradle Wrapper via linha de comando para instalar o app:

```bash
./gradlew installDebug
```

## Como Testar

O projeto possui uma cobertura de testes robusta.

- **Para rodar os testes unitários:**

```bash
./gradlew testDebugUnitTest
```

- **Para rodar os testes instrumentados (UI tests):**

```bash
./gradlew connectedAndroidTest
```