package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.lib.Command;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

// Runs the intake forward or reverse while held; bind with whenActive and whenInactive on the same instance.
public class RunIntake extends Command {

    private final ShooterSubsystem shooter;
    private final boolean reverse;

    public RunIntake(ShooterSubsystem shooter, boolean reverse) {
        this.shooter = shooter;
        this.reverse = reverse;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        if (reverse) {
            shooter.intakeReverse();
        } else {
            shooter.intakeOn();
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        shooter.intakeStop();
    }
}
