package org.firstinspires.ftc.teamcode.commands;

import java.util.function.DoubleSupplier;

import org.firstinspires.ftc.teamcode.lib.Command;
import org.firstinspires.ftc.teamcode.subsystems.DrivetrainSubsystem;

// Default command: drives the robot field-centric from joystick input every loop.
public class DriveFieldCentric extends Command {

    private final DrivetrainSubsystem drivetrain;
    private final DoubleSupplier forward;
    private final DoubleSupplier strafe;
    private final DoubleSupplier turn;

    public DriveFieldCentric(DrivetrainSubsystem drivetrain, DoubleSupplier forward, DoubleSupplier strafe, DoubleSupplier turn) {
        this.drivetrain = drivetrain;
        this.forward = forward;
        this.strafe = strafe;
        this.turn = turn;
        addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        drivetrain.driveFieldCentric(forward.getAsDouble(), strafe.getAsDouble(), turn.getAsDouble());
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
