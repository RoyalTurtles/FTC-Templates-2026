package org.firstinspires.ftc.teamcode.lib;

// Base class for a robot subsystem; auto-registers with the scheduler on construction.
public abstract class Subsystem {

    public Subsystem() {
        CommandScheduler.getInstance().registerSubsystem(this);
    }

    // Called once per scheduler run() regardless of which command is active.
    public void periodic() {}
}
