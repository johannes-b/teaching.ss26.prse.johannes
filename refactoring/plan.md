# Refactoring Plan

## Overview

Analysis of the current codebase revealed four concrete design improvements.
They are ordered by impact (highest first) and are independent of each other
unless stated otherwise.

---

## R1 — Extract `AbstractBinaryOperation` base class

**Problem: Code duplication across operation classes**

`AddOperation` and `SubtractOperation` are structurally identical. Both contain:
- the same `isInteger(String)` private helper method (verbatim copy)
- the same validation-then-parse pattern (split → validate each term → compute)

Every future operation (`MultiplyOperation`, `DivideOperation`, `PowOperation`)
would copy this boilerplate again. This violates DRY and makes maintenance harder.

**Proposed solution**

Introduce an abstract base class `AbstractBinaryOperation` in the `operators`
package:

```
src/main/java/at/jku/se/calculator/operators/AbstractBinaryOperation.java
```

Responsibilities:
- Provides `protected boolean isInteger(String value)`
- Implements the template method pattern: `calculate(String txt)` in the base
  class handles splitting, length checking, validation, and error logging.
  Subclasses only implement `protected abstract int compute(int a, int b)`.

Result: `AddOperation` and `SubtractOperation` are reduced to a single method
each:

```java
// AddOperation
@Override
protected int compute(int a, int b) { return a + b; }

// SubtractOperation
@Override
protected int compute(int a, int b) { return a - b; }
```

**Files affected**
| File | Change |
|---|---|
| `operators/AbstractBinaryOperation.java` | Create (new) |
| `operators/AddOperation.java` | Extend base class, remove duplicated code |
| `operators/SubtractOperation.java` | Extend base class, remove duplicated code |
| `operators/TestAddOperation.java` | No changes required (behaviour unchanged) |
| `operators/TestSubtractOperation.java` | No changes required (behaviour unchanged) |

---

## R2 — Replace `OperationFactory` switch with a `Map`-based registry

**Problem: Switch statement that grows with every new operation**

`OperationFactory.getOperation()` uses a `switch` over `CalcAction`. Adding any
new operation requires editing the factory. This violates the Open/Closed
Principle and makes the factory a maintenance hotspot.

**Proposed solution**

Replace the `switch` with a `static` `EnumMap<CalcAction, ICalculationOperation>`
that is populated once at class-load time:

```java
private static final Map<CalcAction, ICalculationOperation> OPERATIONS =
    new EnumMap<>(CalcAction.class);

static {
    OPERATIONS.put(CalcAction.ADD,  new AddOperation());
    OPERATIONS.put(CalcAction.SUB,  new SubtractOperation());
}

public static ICalculationOperation getOperation(CalcAction action) {
    return OPERATIONS.getOrDefault(action, new NullOperation());
}
```

This also eliminates unreachable `default` branch and the now-redundant logger
in the factory (the `NullOperation` already logs a warning).

Note: operation instances are stateless, so sharing a single instance per
`CalcAction` is safe.

**Files affected**
| File | Change |
|---|---|
| `factory/OperationFactory.java` | Replace switch with EnumMap, remove logger |
| `factory/TestOperationFactory.java` | No changes required (behaviour unchanged) |

---

## R3 — Move `ICalculationOperation` out of the `factory` package

**Problem: Interface lives in the wrong package**

`ICalculationOperation` is declared in `at.jku.se.calculator.factory` but is
implemented exclusively by classes in `at.jku.se.calculator.operators`. This
forces every operator to import from the factory package, creating an odd
reverse dependency.

**Proposed solution**

Move the interface to the `operators` package:

```
src/main/java/at/jku/se/calculator/operators/ICalculationOperation.java
```

All operator classes already live there, so their imports become intra-package.
`OperationFactory` and `TestOperationFactory` will need one import update each.

**Files affected**
| File | Change |
|---|---|
| `factory/ICalculationOperation.java` | Move to `operators/` |
| `operators/AddOperation.java` | Update package reference in import |
| `operators/SubtractOperation.java` | Update package reference in import |
| `operators/NullOperation.java` | Update package reference in import |
| `factory/OperationFactory.java` | Update import |
| `factory/TestOperationFactory.java` | Update import |

> Dependency note: R2 should be completed before R3 to minimise the number of
> import edits needed.

---

## R4 — Fix stale Javadoc in `CalculatorController`

**Problem: Documentation refers to wrong UI framework**

The class-level Javadoc of `CalculatorController` contains:

```
* decoupled from the Swing UI.
```

The UI is JavaFX, not Swing.

**Proposed solution**

Change the comment to reference JavaFX.

**Files affected**
| File | Change |
|---|---|
| `CalculatorController.java` | Fix one word in Javadoc comment |

---

## Priority order

| # | Refactoring | Effort | Impact |
|---|---|---|---|
| R1 | Abstract base class | Medium | High — eliminates ongoing duplication |
| R2 | Map-based factory | Low | Medium — future-proofs the factory |
| R3 | Move interface to operators | Low | Low — cleaner package structure |
| R4 | Fix Javadoc | Trivial | Trivial |

R4 can be done at any time independently. R1–R3 are best done in order.
