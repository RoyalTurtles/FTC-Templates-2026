# FTC-Templates-2026

Command-based FTC template (FRC-style) to help students transition. Hand-rolled scheduler in
`lib/` no FTCLib/SolversLib.

## Structure

```
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/
  Configs.java     all hardware names + tunable constants, grouped by subsystem, @Config for Dashboard
  lib/             scheduler framework (Subsystem, Command, CommandScheduler, Trigger, GamepadButton)
  subsystems/      one class per mechanism, owns the hardware
  commands/        one class per action, calls subsystem methods
  opmode/          TeleOp / Autonomous / tuning OpModes — wiring only
```

## Conventions

- Standardized FTC hardware mapping: every device name lives in `Configs.java` as a
  `public static final String`, inside a nested `*Config` class per subsystem — never hardcoded
  in a subsystem file.
- TeleOp routines stay thin: subsystems + `GamepadButton` bindings + `CommandScheduler.run()`
  in the loop, under ~10 lines of actual logic. No control math in `opmode/`.
- PID/PIDF gains and feedforward terms are self-rolled in each subsystem's `periodic()`, reading
  live from `Configs` (not the SDK's built-in hub PIDF) — see `ElevatorSubsystem` (position + kG)
  and `ShooterSubsystem` (velocity, shared gains across two motors) as the reference pattern.
- Minimal comments: 1-2 lines max per class/method, only where the why isn't obvious.
- Naming: motors/servos are `camelCase` and describe the mechanism + side
  (`elevLeft`/`elevRight`, `pivotServo`, `rightShooter`/`leftShooter`, `intakeMotor`). File name
  must match its public class name exactly. Subsystem classes end in `Subsystem`, never `Sys`.
- API reference: https://javadoc.io/doc/org.firstinspires.ftc/RobotCore/12.0.0/index.html — when
  looking up any FTC API or rules online, append "2026" / "stable 2026 season" to the query.

## Workflow for code changes

1. **Plan** — read the relevant subsystem/command files first. For a high-risk change (rewriting
   a control loop, renaming public APIs, touching `lib/`), state the specific lines you intend to
   change and why, and wait for confirmation before editing.
2. **Execute** — make the localized edit, following the conventions above.
3. **Verify** — run `./gradlew :TeamCode:compileDebugJavaWithJavac`. Do not report a task done on
   a failing build; fix and recompile first.
4. **Report** — short summary of what changed, confirming the build compiled.
