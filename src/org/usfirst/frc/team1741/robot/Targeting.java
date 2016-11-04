package org.usfirst.frc.team1741.robot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Targeting implements Loggable
{
	private static NetworkTable grip;
	private String trackingBlob;
	public Targeting()
	{
		trackingBlob = "No data";
		if(grip == null)
		{
			grip = NetworkTable.getTable("Targeting");
		}
	}

	public List<Target> GetTargets()
	{
		String t  = grip.getString("targets", "");
		trackingBlob = t;
		List<String> strings = Arrays.asList(trackingBlob.split("|"));
		List<Target> targets = new ArrayList<Target>();
		for (String s : strings)
		{
			targets.add(new Target(s));
		}
		return targets;
	}

	public void setupLogging(Logger logger)
	{
		logger.AddAttribute("TrackingbBlob");
	}

	public void log(Logger logger)
	{
		trackingBlob = grip.getString("targets", "");
		if(!trackingBlob.equals(""))
		{
			logger.Log("TrackingBlob", trackingBlob);
		}
		else
		{
			logger.Log("TrackingBlob", "No data");
		}
	}
}
