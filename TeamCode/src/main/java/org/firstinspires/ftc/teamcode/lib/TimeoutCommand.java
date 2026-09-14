package org.firstinspires.ftc.teamcode.lib;

// Wraps a command so it also finishes once a time limit elapses; backs Command#withTimeout.
class TimeoutCommand extends Command {

    private final Command command;
    private final double timeoutSeconds;
    private long startTimeNanos;

    TimeoutCommand(Command command, double timeoutSeconds) {
        this.command = command;
        this.timeoutSeconds = timeoutSeconds;
        addRequirements(command.getRequirements().toArray(new Subsystem[0]));
    }

    @Override
    public void initialize() {
        startTimeNanos = System.nanoTime();
        command.initialize();
    }

    @Override
    public void execute() {
        command.execute();
    }

    @Override
    public boolean isFinished() {
        double elapsed = (System.nanoTime() - startTimeNanos) / 1e9;
        return command.isFinished() || elapsed >= timeoutSeconds;
    }

    @Override
    public void end(boolean interrupted) {
        command.end(interrupted);
    }
}
