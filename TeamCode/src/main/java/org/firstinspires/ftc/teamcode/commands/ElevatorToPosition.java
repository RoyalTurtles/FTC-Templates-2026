package org.firstinspires.ftc.teamcode.commands;

import java.util.function.DoubleSupplier;

import org.firstinspires.ftc.teamcode.lib.Command;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;

// Drives the elevator to a target (inches) and finishes once it's within tolerance.
public class ElevatorToPosition extends Command {

    private final ElevatorSubsystem elevator;
    private final DoubleSupplier targetInches;

    public ElevatorToPosition(ElevatorSubsystem elevator, double targetInches) {
        this(elevator, () -> targetInches);
    }

    // Reads the target from Configs at press-time, so it reflects live Dashboard edits.
    public ElevatorToPosition(ElevatorSubsystem elevator, DoubleSupplier targetInches) {
        this.elevator = elevator;
        this.targetInches = targetInches;
        addRequirements(elevator);
    }

    @Override
    public void initialize() {
        elevator.setTargetInches(targetInches.getAsDouble());
    }

    @Override
    public boolean isFinished() {
        return elevator.atSetpoint();
    }
}
