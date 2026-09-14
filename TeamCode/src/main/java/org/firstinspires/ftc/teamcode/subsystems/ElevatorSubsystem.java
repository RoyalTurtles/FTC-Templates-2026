package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Configs.ElevatorConfig;
import org.firstinspires.ftc.teamcode.lib.Subsystem;

public class ElevatorSubsystem extends Subsystem {

    private final DcMotorEx elevLeft;
    private final DcMotorEx elevRight;
    private final ElapsedTime timer = new ElapsedTime();
    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    private int targetTicks;
    private double integralSum;
    private double lastError;

    public ElevatorSubsystem(HardwareMap hardwareMap) {
        elevLeft = hardwareMap.get(DcMotorEx.class, ElevatorConfig.LEFT_MOTOR_NAME);
        elevRight = hardwareMap.get(DcMotorEx.class, ElevatorConfig.RIGHT_MOTOR_NAME);
        elevLeft.setDirection(DcMotor.Direction.FORWARD);
        elevRight.setDirection(DcMotor.Direction.FORWARD);
        elevLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elevRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elevLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        elevRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        targetTicks = elevLeft.getCurrentPosition();
        timer.reset();
    }

    public void setTargetInches(double inches) {
        targetTicks = (int) Math.round(inches * ElevatorConfig.TICKS_PER_INCH);
    }

    public boolean atSetpoint() {
        return Math.abs(getTargetInches() - getCurrentInches()) < ElevatorConfig.POSITION_TOLERANCE_INCHES;
    }

    public double getCurrentInches() {
        return elevLeft.getCurrentPosition() / ElevatorConfig.TICKS_PER_INCH;
    }

    public double getTargetInches() {
        return targetTicks / ElevatorConfig.TICKS_PER_INCH;
    }

    public int getCurrentTicks() {
        return elevLeft.getCurrentPosition();
    }

    @Override
    public void periodic() {
        int current = elevLeft.getCurrentPosition();
        double dt = timer.seconds();
        timer.reset();
        if (dt <= 0) {
            dt = 1e-3;
        }

        double error = targetTicks - current;
        integralSum += error * dt;
        double derivative = (error - lastError) / dt;
        lastError = error;

        // kG is its own term so raising it alone (with kP/kI/kD zeroed) tunes the hold power.
        double gOutput = ElevatorConfig.kG;
        double pidOutput = ElevatorConfig.kP * error + ElevatorConfig.kI * integralSum + ElevatorConfig.kD * derivative;
        double totalOutput = clamp(pidOutput + gOutput, -ElevatorConfig.MAX_POWER, ElevatorConfig.MAX_POWER);
        elevLeft.setPower(totalOutput);
        elevRight.setPower(totalOutput);

        TelemetryPacket packet = new TelemetryPacket();
        packet.put("elevator/targetInches", getTargetInches());
        packet.put("elevator/currentInches", current / ElevatorConfig.TICKS_PER_INCH);
        packet.put("elevator/errorInches", error / ElevatorConfig.TICKS_PER_INCH);
        packet.put("elevator/currentTicks", getCurrentTicks());
        packet.put("elevator/pidOutput", pidOutput);
        packet.put("elevator/kGOutput", gOutput);
        packet.put("elevator/totalOutput", totalOutput);
        dashboard.sendTelemetryPacket(packet);
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
