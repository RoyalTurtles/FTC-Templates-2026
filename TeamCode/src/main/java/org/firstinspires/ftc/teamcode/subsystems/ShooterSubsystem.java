package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Configs.ShooterConfig;
import org.firstinspires.ftc.teamcode.lib.Subsystem;

// Flywheel shooter with a self-rolled velocity PIDF loop (the hub's built-in velocity PIDF is not
// used here), plus the intake and indexer that feed it. Both flywheel motors share one set of gains
// but run independent PID state since their encoders can drift apart.
public class ShooterSubsystem extends Subsystem {

    private final DcMotorEx rightShooter;
    private final DcMotorEx leftShooter;
    private final DcMotor intake;
    private final DcMotor index;
    private final ElapsedTime timer = new ElapsedTime();
    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    private double targetVelocity;
    private boolean running;
    private double rightIntegral, rightLastError;
    private double leftIntegral, leftLastError;

    public ShooterSubsystem(HardwareMap hardwareMap) {
        rightShooter = hardwareMap.get(DcMotorEx.class, ShooterConfig.RIGHT_MOTOR_NAME);
        leftShooter = hardwareMap.get(DcMotorEx.class, ShooterConfig.LEFT_MOTOR_NAME);
        intake = hardwareMap.get(DcMotor.class, ShooterConfig.INTAKE_MOTOR_NAME);
        index = hardwareMap.get(DcMotor.class, ShooterConfig.INDEX_MOTOR_NAME);

        rightShooter.setDirection(DcMotor.Direction.FORWARD);
        leftShooter.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotor.Direction.FORWARD);
        index.setDirection(DcMotor.Direction.FORWARD);

        rightShooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftShooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        timer.reset();
    }

    public void setTargetVelocity(double ticksPerSecond) {
        targetVelocity = ticksPerSecond;
        running = true;
    }

    public void stopShooter() {
        running = false;
        rightShooter.setPower(0);
        leftShooter.setPower(0);
    }

    public boolean atSetpoint() {
        return Math.abs(targetVelocity - rightShooter.getVelocity()) < ShooterConfig.VELOCITY_TOLERANCE
                && Math.abs(targetVelocity - leftShooter.getVelocity()) < ShooterConfig.VELOCITY_TOLERANCE;
    }

    public double getTargetVelocity() {
        return targetVelocity;
    }

    public double getRightVelocity() {
        return rightShooter.getVelocity();
    }

    public double getLeftVelocity() {
        return leftShooter.getVelocity();
    }

    public void intakeOn() {
        intake.setPower(ShooterConfig.INTAKE_POWER);
    }

    public void intakeReverse() {
        intake.setPower(ShooterConfig.INTAKE_REVERSE_POWER);
    }

    public void intakeStop() {
        intake.setPower(0);
    }

    public void indexOn() {
        index.setPower(ShooterConfig.INDEX_POWER);
    }

    public void indexReverse() {
        index.setPower(ShooterConfig.INDEX_REVERSE_POWER);
    }

    public void indexStop() {
        index.setPower(0);
    }

    @Override
    public void periodic() {
        double dt = timer.seconds();
        timer.reset();
        if (dt <= 0) {
            dt = 1e-3;
        }

        double rightOutput = 0;
        double leftOutput = 0;

        if (running) {
            rightOutput = computePid(targetVelocity - rightShooter.getVelocity(), dt, true);
            leftOutput = computePid(targetVelocity - leftShooter.getVelocity(), dt, false);
        } else {
            rightIntegral = 0;
            rightLastError = 0;
            leftIntegral = 0;
            leftLastError = 0;
        }

        rightShooter.setPower(clamp(rightOutput, -1.0, 1.0));
        leftShooter.setPower(clamp(leftOutput, -1.0, 1.0));

        TelemetryPacket packet = new TelemetryPacket();
        packet.put("shooter/target", targetVelocity);
        packet.put("shooter/rightCurrent", rightShooter.getVelocity());
        packet.put("shooter/leftCurrent", leftShooter.getVelocity());
        packet.put("shooter/rightError", targetVelocity - rightShooter.getVelocity());
        packet.put("shooter/leftError", targetVelocity - leftShooter.getVelocity());
        packet.put("shooter/rightOutput", rightOutput);
        packet.put("shooter/leftOutput", leftOutput);
        dashboard.sendTelemetryPacket(packet);
    }

    // Shared PIDF math for one motor; right/left keep separate integral and derivative state.
    private double computePid(double error, double dt, boolean isRight) {
        double integral = isRight ? rightIntegral + error * dt : leftIntegral + error * dt;
        double lastError = isRight ? rightLastError : leftLastError;
        double derivative = (error - lastError) / dt;

        if (isRight) {
            rightIntegral = integral;
            rightLastError = error;
        } else {
            leftIntegral = integral;
            leftLastError = error;
        }

        return ShooterConfig.kP * error + ShooterConfig.kI * integral + ShooterConfig.kD * derivative
                + ShooterConfig.kF * targetVelocity;
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
