# (m, n)

A Matrix and Vector Calculator Android application built with Kotlin and Jetpack Compose. Designed for engineering students to quickly perform common linear algebra operations.

## Features

### Matrix

- Create and input matrices (up to 10x10)
- Quick-fill with zero matrix or identity matrix
- Addition, subtraction, multiplication
- Scalar multiplication and matrix power
- Transpose, trace, determinant
- Inverse
- Rank
- Row Echelon Form (REF) and Reduced Row Echelon Form (RREF)
- Gaussian elimination and Gauss-Jordan elimination
- Classification: symmetric, skew-symmetric, orthogonal, singular, positive definite, negative definite
- Chain results back as input for further computation

### Vector

- Create and input vectors (up to 10 dimensions)
- Addition, subtraction, scalar multiplication
- Dot product and cross product (3D)
- Magnitude and unit vector
- Angle between vectors (degrees)
- Vector projection
- Check if parallel or perpendicular
- Chain results back as input for further computation

## Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- Android

The mathematical engine is implemented in pure Kotlin with no external mathematics libraries.

## Project Structure

```
app/src/main/java/com/calc/matrixcalculator/
├── math/            # Pure Kotlin math engine
│   ├── Matrix.kt
│   ├── Vector.kt
│   ├── MatrixOperations.kt
│   ├── VectorOperations.kt
│   ├── MathUtils.kt
│   └── MathResult.kt
├── viewmodel/       # UI state management
│   ├── MatrixViewModel.kt
│   └── VectorViewModel.kt
└── ui/
    ├── screens/     # Compose UI screens
    └── theme/       # Material 3 theme (black & white)
```

## Getting Started

1. Clone the repository.
2. Open the project in Android Studio.
3. Let Gradle sync.
4. Run on an emulator or physical Android device.

## Testing

The project includes JUnit unit tests covering the math engine (matrix operations, vector operations, utilities).

```bash
.\gradlew.bat testDebugUnitTest
```

## Design Philosophy

The app uses a simple, straightforward UI built with basic Compose components. The focus is on functionality and ease of use rather than visual polish.

## Logo

The application logo is the "(m, n)" logo, used as the launcher icon and displayed on the home screen.

## License

No license has been specified yet.
