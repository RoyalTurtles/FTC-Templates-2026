package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Configs.ElevatorConfig;
import org.firstinspires.ftc.teamcode.commands.ElevatorToPosition;
import org.firstinspires.ftc.teamcode.lib.CommandScheduler;
import org.firstinspires.ftc.teamcode.lib.GamepadButton;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;

// Match-day tuning harness: jump the elevator between setpoints while editing ElevatorConfig live
// from the FTC Dashboard; target vs current is streamed to the Dashboard telemetry graph.
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Elevator Tuning", group = "tuning")
public class ElevatorTuning extends LinearOpMode {

    @Override
    public void runOpMode() {
        CommandScheduler.getInstance().reset();

        ElevatorSubsystem elevator = new ElevatorSubsystem(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        new GamepadButton(gamepad1, GamepadButton.Button.DPAD_DOWN)
                .whenActive(new ElevatorToPosition(elevator, () -> ElevatorConfig.BOTTOM_POSITION));
        new GamepadButton(gamepad1, GamepadButton.Button.DPAD_LEFT)
                .whenActive(new ElevatorToPosition(elevator, () -> ElevatorConfig.LOW_POSITION));
        new GamepadButton(gamepad1, GamepadButton.Button.DPAD_UP)
                .whenActive(new ElevatorToPosition(elevator, () -> ElevatorConfig.HIGH_POSITION));

        waitForStart();

        while (opModeIsActive()) {
            CommandScheduler.getInstance().run();
            telemetry.addData("target (in)", elevator.getTargetInches());
            telemetry.addData("current (in)", elevator.getCurrentInches());
            telemetry.update();
        }
    }
}
