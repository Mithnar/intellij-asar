# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
- Nothing

## [0.2.0] - 2026-03-26
### Added
- New directive: asar
- New directive: namespace
- New directive: freecode
- New directive: freespacebyte

### Changed
- Improved Parsing on freespace, freedata, autoclean and prot directives
- Split up the namespace directive into configuration and definition variants

### Fixed
- Fixed shadowing of constant and label references in expressions

## [0.1.0] - 2026-03-24
### Added
- Syntax Checking
- Syntax Highlighting
- Labels: Jump to named Label
- Labels: Show-Usage of named Label

[Unreleased]: https://github.com/Mithnar/intellij-asar/compare/0.2.0...HEAD
[0.2.0]: https://github.com/Mithnar/intellij-asar/compare/0.1...0.2
[0.1.0]: https://github.com/Mithnar/intellij-asar/releases/tag/0.1-RC1
