package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.Configs.ShooterConfig;
import org.firstinspires.ftc.teamcode.lib.Subsystem;

// Flywheel shooter using the SDK's built-in encoder velocity PIDF (no gravity term needed, unlike the elevator).
public class ShooterSubsystem extends Subsystem {

    private final DcMotorEx shooterMotor;
    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    private double targetVelocity;
    private boolean running;
    private double lastKP, lastKI, lastKD, lastKF;

    public ShooterSubsystem(HardwareMap hardwareMap) {
        shooterMotor = hardwareMap.get(DcMotorEx.class, ShooterConfig.MOTOR_NAME);
        shooterMotor.setDirection(DcMotor.Direction.FORWARD);
        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        pushPIDFIfChanged();
    }

    public void setTargetVelocity(double ticksPerSecond) {
        targetVelocity = ticksPerSecond;
        running = true;
    }

    public boolean atSetpoint() {
        return Math.abs(targetVelocity - shooterMotor.getVelocity()) < ShooterConfig.VELOCITY_TOLERANCE;
    }

    public double getVelocity() {
        return shooterMotor.getVelocity();
    }

    public void stop() {
        running = false;
        shooterMotor.setVelocity(0);
    }

    // Re-sends the PIDF coefficients only when a Dashboard edit actually changed one, to avoid spamming the hub.
    private void pushPIDFIfChanged() {
        if (ShooterConfig.kP == lastKP && ShooterConfig.kI == lastKI
                && ShooterConfig.kD == lastKD && ShooterConfig.kF == lastKF) {
            return;
        }

        shooterMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,
                new PIDFCoefficients(ShooterConfig.kP, ShooterConfig.kI, ShooterConfig.kD, ShooterConfig.kF));
        lastKP = ShooterConfig.kP;
        lastKI = ShooterConfig.kI;
        lastKD = ShooterConfig.kD;
        lastKF = ShooterConfig.kF;
    }

    @Override
    public void periodic() {
        pushPIDFIfChanged();
        shooterMotor.setVelocity(running ? targetVelocity : 0);

        TelemetryPacket packet = new TelemetryPacket();
        packet.put("shooter/target", targetVelocity);
        packet.put("shooter/current", shooterMotor.getVelocity());
        packet.put("shooter/error", targetVelocity - shooterMotor.getVelocity());
        dashboard.sendTelemetryPacket(packet);
    }
}
