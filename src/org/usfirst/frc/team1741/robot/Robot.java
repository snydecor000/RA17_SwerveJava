
package org.usfirst.frc.team1741.robot;

import java.io.File;
import java.util.Calendar;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    String autoSelected;
    SendableChooser chooser;
    
	CANTalon motorFR;
	CANTalon angleFR;
	AnalogInput absEncFR;
	AnalogGyro gyro;
	BuiltInAccelerometer acceler;
	Logger logger;
	Timer timer;
	Drive drive;
	Gamepad driver;
    
	
    Robot()
    {
    	chooser = null;
		motorFR = null;
		angleFR = null;
		absEncFR = null;
		gyro = null;
		acceler = null;
		logger = null;
		timer = null;
		drive = null;
		driver = null;
    }

    public void robotInit() 
    {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
        
		motorFR = new CANTalon(3);
		angleFR = new CANTalon(7);
		////////////////////////////////////////////////
		absEncFR = new AnalogInput(0);
		////////////////////////////////////////////////
//		gyro = new AnalogGyro(4);
//		gyro->Reset();
//		gyro->Calibrate();
//		acceler = new BuiltInAccelerometer();
		////////////////////////////////////////////////
		logger = new Logger();
		timer = new Timer();
		////////////////////////////////////////////////
		drive = new Drive(motorFR,angleFR,absEncFR);
		////////////////////////////////////////////////
		driver = new Gamepad(0);
    }
    
    public void autonomousInit() 
    {
    	ReloadConfig();
    	autoSelected = (String) chooser.getSelected();
//		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
    }

    public void autonomousPeriodic() 
    {
    	switch(autoSelected) {
    	case customAuto:
        //Put custom auto code here   
            break;
    	case defaultAuto:
    	default:
    	//Put default auto code here
            break;
    	}
    }

    public void teleopInit()
    {
    	ReloadConfig();
    }
    
    public void teleopPeriodic() 
    {
    	drive.Swerve(driver.GetRightX(),driver.GetRightY(),driver.GetLeftX(),0);
    }
    
    public void testInit() 
    {
    	ReloadConfig();
    }
    
    public void testPeriodic() 
    {
    
    }
    
    public void disabledInit() 
    {
    
    }
    
    public void disabledPeriodic() 
    {
    
    }
    
	void StartLogging(String mode, Logger l)
	{
		String robot = !(Config.GetSetting("isPrototype", 0) == 0) ? "_proto" : "_comp";
		l.Close();
		Calendar calendar = Calendar.getInstance();
		String dir = "/home/lvuser";
		if(new File("/media/sda").exists())
		{
		    dir = "/media/sda";
		}
		String name = dir + "/log-" + calendar.get(Calendar.YEAR) + "-" +
				calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "_" +
				calendar.get(Calendar.HOUR_OF_DAY) + "-" + calendar.get(Calendar.MINUTE) + "-" +
				calendar.get(Calendar.SECOND) + "_" + mode + "_" + robot + ".csv";
		System.out.println(name);
		l.Open(name);
	}

	void SetupLogging()
	{
		logger.AddAttribute("Time");
//		logger.AddAttribute("AccX");
//		logger.AddAttribute("AccY");
//		logger.AddAttribute("AccZ");
		drive.SetupLogging(logger);
		logger.WriteAttributes();
	}

	void Log(float time)
	{
		logger.Log("Time", time);
//		logger.Log("AccX", acceler.GetX());
//		logger.Log("AccY", acceler.GetY());
//		logger.Log("AccZ", acceler.GetZ());
		drive.Log(logger);
		logger.WriteLine();
	}

	void ReloadConfig()
	{
		Config.LoadFromFile("/home/lvuser/config.txt");
		drive.ReloadConfig();
	}
}
