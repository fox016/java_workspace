package NateSketch;

import java.awt.Color;

public interface Command
{
	public void run();
	public void setColor(Color color);
}

class BackgroundChanger implements Command
{
	private NateSketchpad panel;
	private Color color;
	
	public BackgroundChanger(NateSketchpad panel)
	{
		this.panel = panel;
		this.color = null;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}
	
	public void run()
	{
		if(color == null)
			throw new IllegalStateException("No color defined");
		panel.setBackground(color);
	}
}

class AxisChanger implements Command
{
	private NateSketchpad pad;
	private Color color;
	
	public AxisChanger(NateSketchpad pad)
	{
		this.pad = pad;
		this.color = null;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}
	
	public void run()
	{
		if(color == null)
			throw new IllegalStateException("No color defined");
		pad.setAxisColor(color);
		pad.repaint();
	}
}
