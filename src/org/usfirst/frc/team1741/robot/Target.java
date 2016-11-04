package org.usfirst.frc.team1741.robot;

import java.util.Arrays;
import java.util.List;

public class Target 
{
	private double tilt;
	private double distance;
	private double pan;
	
	public Target(double d, double p, double t)
	{
		tilt = t;
	  	distance = d;
	  	pan = p;
		// Nothing to do here.
	}

	public Target(String in)
	{
		List<String> items = Arrays.asList(in.split("\\s*,\\s*"));

		distance = Double.parseDouble(items.get(0));
		pan = Double.parseDouble(items.get(1));
		tilt = Double.parseDouble(items.get(2));
	}

	public double getDistance()
	{
		return distance;
	}

	public double getPan()
	{
		return pan;
	}

	public double getTilt()
	{
		return tilt;
	}
}
