package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Configs.ElevatorConfig;
import org.firstinspires.ftc.teamcode.Configs.PivotConfig;
import org.firstinspires.ftc.teamcode.commands.DriveFieldCentric;
import org.firstinspires.ftc.teamcode.commands.ElevatorToPosition;
import org.firstinspires.ftc.teamcode.commands.ResetImu;
import org.firstinspires.ftc.teamcode.commands.SetPivotPosition;
import org.firstinspires.ftc.teamcode.lib.CommandScheduler;
import org.firstinspires.ftc.teamcode.lib.GamepadButton;
import org.firstinspires.ftc.teamcode.subsystems.DrivetrainSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.PivotSubsystem;

// Robot entry point: wires subsystems and bindings once, then the scheduler drives every loop.
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp")
public class TeleOp extends LinearOpMode {

    @Override
    public void runOpMode() {
        CommandScheduler.getInstance().reset();

        ElevatorSubsystem elevator = new ElevatorSubsystem(hardwareMap);
        PivotSubsystem pivot = new PivotSubsystem(hardwareMap);
        DrivetrainSubsystem drivetrain = new DrivetrainSubsystem(hardwareMap);

        CommandScheduler.getInstance().setDefaultCommand(drivetrain,
                new DriveFieldCentric(drivetrain,
                        () -> -gamepad1.left_stick_y,
                        () -> gamepad1.left_stick_x,
                        () -> gamepad1.right_stick_x));

        new GamepadButton(gamepad1, GamepadButton.Button.BACK)
                .whenActive(new ResetImu(drivetrain));

        new GamepadButton(gamepad1, GamepadButton.Button.DPAD_DOWN)
                .whenActive(new ElevatorToPosition(elevator, () -> ElevatorConfig.BOTTOM_POSITION));
        new GamepadButton(gamepad1, GamepadButton.Button.DPAD_LEFT)
                .whenActive(new ElevatorToPosition(elevator, () -> ElevatorConfig.LOW_POSITION));
        new GamepadButton(gamepad1, GamepadButton.Button.DPAD_UP)
                .whenActive(new ElevatorToPosition(elevator, () -> ElevatorConfig.HIGH_POSITION));

        new GamepadButton(gamepad1, GamepadButton.Button.A)
                .whenActive(new SetPivotPosition(pivot, () -> PivotConfig.INTAKE_POSITION));
        new GamepadButton(gamepad1, GamepadButton.Button.B)
                .whenActive(new SetPivotPosition(pivot, () -> PivotConfig.SCORE_POSITION));
        new GamepadButton(gamepad1, GamepadButton.Button.Y)
                .whenActive(new SetPivotPosition(pivot, () -> PivotConfig.STOW_POSITION));

        waitForStart();

        while (opModeIsActive()) {
            CommandScheduler.getInstance().run();
        }
    }
}
