# Implementation & Test Plan — Issue #2: Multiplication Operation

## Implementation Plan

### Step 1 — Create `MultiplyOperation.java`

**File:** `src/main/java/at/jku/se/calculator/operators/MultiplyOperation.java`

Model after `AddOperation.java`:
- Implement `ICalculationOperation`
- In `calculate(String txt)`: split input on `"\\*"`, validate both parts are integers via `isInteger()`, return the product as a String
- Log with Log4j (same pattern as `AddOperation`)
- Throw `IllegalArgumentException` for non-integer operands or malformed input (not exactly 2 tokens after split)

Key difference from `AddOperation`: split delimiter is `"\\*"` instead of `"\\+"`, arithmetic is multiplication (`a * b`).

### Step 2 — Register in `OperationFactory`

**File:** `src/main/java/at/jku/se/calculator/factory/OperationFactory.java`

- Add `import at.jku.se.calculator.operators.MultiplyOperation;`
- In the `switch` statement, replace:
  ```java
  case MULT:
      return new NullOperation();
  ```
  with:
  ```java
  case MULT:
      return new MultiplyOperation();
  ```

---

## Test Plan

### `TestMultiplyOperation` (new file)

**File:** `src/test/java/at/jku/se/calculator/operators/TestMultiplyOperation.java`

Mirror the structure of `TestAddOperation.java`. Required test cases per acceptance criteria:

| Test method | Input | Expected outcome |
|---|---|---|
| `testCalculateStandardProduct` | `"4*3"` | result equals `12` |
| `testCalculateMultiplyByZero` | `"0*9"` | result equals `0` |
| `testCalculateExceptionFirstOperand` | `"xyz*3"` | `IllegalArgumentException` thrown |
| `testCalculateExceptionSecondOperand` | `"4*abc"` | `IllegalArgumentException` thrown |
| `testCalculateMalformedInput` | `"4+3"` | `IllegalArgumentException` thrown |

### `TestOperationFactory` (extend existing)

**File:** `src/test/java/at/jku/se/calculator/factory/TestOperationFactory.java`

Add a test case verifying that `OperationFactory.getOperation(CalcAction.MULT)` returns an instance of `MultiplyOperation` (not `NullOperation`).

---

## Acceptance Criteria Checklist

| Criterion | Covered by |
|---|---|
| `4 x 3 = 12` | `testCalculateStandardProduct` |
| `0 x 9 = 0` | `testCalculateMultiplyByZero` |
| Invalid input → `Error` display | `testCalculateException*` + controller's existing error handling |
| `OperationFactory` returns `MultiplyOperation` for `MULT` | Step 2 + `TestOperationFactory` |
| Unit tests for standard product, multiply by zero, invalid input | `TestMultiplyOperation` |
| No new PMD violations | Follow identical style/structure to `AddOperation` |
