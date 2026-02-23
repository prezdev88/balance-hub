# Changelog

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

## 1.1.0 (2026-02-23)


### Features

* add create debt use case (and installments) ([5e415b7](https://github.com/prezdev88/balance-hub/commit/5e415b744408ff97ddacd5a476cfbfcbc864ce4d))
* add CreateDebtorUseCase and corresponding tests; update use cases documentation ([d674a6d](https://github.com/prezdev88/balance-hub/commit/d674a6dccd66c2a8b1fc1da8d9a0f142f13bb146))
* add devcontainer to project ([4b48b56](https://github.com/prezdev88/balance-hub/commit/4b48b56c4e754c2dbc7d67ac451d99f0b5650632))
* add get debts in use case file ([8e92280](https://github.com/prezdev88/balance-hub/commit/8e922806d1189fb931a3b7ddc5dd63857612351e))
* add Maven Surefire and JaCoCo plugins for unit testing and coverage reporting ([b690c60](https://github.com/prezdev88/balance-hub/commit/b690c607505bc3b60042f3fb3ed441ddc22d79ea))
* add notes on Debt and Installment creation in use cases documentation ([df22f84](https://github.com/prezdev88/balance-hub/commit/df22f8446b74da73cb1f0f470237bc970864aa10))
* add openssh-client to dev container ([f3ae9d7](https://github.com/prezdev88/balance-hub/commit/f3ae9d7c820d03e3bbe23b82b70eb6470ac1387e))
* add pay installment use case ([a70cdb0](https://github.com/prezdev88/balance-hub/commit/a70cdb016b5f423f772af99a4e7130b1d6afe201))
* add PostgreSQL schema and queries for debt management ([402115d](https://github.com/prezdev88/balance-hub/commit/402115daa181b8e192398c6b5d1284da49dc63d1))
* add standard version to project ([5d33f38](https://github.com/prezdev88/balance-hub/commit/5d33f3864f0e033dc1bf9fdfe32081a9ccde44e5))
* add use case for obtaining total expenses by type (fixed/optional) ([26b9c10](https://github.com/prezdev88/balance-hub/commit/26b9c1023d238fe05053659efb4ed4654ad72cec))
* first commit ([e715a00](https://github.com/prezdev88/balance-hub/commit/e715a0034ea06d684b4a2209b4de61d6dc187992))
* implement Debt and Installment classes with validation and exception handling ([322a33e](https://github.com/prezdev88/balance-hub/commit/322a33e6e51906b1662c48d0100e51b705d0be2e))
* implement FixedExpense domain model and related use cases ([421a760](https://github.com/prezdev88/balance-hub/commit/421a7601ab943f6e8f041ba367e9e2a2001d078a))
* implement GetDebts use case with debtor validation and date range filtering ([61226e3](https://github.com/prezdev88/balance-hub/commit/61226e3a80ae4d88f5cade9e29409b941ad3b108))
* implement GetRecurringExpenseTotal use case and related classes; add totalByType method to repository ([994ac34](https://github.com/prezdev88/balance-hub/commit/994ac345bebb68a45f7540fd3d04c6b89b3bafae))
* implement JPA adapters and entities for debt management ([1e738e0](https://github.com/prezdev88/balance-hub/commit/1e738e06c0ad185924e85776623cfcfa854396b5))
* implement ListDebtors use case and related classes ([3ccf732](https://github.com/prezdev88/balance-hub/commit/3ccf732649121076484eb9784321a7342c3170f7))
* implement listing use case for recurring expenses with command and result models ([5bec42c](https://github.com/prezdev88/balance-hub/commit/5bec42c9d87ccf0f705edc21145528f26e7fd39d))
* implement recurring expense management use cases including creation and update ([b7208ca](https://github.com/prezdev88/balance-hub/commit/b7208ca521941783998fada1bd494668cbc3d014))
* implement salary management use cases including creation and validation ([dd0ceef](https://github.com/prezdev88/balance-hub/commit/dd0ceef90253dbb583c3ce1e07b269ae76797dfc))
* implement UpdateFixedExpense use case and related command/response models ([6fd58f4](https://github.com/prezdev88/balance-hub/commit/6fd58f41ff12cf950399cc1eaa60dd0ac22f65f7))
* implement UpdateFixedExpense use case with exception handling and result model ([f971822](https://github.com/prezdev88/balance-hub/commit/f971822cfc196edb81ef1d49106084e20d263e17))
* remove Dockerfile and update application properties for PostgreSQL integration ([a131bc6](https://github.com/prezdev88/balance-hub/commit/a131bc6ff11b3a4609b04e177a58f310d091b693))
* update Java version to 25 in pom.xml ([211c625](https://github.com/prezdev88/balance-hub/commit/211c625a4ad733b4596531876687678fb2a49a8c))


### Bug Fixes

* change id field to final in DebtorId class ([5bbfa83](https://github.com/prezdev88/balance-hub/commit/5bbfa839f233bbd94f487b8bd7fb48a063d80bd8))
* update sonar coverage exclusions for BalanceHubApplication class ([ea2c6a4](https://github.com/prezdev88/balance-hub/commit/ea2c6a4b4eb1afad0a4b2015bb68e3d2786e80fe))


### Refactor

* replace ListDebtorsResult class with record for simplicity ([768cdfc](https://github.com/prezdev88/balance-hub/commit/768cdfccecd4cab5c7c64ef4bd77cc5840ad3b4e))


### Tests

* add unit tests for debt, debtor, installment, and recurring expense domains ([7429280](https://github.com/prezdev88/balance-hub/commit/742928099c23e6ed6b9d909e2cf7a7835cd8ee07))
* add unit tests for debtor, recurring expense, and salary use cases; include validation checks ([47a681f](https://github.com/prezdev88/balance-hub/commit/47a681fc12da9731af6ab0369ed0fa3de6660130))
* refactor salary and debtor tests to use variables for repeated values ([c5bef72](https://github.com/prezdev88/balance-hub/commit/c5bef72d841d6eb792ae0b160819b2e6b16a88ba))
* remove unused BalanceHubApplicationTest class ([1829fb6](https://github.com/prezdev88/balance-hub/commit/1829fb62f6f394789a615fc86923039158152711))
* remove unused BalanceHubApplicationTests class ([78b42e2](https://github.com/prezdev88/balance-hub/commit/78b42e221a196f55267e5f972d63a9623d89b45f))
