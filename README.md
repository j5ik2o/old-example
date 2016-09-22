# Example for DDD/CQRS+ES

# Objective

This project's objective is to show Example DDD with CQRS+Event Sourcing.

# Using libraries

- akka-actor
- akka-stream
- akka-persistence
- akka-persistence-query
- akka-http

# Designs

## The main layers/components in the application

1. API Layer
    - `Main` object is what for bootstrap.
    - `TodoWriteService` trait is the controller component for write side.
    - `TodoReadService` trait is the controller component for read side.
    - There are models in the `json` package.
1. Domain Layer(for the Write Side)
    - `Todo` class is a state in the `TodoAggregate`.
    - `TodoAggregate` class is implemented by the Actor. It's DDD's Aggregate Root.
    - `TodoSupervisor` class is the supervisor for `TodoAggregate`.
    - `TodoShardFactory` class is the factory of `cluster-sharding`.
1. Database Access Layer(for the Read Side)
    - `TodoDao` is Data Access Object.
    - `TodoDas` is that wraps `TodoDao`.

# License

MIT License
Copyright (c) 2016 Junichi Kato
