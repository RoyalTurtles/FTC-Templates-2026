package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.lib.Command;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

// Runs the indexer forward or reverse while held; bind with whenActive and whenInactive on the same instance.
public class RunIndex extends Command {

    private final ShooterSubsystem shooter;
    private final boolean reverse;

    public RunIndex(ShooterSubsystem shooter, boolean reverse) {
        this.shooter = shooter;
        this.reverse = reverse;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        if (reverse) {
            shooter.indexReverse();
        } else {
            shooter.indexOn();
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        shooter.indexStop();
    }
}
