package org.firstinspires.ftc.teamcode.commands;

import java.util.function.DoubleSupplier;

import org.firstinspires.ftc.teamcode.lib.Command;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

// Spins the flywheel up to a target velocity and finishes immediately; the PID loop keeps running in periodic().
public class SetShooterVelocity extends Command {

    private final ShooterSubsystem shooter;
    private final DoubleSupplier velocity;

    public SetShooterVelocity(ShooterSubsystem shooter, DoubleSupplier velocity) {
        this.shooter = shooter;
        this.velocity = velocity;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        shooter.setTargetVelocity(velocity.getAsDouble());
    }
}
