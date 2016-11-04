package org.usfirst.frc.team1741.robot;

import edu.wpi.first.wpilibj.PIDOutput;

public class FakePIDOutput implements PIDOutput
{
	private double value;
	
	public FakePIDOutput()
	{
		value = 0.0;
	}
	
	public FakePIDOutput(double val)
	{
		value = val;
	}

	@Override
	public void pidWrite(double v) 
	{
		value = v;
	}

	public double pidGet() 
	{
		return value;
	}
}
