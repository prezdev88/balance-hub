# Changelog

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

## [1.3.0](https://github.com/prezdev88/balance-hub/compare/v1.2.0...v1.3.0) (2026-03-01)


### Features

* add Dockerfile and Docker Compose for production setup; update application properties for environment configuration ([cb67218](https://github.com/prezdev88/balance-hub/commit/cb672188e82f82ab510565726ed2921c6391f539))
* add must_change_password functionality and related database migration ([675868a](https://github.com/prezdev88/balance-hub/commit/675868a06100ac30cb9b1a12856723291b826c20))
* add preview endpoint for salary snapshot with unpaid installments and free amount calculation ([c82f478](https://github.com/prezdev88/balance-hub/commit/c82f478979e6f9b6ab156e3b284d977eeafc354d))
* add start_prod.sh script for production environment setup ([a1b37da](https://github.com/prezdev88/balance-hub/commit/a1b37da66ec0cd038ff2f5abf688f1c563c759ab))
* Implement authentication and authorization features ([eecd17b](https://github.com/prezdev88/balance-hub/commit/eecd17b230d40ab7fa1b787c712c5e7ee57c468b))
* refactor monthly summary report endpoint to return structured response ([4351e5f](https://github.com/prezdev88/balance-hub/commit/4351e5f372444ba6da39e6a68b162a49589eab6d))
* update debt settlement status calculation in GetDebtDetailUseCase ([4299728](https://github.com/prezdev88/balance-hub/commit/429972874f0bdb142346a88cac703c3ca116bd57))


### Bug Fixes

* update base image in Dockerfile for build stage ([bceb8a7](https://github.com/prezdev88/balance-hub/commit/bceb8a7cc0b813d4fffeec0b62472ef10cea89f9))

## [1.2.0](https://github.com/prezdev88/balance-hub/compare/v1.1.0...v1.2.0) (2026-02-27)


### Features

* add backup and restore scripts for database management ([e5c24ba](https://github.com/prezdev88/balance-hub/commit/e5c24ba40ec11c5ade4c492dd7210efaa74b170a))
* add controllers and configuration for all uses cases ([821c405](https://github.com/prezdev88/balance-hub/commit/821c4056d55e7ea3adc77a329a4aa31d420016f9))
* Add controllers for reports, salary snapshots, and savings goals ([97a9f20](https://github.com/prezdev88/balance-hub/commit/97a9f2046752215501bc61ad3e9b4b7cbf106708))
* add flyway to project ([c43f185](https://github.com/prezdev88/balance-hub/commit/c43f1853e3fdcbe660158bfef8e19f7147568cf6))
* add improvements documentation with feature requests and questions ([93226a7](https://github.com/prezdev88/balance-hub/commit/93226a739d02c9fa06cb333a7acdb240c594920a))
* add OpenAPI specification for Balance Hub API ([a0e9f5f](https://github.com/prezdev88/balance-hub/commit/a0e9f5fb68e4b89402311ec82011b6bd4f9616ca))
* add springdoc-openapi dependency and configure application properties for OpenAPI UI ([a21029e](https://github.com/prezdev88/balance-hub/commit/a21029e46837f2ee782954107338d128a4a90627))
* enhance use cases with transaction management and update controller responses ([eebf169](https://github.com/prezdev88/balance-hub/commit/eebf169a8b67f4518a88197dc0637df61b09bc81))
* implement delete functionality for fixed expenses and enhance debt tracking ([c6231f8](https://github.com/prezdev88/balance-hub/commit/c6231f8a5e3393897f4a823ccd406dcaaeb7b2ce))
* implement input ports for use cases and update controllers to use them ([7dd89e9](https://github.com/prezdev88/balance-hub/commit/7dd89e903f2484d408e7f7723216394a52ca7d6a))
* remove deprecated OpenAPI YAML file and standardize documentation source ([c825133](https://github.com/prezdev88/balance-hub/commit/c825133a44fefc0109d00694d61ca5d6e215bcfb))
* remove unused import statements from RecurringExpenseController and SalaryController ([dfe8dc5](https://github.com/prezdev88/balance-hub/commit/dfe8dc56ea1d7bb0c3fb1b97516a8c9d913ef58b))

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
