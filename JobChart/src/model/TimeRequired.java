package model;

import java.io.Serializable;

public enum TimeRequired implements Serializable
{	
	SHORT("1 - 5 minutes"),
	MEDIUM("5 - 10 minutes"),
	LONG("10 - 20 minutes");
	
	private String _string;
	
	private TimeRequired(String s)
	{
		_string = s;
	}
	
	@Override
	public String toString()
	{
		return _string;
	}
}
