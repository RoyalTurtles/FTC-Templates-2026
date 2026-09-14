// Servo docs: https://ftc-docs.firstinspires.org/en/latest/programming_resources/shared_resources/servo_programming/servo-programming.html
// FTC Dashboard tuning: run an OpMode and edit Configs.PivotConfig live from the dashboard web UI.
// Fill in the two blanks below before relying on this subsystem.
package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Configs.PivotConfig;
import org.firstinspires.ftc.teamcode.lib.Subsystem;

// goBILDA pivot servo on the elevator car.
public class PivotSubsystem extends Subsystem {

    private final Servo pivotServo;
    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    private double commandedPosition;
    private double clampedPosition;

    public PivotSubsystem(HardwareMap hardwareMap) {
        pivotServo = hardwareMap.get(Servo.class, PivotConfig.SERVO_NAME);
        pivotServo.setDirection(Servo.Direction.FORWARD);
        // implement your set zero here
        stow();
    }

    // Commands a raw [0, 1] position; routes through the soft limit before it reaches the servo.
    public void setPosition(double position) {
        commandedPosition = position;
        clampedPosition = position;
        // implement your soft limit here
        pivotServo.setPosition(clampedPosition);
    }

    public void intake() {
        setPosition(PivotConfig.INTAKE_POSITION);
    }

    public void score() {
        setPosition(PivotConfig.SCORE_POSITION);
    }

    public void stow() {
        setPosition(PivotConfig.STOW_POSITION);
    }

    public double getCommandedPosition() {
        return commandedPosition;
    }

    @Override
    public void periodic() {
        TelemetryPacket packet = new TelemetryPacket();
        packet.put("pivot/commanded", commandedPosition);
        packet.put("pivot/clamped", clampedPosition);
        dashboard.sendTelemetryPacket(packet);
    }
}
