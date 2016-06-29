package org.usfirst.frc.team1741.robot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.naming.directory.InvalidSearchFilterException;

public class Config 
{
	private static Map<String, Double> m_settings;

	public static void Dump()
	{
		System.out.println("DUMP");
		for(Map.Entry<String, Double> e : m_settings.entrySet())
		{
			System.out.println(e.getKey() + ": " + e.getValue());
		}
		System.out.println("END DUMP");
	}
	
	public static boolean LoadFromFile(String filename)
	{
		return Parse(filename);
	}

	public static double GetSetting(String name, double reasonable_default)
	{
		double retval = reasonable_default;
		name = ConvertToLower(name);
		
		if (m_settings.containsKey(name)) 
		{
			retval = m_settings.get(name);
		}

		return retval;
	}

	public static void SetSetting(String name, double value)
	{
		name = ConvertToLower(name);
		m_settings.put(name, value);
	}

	public static String ConvertToLower(String str)
	{
		str = str.toLowerCase();
		return str;
	}

	static boolean Parse(String filename)
	{
		m_settings = new HashMap<String,Double>();
		Scanner infile;
		try
		{
			infile = new Scanner(new File(filename));
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Couldn't find config file \"" + filename + "\"");
			return false;
		} 

		Pattern p = Pattern.compile("(#{0})[\\w\\d_]+ ?= ?\\d+(\\.\\d+)?");
		
		while(infile.hasNextLine())
		{
			String in = infile.nextLine();
			if(p.matcher(in).matches())
			{
				String[] key = in.split(" ?=");
				Double value = Double.parseDouble(key[1]);
				m_settings.put(ConvertToLower(key[0]), value);
				System.out.println(key[0] + ": " + value);
			}
		}
		infile.close();
		return true;
	}
		
//		String line = "";
//		int line_number = 0;
//		while (infile.hasNext()) {
//			++line_number;
//			getline(infile,line);
//
//			String name, value_str;
//			int state = 0;
//			boolean done = false;
//			boolean invalid = false;
//			
//			// 
//			// Process each line to eliminate comments.
//			for (unsigned int i = 0; i < line.length() && !done; ++i)
//			{
//				switch(state) {
//				case 0: // Ignoring initial whitespace,
//					    // looking for either comment
//					    // or valid key name
//					//cout<<"Entering state 0 ......"<<endl;
//					if (isspace(line[i])) {
//						continue;
//					} else if (line[i] == '#') {
//						state = 3;
//					} else {
//						name += line[i];
//						state = 1;
//					}
//					break;
//				case 1:
//					// Currently reading key name,
//					// looking for equals sign
//					//cout<<"Entering state 1 ......"<<endl;
//					if (isspace(line[i])) {
//						// Ignore for now
//					} else if (line[i] == '=') {
//						state = 2;
//					} else if (! ValidKeyChar(line[i])) {
//						state = 3;
//						invalid = true;
//					} else {
//						name += line[i];
//					}
//					break;
//				case 2:
//					// Just read equals sign, reading 
//					// value string
//					//cout<<"Entering state 2 ......"<<endl;
//					if (isspace(line[i])) {
//						// Ignore!
//
//					} else if (line[i] == '#') {
//						state = 3;
//					} else {
//						value_str += line[i];
//					}
//					break;
//				case 3:
//					//cout<<"Entering state 3 ......"<<endl;
//					done = true;
//					continue; // Ignore anything after the 
//					break;
//				}
//			}
//
//			if (!invalid) {
//				name = ConvertToLower(name);
//				double val = ::atof(value_str.c_str());
//				//cout<<"Name: "<<name<<endl<<"Value: "<<val<<endl;
//				SetSetting(name, val);
//			} else {
//				cerr << "Invalid string on line " << line_number << ": " << line << endl;
//			}
//		}
//
//	}

}
