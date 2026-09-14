package org.firstinspires.ftc.teamcode.commands;

import java.util.function.DoubleSupplier;

import org.firstinspires.ftc.teamcode.lib.Command;
import org.firstinspires.ftc.teamcode.subsystems.PivotSubsystem;

// Commands the pivot to a position and finishes immediately (no position feedback to wait on).
public class SetPivotPosition extends Command {

    private final PivotSubsystem pivot;
    private final DoubleSupplier position;

    public SetPivotPosition(PivotSubsystem pivot, double position) {
        this(pivot, () -> position);
    }

    // Reads the position from Configs at press-time, so it reflects live Dashboard edits.
    public SetPivotPosition(PivotSubsystem pivot, DoubleSupplier position) {
        this.pivot = pivot;
        this.position = position;
        addRequirements(pivot);
    }

    @Override
    public void initialize() {
        pivot.setPosition(position.getAsDouble());
    }
}
