package minesweeper;

public class Cell
{
	private boolean isExposed;
	private boolean isFlagged;
	private boolean isQueried;
	
	private static final int BOMB = 9;
	private int value;
	
	public Cell()
	{
		isExposed = false;
		isFlagged = false;
		isQueried = false;
		value = 0;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public void setBomb()
	{
		value = BOMB;
	}
	
	public void incrementValue()
	{
		value++;
	}
	
	public boolean isExposed()
	{
		return isExposed;
	}
	
	public boolean isFlagged()
	{
		return isFlagged;
	}
	
	public boolean isQueried()
	{
		return isQueried;
	}
	
	public void expose()
	{
		isExposed = true;
		isFlagged = false;
		isQueried = false;
	}
	
	public void setFlagged(boolean flag)
	{
		if(isExposed)
		{
			isFlagged = false;
			return;
		}
		
		isFlagged = flag;
		if(isFlagged)
			isQueried = false;
	}
	
	public void setQueried(boolean queried)
	{
		if(isExposed)
		{
			isQueried = false;
			return;
		}
		
		isQueried = queried;
		if(isQueried)
			isFlagged = false;
	}
	
	@Override
	public String toString()
	{
		return "{value: " + value + ", flagged: " + isFlagged + ", exposed: " + isExposed + "}";
	}
}
