package org.ironriders.lib.controllers;

import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.event.BooleanEvent;
import edu.wpi.first.wpilibj.event.EventLoop;

/**
 * Handle input from Xbox 360 or Xbox One controllers connected to the Driver Station.
 *
 * <p>This class handles Xbox input that comes from the Driver Station. Each time a value is
 * requested the most recent value is returned. There is a single class instance for each controller
 * and the mapping of ports to hardware buttons depends on the code in the Driver Station.
 */
public class XboxSeriesController extends GenericHID {
    /**
     * Construct an instance of a controller.
     *
     * @param port The port index on the Driver Station that the controller is plugged into.
     */
    public XboxSeriesController(final int port) {
        super(port);

        HAL.report(tResourceType.kResourceType_XboxController, port + 1);
    }

    /**
     * Get the X axis value of left side of the controller.
     *
     * @return The axis value.
     */
    public double getLeftX() {
        return getRawAxis(Axis.LeftX.value);
    }

    /**
     * Get the X axis value of right side of the controller.
     *
     * @return The axis value.
     */
    public double getRightX() {
        return getRawAxis(Axis.RightX.value);
    }

    /**
     * Get the Y axis value of left side of the controller.
     *
     * @return The axis value.
     */
    public double getLeftY() {
        return getRawAxis(Axis.LeftY.value);
    }

    /**
     * Get the Y axis value of right side of the controller.
     *
     * @return The axis value.
     */
    public double getRightY() {
        return getRawAxis(Axis.RightY.value);
    }

    /**
     * Get the left trigger (LT) axis value of the controller. Note that this axis is bound to the
     * range of [0, 1] as opposed to the usual [-1, 1].
     *
     * @return The axis value.
     */
    public double getLeftTriggerAxis() {
        return getRawAxis(Axis.LeftTrigger.value);
    }

    /**
     * Get the right trigger (RT) axis value of the controller. Note that this axis is bound to the
     * range of [0, 1] as opposed to the usual [-1, 1].
     *
     * @return The axis value.
     */
    public double getRightTriggerAxis() {
        return getRawAxis(Axis.RightTrigger.value);
    }

    /**
     * Read the value of the left bumper (LB) button on the controller.
     *
     * @return The state of the button.
     */
    public boolean getLeftBumper() {
        return getRawButton(Button.LeftBumper.value);
    }

    /**
     * Read the value of the right bumper (RB) button on the controller.
     *
     * @return The state of the button.
     */
    public boolean getRightBumper() {
        return getRawButton(Button.RightBumper.value);
    }

    /**
     * Whether the left bumper (LB) was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getLeftBumperPressed() {
        return getRawButtonPressed(Button.LeftBumper.value);
    }

    /**
     * Whether the right bumper (RB) was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getRightBumperPressed() {
        return getRawButtonPressed(Button.RightBumper.value);
    }

    /**
     * Whether the left bumper (LB) was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getLeftBumperReleased() {
        return getRawButtonReleased(Button.LeftBumper.value);
    }

    /**
     * Whether the right bumper (RB) was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getRightBumperReleased() {
        return getRawButtonReleased(Button.RightBumper.value);
    }

    /**
     * Constructs an event instance around the right bumper's digital signal.
     *
     * @param loop the event loop instance to attach the event to.
     * @return an event instance representing the right bumper's digital signal attached to the given
     * loop.
     */
    public BooleanEvent leftBumper(EventLoop loop) {
        return new BooleanEvent(loop, this::getLeftBumper);
    }

    /**
     * Constructs an event instance around the left bumper's digital signal.
     *
     * @param loop the event loop instance to attach the event to.
     * @return an event instance representing the left bumper's digital signal attached to the given
     * loop.
     */
    public BooleanEvent rightBumper(EventLoop loop) {
        return new BooleanEvent(loop, this::getRightBumper);
    }

    /**
     * Read the value of the left stick button (LSB) on the controller.
     *
     * @return The state of the button.
     */
    public boolean getLeftStickButton() {
        return getRawButton(Button.LeftStick.value);
    }

    /**
     * Read the value of the right stick button (RSB) on the controller.
     *
     * @return The state of the button.
     */
    public boolean getRightStickButton() {
        return getRawButton(Button.RightStick.value);
    }

    /**
     * Whether the left stick button (LSB) was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getLeftStickButtonPressed() {
        return getRawButtonPressed(Button.LeftStick.value);
    }

    /**
     * Whether the right stick button (RSB) was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getRightStickButtonPressed() {
        return getRawButtonPressed(Button.RightStick.value);
    }

    /**
     * Whether the left stick button (LSB) was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getLeftStickButtonReleased() {
        return getRawButtonReleased(Button.LeftStick.value);
    }

    /**
     * Whether the right stick (RSB) button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getRightStickButtonReleased() {
        return getRawButtonReleased(Button.RightStick.value);
    }

    /**
     * Constructs an event instance around the left stick button's digital signal.
     *
     * @param loop the event loop instance to attach the event to.
     * @return an event instance representing the left stick button's digital signal attached to the
     * given loop.
     */
    public BooleanEvent leftStick(EventLoop loop) {
        return new BooleanEvent(loop, this::getLeftStickButton);
    }

