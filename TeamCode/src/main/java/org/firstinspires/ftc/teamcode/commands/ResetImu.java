package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.lib.Command;
import org.firstinspires.ftc.teamcode.subsystems.DrivetrainSubsystem;

// Resets the drivetrain's IMU heading and finishes immediately.
public class ResetImu extends Command {

    private final DrivetrainSubsystem drivetrain;

    public ResetImu(DrivetrainSubsystem drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.resetImu();
    }
}
