package org.firstinspires.ftc.teamcode.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Singleton that owns subsystems/commands and drives them once per loop() call.
public class CommandScheduler {

    private static final CommandScheduler instance = new CommandScheduler();

    public static CommandScheduler getInstance() {
        return instance;
    }

    private CommandScheduler() {}

    private final List<Subsystem> subsystems = new ArrayList<>();
    private final Set<Command> scheduledCommands = new LinkedHashSet<>();
    private final Map<Subsystem, Command> requirements = new HashMap<>();
    private final Map<Subsystem, Command> defaultCommands = new HashMap<>();
    private final List<Trigger> triggers = new ArrayList<>();

    void registerSubsystem(Subsystem subsystem) {
        subsystems.add(subsystem);
    }

    void registerTrigger(Trigger trigger) {
        triggers.add(trigger);
    }

    // Command run automatically whenever no other command claims this subsystem.
    public void setDefaultCommand(Subsystem subsystem, Command command) {
        defaultCommands.put(subsystem, command);
    }

    // Interrupts whatever currently owns the command's required subsystems, then starts it.
    public void schedule(Command command) {
        for (Subsystem s : command.getRequirements()) {
            Command current = requirements.get(s);
            if (current != null && current != command) {
                cancel(current);
            }
        }
        for (Subsystem s : command.getRequirements()) {
            requirements.put(s, command);
        }
        scheduledCommands.add(command);
        command.initialize();
    }

    public void cancel(Command command) {
        if (!scheduledCommands.remove(command)) {
            return;
        }
        command.end(true);
        requirements.values().removeIf(c -> c == command);
    }

    // Call once per loop iteration: polls triggers, ticks commands, then subsystem periodics.
    public void run() {
        for (Trigger t : triggers) {
            t.poll();
        }

        List<Command> finished = new ArrayList<>();
        for (Command command : scheduledCommands) {
            command.execute();
            if (command.isFinished()) {
                finished.add(command);
            }
        }
        for (Command command : finished) {
            command.end(false);
            scheduledCommands.remove(command);
            requirements.values().removeIf(c -> c == command);
        }

        for (Map.Entry<Subsystem, Command> entry : defaultCommands.entrySet()) {
            if (!requirements.containsKey(entry.getKey())) {
                schedule(entry.getValue());
            }
        }

        for (Subsystem s : subsystems) {
            s.periodic();
        }
    }

    // Clears all state; call from OpMode init() since the singleton survives between opmode runs.
    public void reset() {
        scheduledCommands.clear();
        requirements.clear();
        defaultCommands.clear();
        subsystems.clear();
        triggers.clear();
    }
}
