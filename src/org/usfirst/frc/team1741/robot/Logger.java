package org.usfirst.frc.team1741.robot;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Logger 
{
	private String filename;
	private PrintWriter m_log = null;
	private Map<String, String> m_fields;

	Logger()
	{
		m_fields = new HashMap<String,String>();
	}

	boolean Open(String filename)
	{
		this.filename = filename;
		try
		{
			m_log = new PrintWriter(filename);
		}
		catch (FileNotFoundException e)
		{
			return false;
		}
		return true;
	}

	void Close()
	{
		if(m_log!=null)
		{
			m_log.close();
		}
	}

	boolean Reset()
	{
		Close();
		Open(this.filename);
		WriteAttributes();
		return true;
	}

	boolean HasAttribute(String name)
	{
		return m_fields.containsKey(name);
	}

	// TODO: needs some serious optimization most likely
//	Entry<String,String> FindField(String name)
//	{
//		String real_name = Normalize(name);
//		for (Entry<String,String> e : m_fields)
//		{
//			if (real_name == e.getKey())
//			{
//				System.out.println(e.getKey());
//				return e;
//			}
//		}
//		return null;
//	}

	boolean AddAttribute(String field)
	{
		if (HasAttribute(field)) {
			// TODO: Output warning
			return false; // We already have this attribute
		}

		m_fields.put(field, "");

		return true;
	}

	boolean Log(String field, double d)
	{
		return Log(field, String.valueOf(d));
	}

	boolean Log(String field, String data)
	{
		if(!HasAttribute(field)) return false;
		
		m_fields.put(field, data);
		return true;
	}

	boolean WriteAttributes()
	{
		for (Map.Entry<String,String> e : m_fields.entrySet())
		{
			m_log.print(e.getKey() + ',');
		}
		m_log.println();
		return !m_log.checkError();
	}

	boolean WriteLine()
	{
		for (Map.Entry<String,String> e : m_fields.entrySet())
		{
			m_log.print(e.getValue() + ',');
		}
		m_log.println();
		return !m_log.checkError();
	}

	String Normalize(String str)
	{
		return str.toLowerCase();
	}
}
