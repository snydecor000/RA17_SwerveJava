
package org.usfirst.frc.team1741.robot;

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
    	
    }
    
    public void teleopPeriodic() 
    {
    	drive.Swerve(driver.GetRightX(),driver.GetRightY(),driver.GetLeftX(),0);
    }
    
    public void testInit() 
    {
    
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
//		String robot = !(Config::GetSetting("isPrototype", 0) == 0) ? "_proto" : "_comp";
//		l.Close();
//		time_t t = time(0);
//		struct tm *now = localtime(&t);
//		//don't touch it
//		std::string dir = "/home/lvuser";
//		struct stat st;
//		if(stat("/media/sda",&st) == 0)
//		{
//		    if(st.st_mode & (S_IFDIR != 0))
//		    {
//		    	dir = "/media/sda";
//		    }
//		}
//		std::string name = dir + "/log-" +
//				std::to_string(now.tm_year + 1900) +
//				"-\0" +
//				std::to_string(now.tm_mon + 1) +
//				"-\0" + std::to_string(now.tm_mday) + "_\0" +
//				std::to_string(now.tm_hour) + "-\0" +
//				std::to_string(now.tm_min) + "-\0" + std::to_string(now.tm_sec) + "-\0" + mode + robot + ".csv";
//		cout << name << endl;
//		l.Open(name);
	}

	void SetupLogging()
	{
//		logger.AddAttribute("Time");
////		logger.AddAttribute("AccX");
////		logger.AddAttribute("AccY");
////		logger.AddAttribute("AccZ");
//		drive.SetupLogging(logger);
//		logger.WriteAttributes();
	}

	void Log(float time)
	{
//		logger.Log("Time", time);
////		logger.Log("AccX", acceler.GetX());
////		logger.Log("AccY", acceler.GetY());
////		logger.Log("AccZ", acceler.GetZ());
//		drive.Log(logger);
//		logger.WriteLine();
	}

	void ReloadConfig()
	{
//		Config::LoadFromFile("/home/lvuser/config.txt");
//		drive.ReloadConfig();
	}
}
