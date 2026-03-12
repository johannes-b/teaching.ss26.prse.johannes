# Implementation & Test Plan — Issue #1: Subtraction Operation

## Implementation Plan

### Step 1 — Create `SubtractOperation.java`

**File:** `src/main/java/at/jku/se/calculator/operators/SubtractOperation.java`

Model after `AddOperation.java`:
- Implement `ICalculationOperation`
- In `calculate(String txt)`: split input on `"\\-"`, validate both parts are integers via `isInteger()`, return the difference as a String
- Log with Log4j (same pattern as `AddOperation`)
- Throw `IllegalArgumentException` for non-integer operands or malformed input (not exactly 2 tokens after split)

Key difference from `AddOperation`: split delimiter is `"\\-"` instead of `"\\+"`, arithmetic is subtraction (`a - b`).

### Step 2 — Register in `OperationFactory`

**File:** `src/main/java/at/jku/se/calculator/factory/OperationFactory.java`

- Add `import at.jku.se.calculator.operators.SubtractOperation;`
- In the `switch` statement, replace:
  ```java
  case SUB:
      return new NullOperation();
  ```
  with:
  ```java
  case SUB:
      return new SubtractOperation();
  ```

---

## Test Plan

### `TestSubtractOperation` (new file)

**File:** `src/test/java/at/jku/se/calculator/operators/TestSubtractOperation.java`

Mirror the structure of `TestAddOperation.java`. Required test cases per acceptance criteria:

| Test method | Input | Expected outcome |
|---|---|---|
| `testCalculatePositiveResult` | `"8-3"` | result equals `5` |
| `testCalculateNegativeResult` | `"3-8"` | result equals `-5` |
| `testCalculateExceptionFirstOperand` | `"xyz-3"` | `IllegalArgumentException` thrown |
| `testCalculateExceptionSecondOperand` | `"3-abc"` | `IllegalArgumentException` thrown |
| `testCalculateMalformedInput` | `"3+3"` | `IllegalArgumentException` thrown |

### `TestOperationFactory` (extend existing)

**File:** `src/test/java/at/jku/se/calculator/factory/TestOperationFactory.java`

Add a test case verifying that `OperationFactory.getOperation(CalcAction.SUB)` returns an instance of `SubtractOperation` (not `NullOperation`).

---

## Acceptance Criteria Checklist

| Criterion | Covered by |
|---|---|
| `8 - 3 = 5` | `testCalculatePositiveResult` |
| `3 - 8 = -5` | `testCalculateNegativeResult` |
| Invalid input → `Error` display | `testCalculateException*` + controller's existing error handling |
| `OperationFactory` returns `SubtractOperation` for `SUB` | Step 2 + `TestOperationFactory` |
| Unit tests for positive, negative, invalid | `TestSubtractOperation` |
| No new PMD violations | Follow identical style/structure to `AddOperation` |
