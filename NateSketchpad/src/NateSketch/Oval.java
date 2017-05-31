package NateSketch;

import java.awt.Color;
import java.awt.Graphics;

public class Oval implements Shape
{
	private int x;
	private int y;
	private int width;
	
	private Color color;
	
	public Oval(int x, int y, int width, Color color)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.color = color;
	}
	
	public void draw(Graphics g)
	{
		g.setColor(color);
		g.fillOval(x, y, width, width);
	}
}
