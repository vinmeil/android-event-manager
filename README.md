# Event Manager

## Overview

This project is an Event Manager application developed as part of a coursework assignment. The project uses Java for the backend and Android for the frontend. The backend manages event data and categories, while the frontend provides an interactive interface for users to create, view, and manage events and categories.

## Features

- **Event Management**: Create, view, and manage events.
- **Category Management**: Create, view, and manage event categories.
- **Interactive Interface**: User-friendly interface for managing events and categories.
- **Data Persistence**: Save event and category data using Room database.

## Installation

To set up the project locally, follow these steps:

1. **Clone the repository**:

```sh
git clone https://github.com/yourusername/event-manager.git
cd <repository-directory>
```

2. **Open the project in Android Studio**:
   - Open Android Studio.
   - Select "Open an existing project".
   - Navigate to the event-manager directory and select it.

## Usage

1. **Build and run the application**:

   - Connect an Android device or start an Android emulator.
   - Click the "Run" button in Android Studio.

2. **Use the application**:
   - Navigate through the app to create, view, and manage events and categories.
   - Use the navigation drawer to switch between different sections of the app.

## Key Files

- **MainActivity.java**: Main entry point for the Android application.
- **NewEventCategoryActivity.java**: Activity for creating new event categories.
- **CategoryViewModel.java**: ViewModel for managing category data.
- **Event.java**: Data model for events.
- **NewEventUtils.java**: Utility class for event-related operations.

## Technologies Used

- **Java**: For the backend and Android application.
- **Android**: For the frontend.
- **Room**: For data persistence.
- **ViewModel**: For managing UI-related data in a lifecycle-conscious way.

## Contributing

Contributions are welcome! Please follow these steps to contribute:

1. **Fork the repository**.
2. **Create a new branch**:
   ```sh
   git checkout -b feature/your-feature-name
   ```
3. **Make your changes**.
4. **Commit your changes**:
   ```sh
   git commit -m 'Add some feature'
   ```
5. **Push to the branch**:
   ```sh
   git push origin feature/your-feature-name
   ```
6. **Open a pull request**.

## License

This project is not licensed and is intended for educational purposes. Please do not use it to create a public product. If you wish to use it for private, educational purposes, please contact the authors.

## Authors

- Vincent Wesley Liem - [vinmeil](https://github.com/vinmeil) - Main author

## Contact

For any questions or feedback, please contact Vincent Liem at vincent.wesley.liem@gmail.com.
