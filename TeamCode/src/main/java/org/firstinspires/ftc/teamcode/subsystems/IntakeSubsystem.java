package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Configs.IntakeConfig;
import org.firstinspires.ftc.teamcode.lib.Subsystem;

// Plain open-loop intake roller; no closed-loop control.
public class IntakeSubsystem extends Subsystem {

    private final DcMotor intakeMotor;

    public IntakeSubsystem(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotor.class, IntakeConfig.MOTOR_NAME);
        intakeMotor.setDirection(DcMotor.Direction.FORWARD);
    }
    public void intakeOn() {
        intakeMotor.setPower(IntakeConfig.INTAKE_POWER);
    }

    public void intakeReverse() {
        intakeMotor.setPower(IntakeConfig.INTAKE_REVERSE_POWER);
    }

    public void intakeStop() {
        intakeMotor.setPower(0);
    }
}
