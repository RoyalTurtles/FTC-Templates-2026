package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.lib.Command;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

// Stops the flywheel and finishes immediately.
public class StopShooter extends Command {

    private final ShooterSubsystem shooter;

    public StopShooter(ShooterSubsystem shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        shooter.stopShooter();
    }
}
