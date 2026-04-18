# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
- Nothing

## [0.3.0] - 2026-04-18
### Added
- New directive: include
- New directive: includeonce
- New directive: includefrom
- Go-To file for incsrc & includefrom
- Support for multiline operators "," and "\"

### Changed
- Parser improvements for better handling of labels and registers
- Directives should now behave like opcodes and not show an error on an incomplete line

### Fixed
- incsrc directive now supports files without quotes if it is not a path or contains whitespaces

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

[Unreleased]: https://github.com/Mithnar/intellij-asar/compare/v0.3.0...HEAD
[0.3.0]: https://github.com/Mithnar/intellij-asar/compare/v0.2.0...v0.3.0
[0.2.0]: https://github.com/Mithnar/intellij-asar/compare/v0.1...v0.2.0
[0.1.0]: https://github.com/Mithnar/intellij-asar/releases/tag/v0.1
