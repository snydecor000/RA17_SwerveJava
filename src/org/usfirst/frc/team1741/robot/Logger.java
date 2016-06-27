package org.usfirst.frc.team1741.robot;

import java.io.FileOutputStream;
import java.util.Vector;

public class Logger 
{
//	private String filename;
//	private FileOutputStream m_log;
//	private Vector< std::pair<String, String> > m_fields;
//
//	Logger()
//	{
//		
//	}
//	
//	boolean Open(String filename) 
//	{
//		this.filename = filename;
//		m_log = new FileOutputStream(filename, true);
//		return m_log.is_open() && m_log.good();
//	}
//
//	boolean Close() 
//	{
//		m_log.close();
//		return !m_log.is_open();
//	}
//
//	boolean Reset() 
//	{
//		Close();
//		Open(this.filename);
//		WriteAttributes();
//		return true;
//	}
//
//	boolean HasAttribute(String name) 
//	{
//		return (FindField(name) != -1);
//	}
//
//	// TODO: needs some serious optimization most likely
//	int FindField(String name) 
//	{
//		String real_name = Normalize(name);
//		for (unsigned int i = 0; i < m_fields.size(); ++i) {
//			if (real_name == m_fields[i].first) {
//				return i;
//			}
//		}
//		return -1;
//	}
//
//
//	boolean AddAttribute(String field) 
//	{
//		if (HasAttribute(field)) {
//			// TODO: Output warning
//			return false; // We already have this attribute
//		}
//
//		std::pair<std::string, std::string> item;
//		item.first = Normalize(field);
//		item.second = "";
//
//		m_fields.push_back(item);
//
//		return true;
//	}
//
//	boolean Log(String field, float data) {
//		std::ostringstream ostr;
//		ostr << data;
//		String str(ostr.str());
//		return Log(field, str);
//	}
//
//	boolean Log(String field, String data) {
//		String converted_field = Normalize(field);
//		int idx = FindField(converted_field);
//		if (idx < 0) return false;
//
//		m_fields[idx].second = data;
//		return true;
//	}
//
//	boolean WriteAttributes() {
//		for (unsigned int i = 0; i < m_fields.size(); ++i) {
//			m_log << m_fields[i].first << ',';
//		}
//		m_log << std::endl; // use \n if this is too slow
//		return m_log.good();
//	}
//
//	boolean WriteLine() {
//		for (unsigned int i = 0; i < m_fields.size(); ++i) {
//			m_log << m_fields[i].second << ',';
//		}
//		m_log << std::endl;
//		return m_log.good();
//	}
//
//	String Normalize(String str) 
//	{
//		return str.toLowerCase();
//	}
}
