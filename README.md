# Asar ASM Plugin for IntelliJ IDEA

n IntelliJ IDEA plugin for [Asar](https://github.com/RPGHacker/asar) the assembler used in Super Mario World hacking.

This plugin is WIP, highlighting and syntax checking should work for common asar, but some asar features might not work (yet).

## Installation
1. You can Download [IntelliJ Community Edition](https://www.jetbrains.com/idea/) for free
2. Download the latest plugin `.zip` from [Releases](https://github.com/Mithnar/intellij-asar/releases)
3. Open IntelliJ IDEA (only tested with IntelliJ IDEA, but should work with others)
4. Go to **Settings -> Plugins -> Cogwheel (next to marketplace and installed)→ Install Plugin from Disk...**
5. Select the downloaded `.zip` file
6. Restart the IDE

## Getting Started
1. Open a Project/File with ASAR asm (Or create it from scratch!)
2. .asm files should now get syntax highlighting and syntax checking
3. The plugin can conflict with other plugins dealing with .asm files, please check that this is the only plugin for .asm files active

## Features
1. Syntax Highlighting
2. Syntax Checking
3. Go-To-Definition (wip)
4. Find Usages (very wip)
5. And probably more in the future.

## Release History
For the detailed release history, head over to the [Changelog](https://github.com/Mithnar/intellij-asar/blob/main/CHANGELOG.md)


## Recent Releases:
### [0.5.0] - 2026-05-28
#### Added
- New directive: `struct`
- New directive: `pushns`
- New directive: `pullns`
- New operators: `+=`, `:=`, `?=`
- Global labels
- Namespaced labels

#### Changed
- Parser improvements for the handling of labels with namespaces

#### Fixed
- Fixed an issue with unquoted file paths

### [0.4.0] - 2026-04-20
#### Added
- New directive: `base`
- New directive: `bank`
- New directive: `dpbase`
- New directive: `optimize {dp/address}`
- New directive: `pushbase`
- New directive: `pullbase`
- New directive: `fullsa1rom`
- New directive: `sfxrom`

#### Changed
- Improved lexing: Split `NUMBER_TOKEN` into `NUMBER_HEX_TOKEN`, `NUMBER_DEC_TOKEN` and `NUMBER_BIN_TOKEN`
- Create Deprecation & Quickfix for `warnpc`

#### Fixed
- `skip` directive now supports align and offset keywords
- `sa1rom` directive now supports bank parameters

## Meta
Distributed under the GPL-3.0 license. See [LICENSE](https://github.com/Mithnar/intellij-asar/blob/main/LICENSE) for more information.

## Feedback & Issues

Found a bug or missing syntax? [Open an issue](https://github.com/Mithnar/intellij-asar/issues) with:
- The code snippet that breaks (Red line for valid asm? Wrong highlighting color? Accepts invalid asm?)
- Contributions are welcome

## Contributing

1. Fork it (<https://github.com/Mithnar/intellij-asar/fork>)
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Commit your changes (`git commit -am 'Add some fooBar'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request

[0.5.0]: https://github.com/Mithnar/intellij-asar/compare/v0.4.0...v0.5.0
[0.4.0]: https://github.com/Mithnar/intellij-asar/compare/v0.3.0...v0.4.0
