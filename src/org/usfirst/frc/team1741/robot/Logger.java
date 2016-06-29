package org.usfirst.frc.team1741.robot;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map.Entry;

public class Logger 
{
	private String filename;
	private PrintWriter m_log;
	private ArrayList< Entry<String, String> > m_fields;

	Logger()
	{
		m_fields = new ArrayList<Entry<String,String>>();
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
		m_log.close();
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
		return (FindField(name) != -1);
	}

	// TODO: needs some serious optimization most likely
	int FindField(String name)
	{
		String real_name = Normalize(name);
		for (Entry<String,String> e : m_fields)
		{
			if (real_name == e.getKey())
			{
				return m_fields.indexOf(e);
			}
		}
		return -1;
	}

	boolean AddAttribute(String field)
	{
		if (HasAttribute(field)) {
			// TODO: Output warning
			return false; // We already have this attribute
		}

		m_fields.add(new AbstractMap.SimpleEntry<String,String>(Normalize(field), ""));

		return true;
	}

	boolean Log(String field, double d)
	{
		return Log(field, String.valueOf(d));
	}

	boolean Log(String field, String data)
	{;
		int idx = FindField(field);
		if (idx < 0) return false;

		m_fields.get(idx).setValue(data);
		return true;
	}

	boolean WriteAttributes()
	{
		for (Entry<String,String> e : m_fields)
		{
			m_log.print(e.getKey() + ',');
		}
		m_log.println();
		return !m_log.checkError();
	}

	boolean WriteLine()
	{
		for (Entry<String,String> e : m_fields)
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
