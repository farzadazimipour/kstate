# Changelog

## [Unreleased] - 2024-12-19

### Added
- **Core Library**: Complete state machine implementation with `DefaultStateMachine`
- **Error Handling**: Comprehensive error handling in state machine to prevent crashes from reducer and effect handler exceptions
- **Event Dispatch**: Robust event dispatch with failure handling and logging
- **Testing Utilities**: `TestStateMachine` and `TestEffectHandler` for easier unit testing
- **Logging System**: Configurable logging with `DefaultLogger` and `NoOpLogger` implementations
- **Compose Integration**: `collectStateMachineAsStateWithLifecycle()` extension for Jetpack Compose
- **ViewModel Integration**: StateMachine integration with Android ViewModels
- **Sample Application**: Complete counter app demonstrating MVI pattern usage
- **Documentation**: Comprehensive inline documentation for all core interfaces
- **Unit Tests**: Example unit tests demonstrating testing patterns with `LoginReducerTest`

### Technical Details
- Modern Kotlin practices with `data object` for sealed interface members
- Coroutine-based architecture with proper cancellation handling
- Type-safe MVI pattern with `State`, `Event`, and `Effect` interfaces
- Android SDK 36 compatibility with Java 21 target
- Modular architecture: `kstate-core`, `kstate-compose`, `kstate-viewmodel`

### Project Structure
- **kstate-core**: Core state machine logic and interfaces
- **kstate-compose**: Jetpack Compose integration utilities
- **kstate-viewmodel**: Android ViewModel integration
- **sample-app**: Demonstration app with counter example