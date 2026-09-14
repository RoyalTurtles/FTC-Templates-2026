package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.Configs.DrivetrainConfig;
import org.firstinspires.ftc.teamcode.lib.MecanumDrive;
import org.firstinspires.ftc.teamcode.lib.Subsystem;

// Mecanum drivetrain with field-centric control via the hub's built-in IMU.
public class DrivetrainSubsystem extends Subsystem {

    private final MecanumDrive mecanumDrive;
    private final IMU imu;

    public DrivetrainSubsystem(HardwareMap hardwareMap) {
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, DrivetrainConfig.FRONT_LEFT_NAME);
        DcMotor frontRight = hardwareMap.get(DcMotor.class, DrivetrainConfig.FRONT_RIGHT_NAME);
        DcMotor backLeft = hardwareMap.get(DcMotor.class, DrivetrainConfig.BACK_LEFT_NAME);
        DcMotor backRight = hardwareMap.get(DcMotor.class, DrivetrainConfig.BACK_RIGHT_NAME);

        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        mecanumDrive = new MecanumDrive(frontLeft, frontRight, backLeft, backRight);

        imu = hardwareMap.get(IMU.class, DrivetrainConfig.IMU_NAME);
        imu.initialize(
                new IMU.Parameters(
                        new RevHubOrientationOnRobot(
                                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                        )
                )
        );
    }

    // Drives the robot using field-centric control.
    public void driveFieldCentric(double forward, double strafe, double turn) {
        mecanumDrive.driveFieldCentric(
                strafe,
                forward,
                turn,
                imu.getRobotYawPitchRollAngles().getYaw()
        );
    }

    // Stops the drivetrain.
    public void stop() {
        mecanumDrive.stop();
    }

    // Resets the IMU's heading.
    public void resetImu() {
        imu.resetYaw();
    }
}