    /**
     * Constructs an event instance around the right stick button's digital signal.
     *
     * @param loop the event loop instance to attach the event to.
     * @return an event instance representing the right stick button's digital signal attached to the
     * given loop.
     */
    public BooleanEvent rightStick(EventLoop loop) {
        return new BooleanEvent(loop, this::getRightStickButton);
    }

    /**
     * Read the value of the A button on the controller.
     *
     * @return The state of the button.
     */
    public boolean getAButton() {
        return getRawButton(Button.A.value);
    }

    /**
     * Whether the A button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getAButtonPressed() {
        return getRawButtonPressed(Button.A.value);
    }

    /**
     * Whether the A button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getAButtonReleased() {
        return getRawButtonReleased(Button.A.value);
    }

    /**
     * Constructs an event instance around the A button's digital signal.
     *
     * @param loop the event loop instance to attach the event to.
     * @return an event instance representing the A button's digital signal attached to the given
     * loop.
     */
    @SuppressWarnings("MethodName")
    public BooleanEvent a(EventLoop loop) {
        return new BooleanEvent(loop, this::getAButton);
    }

    /**
     * Read the value of the B button on the controller.
     *
     * @return The state of the button.
     */
    public boolean getBButton() {
        return getRawButton(Button.B.value);
    }

    /**
     * Whether the B button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getBButtonPressed() {
        return getRawButtonPressed(Button.B.value);
    }

    /**
     * Whether the B button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getBButtonReleased() {
        return getRawButtonReleased(Button.B.value);
    }

    /**
     * Constructs an event instance around the B button's digital signal.
     *
     * @param loop the event loop instance to attach the event to.
     * @return an event instance representing the B button's digital signal attached to the given
     * loop.
     */
    @SuppressWarnings("MethodName")
    public BooleanEvent b(EventLoop loop) {
        return new BooleanEvent(loop, this::getBButton);
    }

    /**
     * Read the value of the X button on the controller.
     *
     * @return The state of the button.
     */
    public boolean getXButton() {
        return getRawButton(Button.X.value);
    }

    /**
     * Whether the X button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getXButtonPressed() {
        return getRawButtonPressed(Button.X.value);
    }

    /**
     * Whether the X button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getXButtonReleased() {
        return getRawButtonReleased(Button.X.value);
    }

    /**
     * Constructs an event instance around the X button's digital signal.
     *
     * @param loop the event loop instance to attach the event to.
     * @return an event instance representing the X button's digital signal attached to the given
     * loop.
     */
    @SuppressWarnings("MethodName")
    public BooleanEvent x(EventLoop loop) {
        return new BooleanEvent(loop, this::getXButton);
    }

    /**
     * Read the value of the Y button on the controller.
     *
     * @return The state of the button.
     */
    public boolean getYButton() {
        return getRawButton(Button.Y.value);
    }

    /**
     * Whether the Y button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getYButtonPressed() {
        return getRawButtonPressed(Button.Y.value);
    }

    /**
     * Whether the Y button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getYButtonReleased() {
        return getRawButtonReleased(Button.Y.value);
    }

    /**
     * Constructs an event instance around the Y button's digital signal.
     *
     * @param loop the event loop instance to attach the event to.
     * @return an event instance representing the Y button's digital signal attached to the given
     * loop.
     */
    @SuppressWarnings("MethodName")
    public BooleanEvent y(EventLoop loop) {
        return new BooleanEvent(loop, this::getYButton);
    }

    /**
     * Read the value of the view button on the controller.
     *
     * @return The state of the button.
     */
    public boolean getViewButton() {
        return getRawButton(Button.View.value);
    }

    /**
     * Whether the view button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getViewButtonPressed() {
        return getRawButtonPressed(Button.View.value);
    }

    /**
     * Whether the view button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getViewButtonReleased() {
        return getRawButtonReleased(Button.View.value);
    }

    /**
     * Constructs an event instance around the back menu's digital signal.
     *
     * @param loop the event loop instance to attach the event to.
     * @return an event instance representing the back button's digital signal attached to the given
     * loop.
     */
    public BooleanEvent view(EventLoop loop) {
        return new BooleanEvent(loop, this::getViewButton);
    }

    /**
     * Read the value of the menu button on the controller.
     *
     * @return The state of the button.
     */
    public boolean getMenuButton() {
        return getRawButton(Button.Menu.value);
    }

