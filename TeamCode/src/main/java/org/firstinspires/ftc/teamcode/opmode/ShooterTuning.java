package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Configs.ShooterConfig;
import org.firstinspires.ftc.teamcode.commands.RunIndex;
import org.firstinspires.ftc.teamcode.commands.RunIntake;
import org.firstinspires.ftc.teamcode.commands.SetShooterVelocity;
import org.firstinspires.ftc.teamcode.commands.StopShooter;
import org.firstinspires.ftc.teamcode.lib.CommandScheduler;
import org.firstinspires.ftc.teamcode.lib.GamepadButton;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

// Match-day tuning harness: A spins the flywheel up, B stops it, bumpers run the intake, dpad
// up/down runs the indexer. Edit ShooterConfig.kP/kI/kD/kF live from FTC Dashboard while watching
// the target-vs-current velocity graph there.
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Shooter Tuning", group = "tuning")
public class ShooterTuning extends LinearOpMode {

    @Override
    public void runOpMode() {
        CommandScheduler.getInstance().reset();

        ShooterSubsystem shooter = new ShooterSubsystem(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        new GamepadButton(gamepad1, GamepadButton.Button.A)
                .whenActive(new SetShooterVelocity(shooter, () -> ShooterConfig.SHOOT_VELOCITY));
        new GamepadButton(gamepad1, GamepadButton.Button.B)
                .whenActive(new StopShooter(shooter));

        RunIntake intakeForward = new RunIntake(shooter, false);
        RunIntake intakeReverse = new RunIntake(shooter, true);
        new GamepadButton(gamepad1, GamepadButton.Button.RIGHT_BUMPER)
                .whenActive(intakeForward)
                .whenInactive(intakeForward);
        new GamepadButton(gamepad1, GamepadButton.Button.LEFT_BUMPER)
                .whenActive(intakeReverse)
                .whenInactive(intakeReverse);

        RunIndex indexForward = new RunIndex(shooter, false);
        RunIndex indexReverse = new RunIndex(shooter, true);
        new GamepadButton(gamepad1, GamepadButton.Button.DPAD_UP)
                .whenActive(indexForward)
                .whenInactive(indexForward);
        new GamepadButton(gamepad1, GamepadButton.Button.DPAD_DOWN)
                .whenActive(indexReverse)
                .whenInactive(indexReverse);

        waitForStart();

        while (opModeIsActive()) {
            CommandScheduler.getInstance().run();
            telemetry.addData("target (ticks/s)", shooter.getTargetVelocity());
            telemetry.addData("right current (ticks/s)", shooter.getRightVelocity());
            telemetry.addData("left current (ticks/s)", shooter.getLeftVelocity());
            telemetry.update();
        }
    }
}
