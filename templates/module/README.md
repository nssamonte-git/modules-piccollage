# core-NAME

One short paragraph: what the module does and the problem it solves in an app. Say whether it is an engine (no Compose) or a Compose UI module.

## When to use it

- The situations it is for.
- When to use a different module instead (for example the `-compose` module for ready-made UI).

## Add it

```kotlin
implementation("noel.libs:core-NAME:<version>")
```

What it brings in transitively (its `api` dependencies), and which modules it is usually paired with.

## Quick start

The smallest working example, using only real public API from this module:

```kotlin
// create, call, observe the result
```

## Main types

| Type | What it does |
|---|---|
| `TypeName` | One line. |

## What your app provides

What the host app must supply or own: interfaces to implement, threading (which calls block), lifecycle and cleanup, files, FileProvider, permissions, credentials.

## Limits and behaviour

Defaults, bounds, failure results, and anything the module deliberately does not do.

## Tests

What the tests cover and how to run them:

```bash
./gradlew :core-NAME:testDebugUnitTest
```

## See also

Related modules, the relevant guide section, and where the sample app uses this module.
