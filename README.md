# ShortenLinks

An Android application built with Kotlin and Jetpack Compose that allows users to shorten and expand URLs using a remote service.

## Features

- **Shorten URLs**: Enter a long URL and get a shortened version.
- **Expand URLs**: Enter a shortened URL to retrieve the original one.
- **Link History**: View a list of your recently processed links.
- **Copy to Clipboard**: Easily copy both shortened and original URLs.

## Architecture

The project follows the principles of **Clean Architecture**, separating concerns into three distinct layers:

- **Presentation Layer**: Built entirely with Jetpack Compose, this layer uses the **MVI (Model-View-Intent)** pattern to manage UI state. The `ShortenScreen` sends events to the `ShortenViewModel`, which in turn updates the UI state.
- **Domain Layer**: This layer contains the core business logic of the application. It is framework-independent and includes use cases like `ShortenUrlUseCase` and `GetOriginalUrlUseCase`.
- **Data Layer**: Responsible for data operations, this layer implements the repository pattern and communicates with the remote API using Retrofit.

## Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Dependency Injection**: [Hilt](https://dagger.dev/hilt/)
- **Asynchronous Programming**: [Kotlin Coroutines & Flow](https://kotlinlang.org/docs/coroutines-guide.html)
- **Networking**: [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/)
- **Testing**:
    - Unit Tests: [JUnit](https://junit.org/junit5/) & [MockK](https://mockk.io/)
    - UI Tests: [Espresso](https://developer.android.com/training/testing/espresso) & [Compose Test Rule](https://developer.android.com/jetpack/compose/testing)

## How to Build and Run

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/ShortenLinks.git
   ```
2. Open the project in Android Studio.
3. Run the app on an emulator or a physical device.

Alternatively, you can use the Gradle wrapper to build and install the app:

```bash
./gradlew installDebug
```

## How to Test

The project has a comprehensive test suite.

### Unit Tests

To run the unit tests, execute the following command:

```bash
./gradlew testDebugUnitTest
```

### Instrumented Tests

To run the instrumented (UI) tests, make sure you have a device or emulator connected and run:

```bash
./gradlew connectedAndroidTest
```

## License

This project is licensed under the MIT License - see the [LICENSE](https://mit-license.org/) file for details.
