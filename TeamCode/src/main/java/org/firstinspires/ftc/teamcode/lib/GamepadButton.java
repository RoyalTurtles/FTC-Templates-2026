package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.robotcore.hardware.Gamepad;

// Trigger bound to a single gamepad button, read fresh from the gamepad each poll.
public class GamepadButton extends Trigger {

    public GamepadButton(Gamepad gamepad, Button button) {
        super(() -> button.isPressed(gamepad));
    }

    public enum Button {
        A, B, X, Y,
        DPAD_UP, DPAD_DOWN, DPAD_LEFT, DPAD_RIGHT,
        LEFT_BUMPER, RIGHT_BUMPER,
        LEFT_STICK_BUTTON, RIGHT_STICK_BUTTON,
        BACK, START;

        boolean isPressed(Gamepad g) {
            switch (this) {
                case A: return g.a;
                case B: return g.b;
                case X: return g.x;
                case Y: return g.y;
                case DPAD_UP: return g.dpad_up;
                case DPAD_DOWN: return g.dpad_down;
                case DPAD_LEFT: return g.dpad_left;
                case DPAD_RIGHT: return g.dpad_right;
                case LEFT_BUMPER: return g.left_bumper;
                case RIGHT_BUMPER: return g.right_bumper;
                case LEFT_STICK_BUTTON: return g.left_stick_button;
                case RIGHT_STICK_BUTTON: return g.right_stick_button;
                case BACK: return g.back;
                case START: return g.start;
                default: return false;
            }
        }
    }
}
