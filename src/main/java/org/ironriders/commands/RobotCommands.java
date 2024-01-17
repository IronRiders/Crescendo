package org.ironriders.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import org.ironriders.constants.Game;
import org.ironriders.constants.Manipulator;
import org.ironriders.constants.Pivot;
import org.ironriders.subsystems.*;

public class RobotCommands {
    private final DriveCommands drive;
    private final ShooterCommands shooter;
    private final PivotCommands pivot;
    private final ManipulatorCommands manipulator;
    private final ClimberCommands climber;

    public RobotCommands(DriveSubsystem drive, ShooterSubsystem shooter, PivotSubsystem pivot,
                         ManipulatorSubsystem manipulator, ClimberSubsystem climber) {
        this.drive = drive.getCommands();
        this.shooter = shooter.getCommands();
        this.pivot = pivot.getCommands();
        this.manipulator = manipulator.getCommands();
        this.climber = climber.getCommands();
    }

    public Command amp() {
        return Commands.sequence(
                Commands.parallel(
                        pivot.set(Pivot.State.AMP),
                        drive.pathFindTo(Game.KeyLocations.AMP.getRobotPose())
                ),
                manipulator.set(Manipulator.State.EJECT_TO_AMP)
        );
    }

    public Command onStage(Game.Location location) {
        return Commands.deadline(
                drive.pathFindTo(location.getRobotPose()),
                climber.set(() -> 1, () -> 1)
        );
    }

    public Command launchAt(Game.Location location) {
        return Commands.sequence(
                Commands.parallel(
                        shooter.initialize(),
                        pivot.set(Pivot.State.SHOOTER),
                        drive.pathFindTo(location.getRobotPose())
                ),
                launch()
        );
    }

    public Command launch() {
        return Commands.sequence(
                Commands.parallel(
                        shooter.initialize(),
                        pivot.set(Pivot.State.SHOOTER)
                ),
                manipulator.set(Manipulator.State.EJECT_TO_SHOOTER),
                shooter.stop()
        );
    }

    public Command startGroundPickup() {
        return Commands.parallel(
                pivot.set(Pivot.State.GROUND),
                manipulator.set(Manipulator.State.GRAB),
                shooter.initialize()
        );
    }

    public Command endGroundPickup() {
        return Commands.parallel(
                pivot.set(Pivot.State.SHOOTER),
                manipulator.set(Manipulator.State.STOP)
        );
    }
}
