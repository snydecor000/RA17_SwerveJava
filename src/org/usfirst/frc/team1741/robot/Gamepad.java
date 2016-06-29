package org.usfirst.frc.team1741.robot;

import edu.wpi.first.wpilibj.DriverStation;

public class Gamepad 
{
    public enum AxisType
    {
        kLeftXAxis, kLeftYAxis, kRightXAxis, kRightYAxis, kLTrigger, kRTrigger
    }

    public enum DPadDirection
    {
        kCenter, kUp, kUpLeft, kLeft, kDownLeft, kDown, kDownRight, kRight,
        kUpRight
    }
    
    protected class AxisNumber
    {
		static final int LEFT_X_AXIS_NUM = 0;
		static final int LEFT_Y_AXIS_NUM = 1;
		static final int LEFT_TRIGGER_AXIS = 2;
		static final int RIGHT_TRIGGER_AXIS = 3;
		static final int RIGHT_X_AXIS_NUM = 4;
		static final int RIGHT_Y_AXIS_NUM = 5;
		static final int DPAD_X_AXIS_NUM = 6;
		static final int DPAD_Y_AXIS_NUM = 7;
	}

	protected enum StickButton
	{
		kLeftAnalogStickButton(11),
		kRightAnalogStickButton(12);
		
		private final int value;

	    private StickButton(int value) 
	    {
	        this.value = value;
	    }

	    public int getValue() 
	    {
	        return value;
	    }
	}

    protected DriverStation ap_ds;
    protected int a_port;

    
    
	Gamepad(int port)
	{
	    a_port = port;
	    ap_ds = DriverStation.getInstance();
	}
	
	/**
	 * Get the X value of the left analog stick.
	 */
	float GetLeftX()
	{
	    return GetRawAxis(AxisNumber.LEFT_X_AXIS_NUM);
	}

	/**
	 * Get the Y value of the left analog stick.
	 */
	float GetLeftY()
	{
	    return GetRawAxis(AxisNumber.LEFT_Y_AXIS_NUM);
	}

	/**
	 * Get the X value of the right analog stick.
	 */
	float GetRightX()
	{
	    return GetRawAxis(AxisNumber.RIGHT_X_AXIS_NUM);
	}

	/**
	 * Get the Y value of the right analog stick.
	 */
	float GetRightY()
	{
	    return GetRawAxis(AxisNumber.RIGHT_Y_AXIS_NUM);
	}

	/**
	 * Get the value of the axis.
	 *
	 * @param axis The axis to read [1-6].
	 * @return The value of the axis.
	 */
	float GetRawAxis(int axis)
	{
	    return (float) ap_ds.getStickAxis(a_port, (int) axis);
	}

	/**
	 * Return the axis determined by the argument.
	 *
	 * This is for cases where the gamepad axis is returned programatically,
	 * otherwise one of the previous functions would be preferable (for example
	 * GetLeftX()).
	 *
	 * @param axis The axis to read.
	 * @return The value of the axis.
	 */
	float GetAxis(AxisType axis)
	{
	    switch(axis)
	    {
	        case kLeftXAxis: return GetLeftX();
	        case kLeftYAxis: return GetLeftY();
	        case kRightXAxis: return GetRightX();
	        case kRightYAxis: return GetRightY();
	        case kRTrigger: return GetRTriggerAxis();
	        case kLTrigger: return GetLTriggerAxis();
	        default:
	            //wpi_fatal(BadJoystickAxis);
	            return (float) 0.0;
	    }
	}

	/**
	 * Get the button value for buttons 1 through 12.
	 *
	 * The buttons are returned in a single 16 bit value with one bit representing
	 * the state of each button. The appropriate button is returned as a boolean
	 * value.
	 *
	 * @param button The button number to be read.
	 * @return The state of the button.
	 **/
	boolean GetNumberedButton(int button)
	{
	    return ((0x1 << (button-1)) & ap_ds.getStickButtons(a_port)) != 0;
	}
	
	/**
	 * Gets whether or not the left analog stick is depressed.
	 *
	 * @return The state of the left analog stick button.
	 */
	boolean GetLeftPush()
	{
	    return GetNumberedButton(StickButton.kLeftAnalogStickButton.getValue());
	}

	/**
	 * Gets whether or not the right analog stick is depressed.
	 *
	 * @return The state of the right analog stick button.
	 */
	boolean GetRightPush()
	{
	    return GetNumberedButton(StickButton.kRightAnalogStickButton.getValue());
	}
	
    boolean GetA() { return GetNumberedButton(1); }
    boolean GetB() { return GetNumberedButton(2); }
    boolean GetY() { return GetNumberedButton(4); }
    boolean GetX() { return GetNumberedButton(3); }
    boolean GetLeftBumper() { return GetNumberedButton(5); }
    boolean GetRightBumper() { return GetNumberedButton(6); }
    boolean GetBack() { return GetNumberedButton(7); }
    boolean GetStart() { return GetNumberedButton(8); }
    boolean GetLeftTrigger() { return GetNumberedButton(7); }
    boolean GetRightTrigger() { return GetNumberedButton(9); }
    float GetLTriggerAxis() { return GetRawAxis(AxisNumber.LEFT_TRIGGER_AXIS); }
    float GetRTriggerAxis() { return GetRawAxis(AxisNumber.RIGHT_TRIGGER_AXIS); }

	/*
	 * Returns a DPad Direction, not degrees.
	 */
	DPadDirection GetDPad()
	{
	    int pos = ap_ds.getStickPOV(a_port, 0);

	    if (pos == 315)
	        return DPadDirection.kUpLeft;
	    if (pos == 225)
	        return DPadDirection.kDownLeft;
	    if (pos == 135)
	        return DPadDirection.kDownRight;
	    if (pos == 45)
	        return DPadDirection.kUpRight;
	    if (pos == 0)
	        return DPadDirection.kUp;
	    if (pos == 270)
	        return DPadDirection.kLeft;
	    if (pos == 180)
	        return DPadDirection.kDown;
	    if (pos == 90)
	        return DPadDirection.kRight;

	  return DPadDirection.kCenter;
	}


	/*
	 * AKA getDpadRaw()
	 */
	int GetPOV()
	{
		return ap_ds.getStickPOV(a_port, 0);
	}
}
