package org.firstinspires.ftc.teamcode.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

// Edge-detects a boolean condition each loop and fires bound commands; mirrors WPILib's Trigger.
public class Trigger {

    private final BooleanSupplier condition;
    private boolean lastState;
    private final List<Runnable> whenActiveActions = new ArrayList<>();
    private final List<Runnable> whenInactiveActions = new ArrayList<>();

    public Trigger(BooleanSupplier condition) {
        this.condition = condition;
        CommandScheduler.getInstance().registerTrigger(this);
    }

    public Trigger whenActive(Command command) {
        whenActiveActions.add(command::schedule);
        return this;
    }

    public Trigger whenInactive(Command command) {
        whenInactiveActions.add(() -> CommandScheduler.getInstance().cancel(command));
        return this;
    }

    public boolean get() {
        return condition.getAsBoolean();
    }

    // Polled once per scheduler run() to detect rising/falling edges.
    void poll() {
        boolean current = condition.getAsBoolean();
        if (current && !lastState) {
            whenActiveActions.forEach(Runnable::run);
        }
        if (!current && lastState) {
            whenInactiveActions.forEach(Runnable::run);
        }
        lastState = current;
    }
}
