package org.firstinspires.ftc.teamcode.lib;

// Runs a list of commands one after another; backs Command#andThen.
class SequentialCommand extends Command {

    private final Command[] commands;
    private int index;

    SequentialCommand(Command... commands) {
        this.commands = commands;
        for (Command c : commands) {
            addRequirements(c.getRequirements().toArray(new Subsystem[0]));
        }
    }

    @Override
    public void initialize() {
        index = 0;
        if (commands.length > 0) {
            commands[0].initialize();
        }
    }

    @Override
    public void execute() {
        if (index >= commands.length) {
            return;
        }
        Command current = commands[index];
        current.execute();
        if (current.isFinished()) {
            current.end(false);
            index++;
            if (index < commands.length) {
                commands[index].initialize();
            }
        }
    }

    @Override
    public boolean isFinished() {
        return index >= commands.length;
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted && index < commands.length) {
            commands[index].end(true);
        }
    }
}
