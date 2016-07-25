package org.usfirst.frc.team1741.robot;

public class EdgeDetect 
{
	private boolean state;
	
	EdgeDetect()
	{
		state = false;
	}
	
	boolean Check(boolean in)
	{
		boolean out;
		if(in && state)
		{
			out = true;
		}
		else
		{
			out = false;
		}

		if(in)
		{
			state = false;
		}
		else
		{
			state = true;
		}
		return out;
	}
}