    /**
     * Whether the menu button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getMenuButtonPressed() {
        return getRawButtonPressed(Button.Menu.value);
    }

    /**
     * Whether the menu button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getMenuButtonReleased() {
        return getRawButtonReleased(Button.Menu.value);
    }

    /**
     * Constructs an event instance around the menu button's digital signal.
     *
     * @param loop the event loop instance to attach the event to.
     * @return an event instance representing the start button's digital signal attached to the given
     * loop.
     */
    public BooleanEvent menu(EventLoop loop) {
        return new BooleanEvent(loop, this::getMenuButton);
    }

    /**
     * Read the value of the share button on the controller.
     *
     * @return The state of the button.
     */
    public boolean getShareButton() {
        return getRawButton(Button.Share.value);
    }

    /**
     * Whether the share button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getShareButtonPressed() {
        return getRawButtonPressed(Button.Share.value);
    }

    /**
     * Whether the start button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getShareButtonReleased() {
        return getRawButtonReleased(Button.Share.value);
    }

    /**
     * Constructs an event instance around the start button's digital signal.
     *
     * @param loop the event loop instance to attach the event to.
     * @return an event instance representing the start button's digital signal attached to the given
     * loop.
     */
    public BooleanEvent share(EventLoop loop) {
        return new BooleanEvent(loop, this::getShareButton);
    }

    /**
     * Constructs an event instance around the axis value of the left trigger. The returned trigger
     * will be true when the axis value is greater than {@code threshold}.
     *
     * @param threshold the minimum axis value for the returned {@link BooleanEvent} to be true. This
     *                  value should be in the range [0, 1] where 0 is the unpressed state of the axis.
     * @param loop      the event loop instance to attach the event to.
     * @return an event instance that is true when the left trigger's axis exceeds the provided
     * threshold, attached to the given event loop
     */
    public BooleanEvent leftTrigger(double threshold, EventLoop loop) {
        return new BooleanEvent(loop, () -> getLeftTriggerAxis() > threshold);
    }

    /**
     * Constructs an event instance around the axis value of the left trigger. The returned trigger
     * will be true when the axis value is greater than 0.5.
     *
     * @param loop the event loop instance to attach the event to.
     * @return an event instance that is true when the left trigger's axis exceeds the provided
     * threshold, attached to the given event loop
     */
    public BooleanEvent leftTrigger(EventLoop loop) {
        return leftTrigger(0.5, loop);
    }

    /**
     * Constructs an event instance around the axis value of the right trigger. The returned trigger
     * will be true when the axis value is greater than {@code threshold}.
     *
     * @param threshold the minimum axis value for the returned {@link BooleanEvent} to be true. This
     *                  value should be in the range [0, 1] where 0 is the unpressed state of the axis.
     * @param loop      the event loop instance to attach the event to.
     * @return an event instance that is true when the right trigger's axis exceeds the provided
     * threshold, attached to the given event loop
     */
    public BooleanEvent rightTrigger(double threshold, EventLoop loop) {
        return new BooleanEvent(loop, () -> getRightTriggerAxis() > threshold);
    }

    /**
     * Constructs an event instance around the axis value of the right trigger. The returned trigger
     * will be true when the axis value is greater than 0.5.
     *
     * @param loop the event loop instance to attach the event to.
     * @return an event instance that is true when the right trigger's axis exceeds the provided
     * threshold, attached to the given event loop
     */
    public BooleanEvent rightTrigger(EventLoop loop) {
        return rightTrigger(0.5, loop);
    }

    /**
     * Represents a digital button on an XboxController.
     */
    public enum Button {
        LeftBumper(7),
        RightBumper(8),
        LeftStick(14),
        RightStick(15),
        A(1),
        B(2),
        X(3),
        Y(4),
        View(11),
        Share(16),
        Menu(12);

        public final int value;

        Button(int value) {
            this.value = value;
        }

        /**
         * Get the human-friendly name of the button, matching the relevant methods. This is done by
         * stripping the leading `k`, and if not a Bumper button append `Button`.
         *
         * <p>Primarily used for automated unit tests.
         *
         * @return the human-friendly name of the button.
         */
        @Override
        public String toString() {
            if (this.name().endsWith("Bumper")) {
                return this.name();
            }
            return this.name() + "Button";
        }
    }

    /**
     * Represents an axis on an XboxController.
     */
    public enum Axis {
        LeftX(0),
        RightX(2),
        LeftY(1),
        RightY(3),
        LeftTrigger(5),
        RightTrigger(4);

        public final int value;

        Axis(int value) {
            this.value = value;
        }

        /**
         * Get the human-friendly name of the axis, matching the relevant methods. This is done by
         * stripping the leading `k`, and if a trigger axis append `Axis`.
         *
         * <p>Primarily used for automated unit tests.
         *
         * @return the human-friendly name of the axis.
         */
        @Override
        public String toString() {
            if (this.name().endsWith("Trigger")) {
                return this.name() + "Axis";
            }
            return this.name();
        }
    }
}
