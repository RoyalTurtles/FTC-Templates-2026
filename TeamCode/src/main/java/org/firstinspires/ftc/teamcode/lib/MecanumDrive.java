package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.robotcore.hardware.DcMotor;

// Field-centric mecanum kinematics wrapping the four drive motors.
public class MecanumDrive {

    private final DcMotor frontLeft;
    private final DcMotor frontRight;
    private final DcMotor backLeft;
    private final DcMotor backRight;

    public MecanumDrive(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
    }

    // strafe/forward/turn are joystick powers in [-1, 1]; headingDegrees rotates them field-centric.
    public void driveFieldCentric(double strafe, double forward, double turn, double headingDegrees) {
        double headingRadians = Math.toRadians(headingDegrees);
        double cos = Math.cos(-headingRadians);
        double sin = Math.sin(-headingRadians);
        double rotatedStrafe = strafe * cos - forward * sin;
        double rotatedForward = strafe * sin + forward * cos;

        double fl = rotatedForward + rotatedStrafe + turn;
        double fr = rotatedForward - rotatedStrafe - turn;
        double bl = rotatedForward - rotatedStrafe + turn;
        double br = rotatedForward + rotatedStrafe - turn;

        double max = Math.max(1.0, Math.max(Math.abs(fl), Math.max(Math.abs(fr), Math.max(Math.abs(bl), Math.abs(br)))));

        frontLeft.setPower(fl / max);
        frontRight.setPower(fr / max);
        backLeft.setPower(bl / max);
        backRight.setPower(br / max);
    }

    public void stop() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }
}
