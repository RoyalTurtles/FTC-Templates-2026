package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

// Single source of truth for match-day tuning: hardware names and all tunable constants.
@Config
public final class Configs {

    private Configs() {}

    @Config
    public static class ElevatorConfig {
        public static final String LEFT_MOTOR_NAME = "elevLeft";
        public static final String RIGHT_MOTOR_NAME = "elevRight";

        public static double kP = 0.01;
        public static double kI = 0.0;
        public static double kD = 0.0004;
        public static double kF = 0.0;
        public static double kG = 0.05;

        public static double BOTTOM_POSITION = 0.0;
        public static double LOW_POSITION = 9.0;
        public static double HIGH_POSITION = 27.0;

        public static double TICKS_PER_INCH = 87.5;
        public static double MAX_POWER = 1.0;
        public static double POSITION_TOLERANCE_INCHES = 0.25;
    }

    @Config
    public static class PivotConfig {
        public static final String SERVO_NAME = "pivotServo";

        public static double INTAKE_POSITION = 0.5;
        public static double STOW_POSITION = 0.15;
        public static double SCORE_POSITION = 0.85;

        public static double SOFT_LIMIT_MIN = 0.05;
        public static double SOFT_LIMIT_MAX = 0.95;

        public static double ZERO_OFFSET = 0.0;
    }

    @Config
    public static class ShooterConfig {
        public static final String MOTOR_NAME = "shooterMotor";

        public static double kP = 0.0004;
        public static double kI = 0.0;
        public static double kD = 0.0;
        public static double kF = 0.00021;

        public static double IDLE_VELOCITY = 0;
        public static double SHOOT_VELOCITY = 1650;
        public static double VELOCITY_TOLERANCE = 40;
    }

    public static class IntakeConfig {
        public static final String MOTOR_NAME = "intakeMotor";

        public static final double INTAKE_POWER = 0.5;
        public static final double INTAKE_REVERSE_POWER = -0.5;
    }

    public static class DrivetrainConfig {
        public static final String FRONT_LEFT_NAME = "frontLeft";
        public static final String FRONT_RIGHT_NAME = "frontRight";
        public static final String BACK_LEFT_NAME = "backLeft";
        public static final String BACK_RIGHT_NAME = "backRight";
        public static final String IMU_NAME = "imu";
    }
}
