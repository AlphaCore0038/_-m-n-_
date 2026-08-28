# Matrix Calculator — Implementation Plan

## Phase 1: Project Foundation

### Files to Create:
- `build.gradle.kts` (project-level)
- `app/build.gradle.kts`
- `settings.gradle.kts`
- `gradle.properties`
- `gradle/libs.versions.toml`
- `app/src/main/AndroidManifest.xml`
- `app/src/main/java/com/calc/matrixcalculator/MainActivity.kt`
- `app/src/main/java/com/calc/matrixcalculator/ui/theme/Color.kt`
- `app/src/main/java/com/calc/matrixcalculator/ui/theme/Theme.kt`
- `app/src/main/java/com/calc/matrixcalculator/ui/theme/Type.kt`
- `app/src/main/res/values/strings.xml`

## Phase 2: Math Engine Data Models

### Files to Create:
- `app/src/main/java/com/calc/matrixcalculator/math/MathUtils.kt`
- `app/src/main/java/com/calc/matrixcalculator/math/Matrix.kt`
- `app/src/main/java/com/calc/matrixcalculator/math/Vector.kt`
- `app/src/test/java/com/calc/matrixcalculator/math/MathUtilsTest.kt`
- `app/src/test/java/com/calc/matrixcalculator/math/MatrixTest.kt`
- `app/src/test/java/com/calc/matrixcalculator/math/VectorTest.kt`

## Phase 3: Basic Matrix Operations

### Files to Create/Update:
- `app/src/main/java/com/calc/matrixcalculator/math/MatrixOperations.kt`
- `app/src/test/java/com/calc/matrixcalculator/math/MatrixOperationsTest.kt`

### Operations:
- Addition, Subtraction, Multiplication, Scalar multiplication, Transpose, Power, Trace

## Phase 4: Advanced Matrix Operations

### Updates to:
- `MatrixOperations.kt`
- `MatrixOperationsTest.kt`

### Operations:
- Determinant, Inverse, Rank, REF, RREF, Gaussian elimination, Gauss-Jordan

## Phase 5: Matrix Classification

### Updates to:
- `MatrixOperations.kt`
- `MatrixOperationsTest.kt`

### Classifications:
- Symmetric, Skew-symmetric, Orthogonal, Singular, Positive definite, Negative definite

## Phase 6: Vector Operations

### Files to Create:
- `app/src/main/java/com/calc/matrixcalculator/math/VectorOperations.kt`
- `app/src/test/java/com/calc/matrixcalculator/math/VectorOperationsTest.kt`

### Operations:
- Addition, Subtraction, Scalar multiplication, Dot product, Cross product, Magnitude, Unit vector, Angle, Projection, Parallel, Perpendicular

## Phase 7: Compose UI

### Files to Create:
- `app/src/main/java/com/calc/matrixcalculator/ui/navigation/NavGraph.kt`
- `app/src/main/java/com/calc/matrixcalculator/ui/screens/HomeScreen.kt`
- `app/src/main/java/com/calc/matrixcalculator/ui/screens/matrix/MatrixScreen.kt`
- `app/src/main/java/com/calc/matrixcalculator/ui/screens/vector/VectorScreen.kt`
- `app/src/main/java/com/calc/matrixcalculator/ui/components/MatrixGrid.kt`
- `app/src/main/java/com/calc/matrixcalculator/ui/components/VectorInput.kt`
- `app/src/main/java/com/calc/matrixcalculator/ui/components/ResultDisplay.kt`

## Phase 8: ViewModels

### Files to Create:
- `app/src/main/java/com/calc/matrixcalculator/viewmodel/MatrixViewModel.kt`
- `app/src/main/java/com/calc/matrixcalculator/viewmodel/VectorViewModel.kt`
- `app/src/test/java/com/calc/matrixcalculator/viewmodel/MatrixViewModelTest.kt`
- `app/src/test/java/com/calc/matrixcalculator/viewmodel/VectorViewModelTest.kt`

## Phase 9-10: Integration, Testing, Polish
- Full integration testing
- UI polish
- Final verification: `./gradlew test` and `./gradlew assembleDebug`
