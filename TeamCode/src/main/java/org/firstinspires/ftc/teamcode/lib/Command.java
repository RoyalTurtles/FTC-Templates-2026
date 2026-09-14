package org.firstinspires.ftc.teamcode.lib;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// Base class for a unit of robot behavior; mirrors WPILib's Command lifecycle.
public abstract class Command {

    private final Set<Subsystem> requirements = new HashSet<>();

    public void initialize() {}
    public void execute() {}
    public void end(boolean interrupted) {}

    public boolean isFinished() {
        return true;
    }

    // Declares which subsystems this command needs exclusive use of.
    protected void addRequirements(Subsystem... subsystems) {
        requirements.addAll(Arrays.asList(subsystems));
    }

    public Set<Subsystem> getRequirements() {
        return requirements;
    }

    public void schedule() {
        CommandScheduler.getInstance().schedule(this);
    }

    public Command andThen(Command next) {
        return new SequentialCommand(this, next);
    }

    public Command withTimeout(double seconds) {
        return new TimeoutCommand(this, seconds);
    }
}
