# TeamCode

A command-based FTC template, structured like an FRC project. Own mini scheduler in `lib/`
(no FTCLib/SolversLib) — `Subsystem`, `Command`, `CommandScheduler`, `Trigger`/`GamepadButton`.

## Structure

```
Configs.java     hardware names + everything tunable, grouped by subsystem, @Config for Dashboard
lib/             the scheduler framework itself — don't touch unless you're changing how commands run
subsystems/      one class per physical mechanism, owns the hardware
commands/        one class per action, calls subsystem methods
opmode/          TeleOp / Autonomous / tuning OpModes — wiring only
```

## Subsystem (`subsystems/`)

Owns exactly one mechanism's hardware objects (motors, servos, sensors) and nothing else.
Extends `Subsystem`, registers itself automatically. Public methods are small and direct
(`setTargetInches`, `intakeOn`, `setPosition`) — never a gamepad, a command, or another subsystem.
Any closed-loop control (PID, feedforward) lives in `periodic()`, reading gains from `Configs`
every loop so Dashboard edits apply live. Examples: `ElevatorSubsystem`, `PivotSubsystem`,
`ShooterSubsystem`, `IntakeSubsystem`, `DrivetrainSubsystem`.

## Command (`commands/`)

One action, one file. Extends `Command`, declares the subsystem(s) it needs with
`addRequirements(...)` so the scheduler auto-cancels conflicting commands. Implements whichever
of `initialize()/execute()/isFinished()/end()` it needs — most commands here only need
`initialize()` and rely on the default `isFinished() = true` (fire-and-forget) or a subsystem's
own `atSetpoint()`. Examples: `ElevatorToPosition`, `SetPivotPosition`.

## Configs.java

Every number or name a student would touch on match day, and nothing else. Grouped into a
nested `*Config` class per subsystem:
- hardware map device names (`public static final String`, never change at runtime)
- PID/PIDF gains (`kP`/`kI`/`kD`/`kF`/`kG`)
- setpoints and presets (positions in inches, servo positions, velocities)
- physical/safety constants (`TICKS_PER_INCH`, `MAX_POWER`, tolerances, soft limits)

Gains and setpoints stay non-`final` on purpose — `@Config` needs a mutable field to push
Dashboard edits at runtime. Only the device-name strings are `final`.

## OpMode (`opmode/`)

Construct subsystems, bind gamepad buttons to commands via `GamepadButton`/`Trigger`, set
default commands, then loop `CommandScheduler.getInstance().run()`. No control logic, no math —
if you're computing something here, it belongs in a subsystem or command instead.

## What needs tuning before this runs on a real robot

- **Names** — every `*_NAME` / `*_MOTOR_NAME` / `SERVO_NAME` in `Configs.java` must match the
  hardware config saved on the Driver Station exactly.
- **Directions** — each subsystem defaults every motor/servo to `FORWARD`; flip to `REVERSE`
  where the physical mounting needs it (e.g. a mirrored second elevator motor).
- **PID gains** — `kP`/`kI`/`kD` per subsystem. Tune with everything else zeroed first.
- **Feedforward** — `ElevatorConfig.kG`: zero `kP`/`kI`/`kD`, raise `kG` alone until the elevator
  just barely holds. `ShooterConfig.kF`: velocity feedforward for the flywheel.
- **Distances/positions** — `ElevatorConfig.TICKS_PER_INCH` (measure it), `STOW`/`LOW`/`HIGH_POSITION`
  (inches), `PivotConfig` preset servo positions and soft limits, `ShooterConfig` velocities.
- **Tolerances** — `POSITION_TOLERANCE_INCHES`, `VELOCITY_TOLERANCE` — how close is "close enough"
  for a command to finish.
- **The two blanks in `PivotSubsystem`** — zero offset and soft limit are intentionally unimplemented;
  fill them in before relying on the pivot.

Do all of the above live from `opmode/ElevatorTuning.java` and FTC Dashboard where possible —
no redeploy needed to see the effect of a gain change.
