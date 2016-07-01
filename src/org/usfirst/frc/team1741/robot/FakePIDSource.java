package org.usfirst.frc.team1741.robot;

import java.util.Scanner;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class FakePIDSource implements PIDSource
{
	private double m_value;
	private double m_offset;
	private double m_min;
	private double m_max;
	private double m_diff;
	
	FakePIDSource(double offset,double min,double max)
	{
		m_offset = offset;
		m_min = min;
		m_max = max;
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) 
	{
		
	}

	@Override
	public PIDSourceType getPIDSourceType() 
	{
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet() 
	{
		return m_value;
	}
	
	public void pidSet(double value) 
	{
		m_value = value + m_offset;
		if(m_value <= m_max && m_value >= m_min)//checks to see if within normal range
		{
			//nothing happens, pidGet will return a proper value 
		}
		else if(m_value > m_max)//checks if too high
		{
			m_diff = m_value - m_max;//get difference
			m_value = m_min + m_diff;//sets value to wrap from max to min and add the diff
		}
		else if(m_value < m_min)//checks if too high
		{
			m_diff = m_min - m_value;//get difference
			m_value = m_max - m_diff;//sets value to wrap from min to max and subtract the diff
		}
	}
	
	public void setOffset(double offset)
	{
		m_offset = offset;
	}
	
	public void setMinMax(double min, double max)
	{
		m_min = min;
		m_max = max;
	}
	
}

