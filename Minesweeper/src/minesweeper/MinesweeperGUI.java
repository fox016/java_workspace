package minesweeper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class MinesweeperGUI extends JFrame implements MouseListener, ActionListener
{
	// GUI constants
	private static final int frameBorder = 50; // Pixel size of frame border
	private static final int edge = 25; // Pixels on the edge of a square
	private static int scoreHeight = 48; // Pixels at top used for scores
	private static int faceSize = 32; // Pixel size of smiley face

	// Model Elements
	private Board board;
	
	// Menu Elements
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem newGameMenu;
	private JMenuItem hintMenu;
	private JMenuItem exitMenu;

	// Image Elements
	private Image[] exposedRect;
	private Image incorrectRect;
	private Image explodedRect;
	private Image unexposedRect;
	private Image flaggedRect;
	private Image queriedRect;
	private Image boredSmiley;
	private Image happySmiley;
	private Image sadSmiley;

	// Colors
	private Color[] colors; // Color for exposed counts
	private Color baseColor; // Color for unexposed squares
	private Color baseShadow; // Slightly darker than baseColor
	private Color mineColor; // Color for mine itself
	private Color numberColor; // Color for score digits
	private Color dangerousColor; // Color for flag, explode, incorrect

	// Fonts
	private Font mainFont;
	private Font scoreFont;
	private int numberSize;
	private int numberMargin;

	// Time
	private Thread timer;
	private long startTime;

	/**
	 * Constructor
	 * 
	 * @param board
	 */
	public MinesweeperGUI(Board board)
	{
		this.board = board;

		initMenu();
		initFrame();
		initColors();
		initFonts();
		initImages();

		startTime = -1;
		timer = null;

		this.addMouseListener(this);
		this.repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g)
	{
		g.setColor(baseColor);
		g.fill3DRect(0, frameBorder, board.getCols() * edge, scoreHeight, true);
		paintRemainingBombCount(g);
		paintFace(g);
		paintTime(g);
		for (int r = 0; r < board.getRows(); r++)
		{
			for (int c = 0; c < board.getCols(); c++)
			{
				paintCell(g, r, c);
			}
		}
		menuBar.repaint();

		// Repaint every second (so timer updates)
		if(timer == null)
		{
			timer = new Thread()
			{
				@Override
				public void run()
				{
					while (!board.isGameOver())
					{
						try
						{
							sleep(1000);
						} catch (InterruptedException e)
						{
							return;
						}
						repaint();
					}
				}
			};
			timer.start();
		}
	}

	/**
	 * Paints the Minsweeper Cell at position (row, col)
	 * 
	 * @param g
	 * @param row
	 * @param col
	 */
	private void paintCell(Graphics g, int row, int col)
	{
		Image image;
		Cell cell = board.getCell(row, col);

		if(cell.isFlagged() && board.isGameOver() && cell.getValue() > 8)
			image = flaggedRect;
		else if(cell.isFlagged() && !board.isGameOver())
			image = flaggedRect;
		else if(!cell.isFlagged() && board.isGameOver() && cell.getValue() > 8
				&& board.isVictory())
			image = flaggedRect;
		else if(cell.isQueried() && !board.isGameOver())
			image = queriedRect;
		else if(!cell.isFlagged() && !cell.isQueried() && !cell.isExposed()
				&& !board.isGameOver())
			image = unexposedRect;
		else if(!cell.isFlagged() && board.isGameOver() && cell.getValue() > 8
				&& !board.isVictory())
			image = explodedRect;
		else if(cell.isFlagged() && board.isGameOver() && cell.getValue() <= 8)
			image = incorrectRect;
		else
			image = exposedRect[cell.getValue()];

		g.drawImage(image, col * edge,
				(row * edge) + scoreHeight + frameBorder, this);
	}

	/**
	 * Paints time on the clock
	 * 
	 * @param g
	 */
	private void paintTime(Graphics g)
	{
		long elapsedTime = 0;
		if(startTime != -1)
			elapsedTime = System.currentTimeMillis() - startTime;
		elapsedTime /= 1000;

		paintNumber(
				g,
				elapsedTime < 0 ? 0L : elapsedTime,
				(board.getCols() * edge)
						- ((board.getCols() * edge) - faceSize - (numberSize * 2))
						/ 4);
	}

	/**
	 * Paints a number n, used for clock and for remaining bomb count
	 * 
	 * @param g
	 * @param n
	 * @param rightOffset
	 */
	private void paintNumber(Graphics g, long n, int rightOffset)
	{
		String s = (n > 999 || n < -99 ? "---" : n + "");

		FontMetrics fm = this.getFontMetrics(scoreFont);
		int topOffset = (scoreHeight - fm.getHeight()) / 2;

		g.setColor(Color.black);
		g.fillRect(rightOffset - numberSize, topOffset - numberMargin
				+ frameBorder, numberSize, fm.getHeight() + (numberMargin * 2));

		g.setColor(numberColor);
		g.setFont(scoreFont);
		g.drawString(s, rightOffset - fm.stringWidth(s) - numberMargin,
				topOffset + fm.getAscent() + frameBorder);
	}

	/**
	 * Paints face
	 * 
	 * @param g
	 */
	private void paintFace(Graphics g)
	{
		Image smiley;
		if(!board.isGameOver())
			smiley = boredSmiley;
		else if(board.isVictory())
			smiley = happySmiley;
		else
			smiley = sadSmiley;

		g.drawImage(smiley, ((board.getCols() * edge) - faceSize) / 2,
				((scoreHeight - faceSize) / 2) + frameBorder, this);
	}

	/**
	 * Calculates and paints remaining bomb count
	 * 
	 * @param g
	 */
	private void paintRemainingBombCount(Graphics g)
	{
		int count = 0;
		if(!board.isGameOver())
		{
			count = board.getBombTotal() - board.getFlagTotal();
		}
		paintNumber(g, count, numberSize
				+ ((board.getCols() * edge) - faceSize - (numberSize * 2)) / 4);
	}
	
	/**
	 * Initializes global menu items
	 */
	private void initMenu()
	{
		menuBar = new JMenuBar();
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menu);
		
		newGameMenu = new JMenuItem("New game", KeyEvent.VK_N);
		newGameMenu.addActionListener(this);
		menu.add(newGameMenu);
		
		hintMenu = new JMenuItem("Hint", KeyEvent.VK_H);
		hintMenu.addActionListener(this);
		menu.add(hintMenu);
		
		exitMenu = new JMenuItem("Quit", KeyEvent.VK_Q);
		exitMenu.addActionListener(this);
		menu.add(exitMenu);
	}

	/**
	 * Initializes JFrame
	 */
	private void initFrame()
	{
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = board.getCols() * edge;
		int height = board.getRows() * edge + frameBorder + scoreHeight;
		
		this.setTitle("Minesweeper");
		this.setBackground(baseColor);
		this.setJMenuBar(menuBar);
		this.setSize(width, height);
		this.setLocation(dim.width/2-width/2, dim.height/2-height/2);
		this.setDefaultCloseOperation(MinesweeperGUI.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}

	/**
	 * Initializes global colors
	 */
	private void initColors()
	{
		baseColor = new Color(204, 204, 204);
		baseShadow = new Color(153, 153, 153);
		mineColor = new Color(51, 51, 51);
		numberColor = new Color(255, 102, 102);
		dangerousColor = new Color(255, 51, 51);

		colors = new Color[9];
		colors[0] = Color.black; // Black
		colors[1] = new Color(51, 51, 204); // Blue
		colors[2] = new Color(0, 102, 0); // Green
		colors[3] = new Color(204, 0, 0); // Red
		colors[4] = new Color(102, 0, 102); // Purple
		colors[5] = new Color(0, 102, 102); // Dark cyan
		colors[6] = Color.black;
		colors[7] = Color.black;
		colors[8] = Color.black;
	}

	/**
	 * Initializes global fonts
	 */
	private void initFonts()
	{
		numberMargin = 2;
		mainFont = new Font("TimesRoman", Font.BOLD, ((edge * 5) / 8)
				+ numberMargin);
		scoreFont = new Font("TimesRoman", Font.BOLD, (edge * 5) / 4);
		numberSize = this.getFontMetrics(scoreFont).stringWidth("000")
				+ (numberMargin * 2);
	}

	/**
	 * Initializes global images
	 */
	private void initImages()
	{
		explodedRect = buildImage(ImageType.EXPLODED);
		incorrectRect = buildImage(ImageType.INCORRECT);
		unexposedRect = buildImage(ImageType.UNEXPOSED);
		flaggedRect = buildImage(ImageType.FLAGGED);
		queriedRect = buildImage(ImageType.QUERIED);

		exposedRect = new Image[9];
		for (int i = 0; i < 9; i++)
			exposedRect[i] = buildExposedImage(i);

		happySmiley = buildSmiley(SmileType.HAPPY);
		boredSmiley = buildSmiley(SmileType.BORED);
		sadSmiley = buildSmiley(SmileType.SAD);
	}

	/**
	 * Returns an Image of given type
	 * 
	 * @param type
	 * @return
	 */
	private Image buildImage(ImageType type)
	{
		Image image = this.createImage(edge, edge);
		Graphics g = image.getGraphics();

		g.setColor(type == ImageType.EXPLODED ? dangerousColor : baseColor);

		if(type == ImageType.EXPLODED || type == ImageType.INCORRECT)
		{
			g.fillRect(0, 0, edge, edge);
			g.setColor(baseShadow);
		} else
		{
			g.fill3DRect(0, 0, edge - 1, edge - 1, true);
			g.setColor(Color.black);
		}

		g.drawLine(edge - 1, 0, edge - 1, edge - 1);
		g.drawLine(0, edge - 1, edge - 1, edge - 1);

		int halfWidth = edge / 2;
		int quarterPos = (edge - 1) / 4;

		switch(type)
		{
			case MINE:
			case EXPLODED:
				g.setColor(mineColor);
				g.drawLine(2, 2, edge - 4, edge - 4);
				g.drawLine(edge - 4, 2, 2, edge - 4);
				g.drawLine(halfWidth - 1, 1, halfWidth - 1, edge - 3);
				g.drawLine(1, halfWidth - 1, edge - 3, halfWidth - 1);
				g.fillOval(quarterPos, quarterPos, halfWidth + 1, halfWidth + 1);
				g.setColor(Color.white);
				g.fillOval(halfWidth - 3, halfWidth - 3, edge / 8, edge / 8);
				break;
			case UNEXPOSED:
				break;
			case INCORRECT:
				g.setColor(dangerousColor);
				g.drawLine(2, 2, edge - 4, edge - 4);
				g.drawLine(2, 3, edge - 5, edge - 4);
				g.drawLine(3, 2, edge - 4, edge - 5);
				g.drawLine(edge - 4, 2, 2, edge - 4);
				g.drawLine(edge - 4, 3, 3, edge - 4);
				g.drawLine(edge - 5, 2, 2, edge - 5);
				break;
			case FLAGGED:
				g.setColor(dangerousColor);
				g.fillRect(halfWidth - quarterPos, halfWidth - quarterPos,
						halfWidth - quarterPos, halfWidth - quarterPos);
				g.setColor(mineColor);
				g.drawLine(halfWidth, 3, halfWidth, edge - 4);
				g.drawLine(5, edge - 4, edge - 5, edge - 4);
				break;
			case QUERIED:
				FontMetrics fm = this.getFontMetrics(mainFont);
				int fontAscent = fm.getAscent();
				g.setColor(new Color(0, 0, 255));
				g.setFont(mainFont);
				g.drawString("?", (edge - fm.stringWidth("?")) / 2, fontAscent);
				break;
		}

		return image;
	}

	/**
	 * Returns smiley Image of given type
	 * 
	 * @param type
	 * @return
	 */
	private Image buildSmiley(SmileType type)
	{
		Image image = this.createImage(faceSize, faceSize);
		Graphics g = image.getGraphics();

		g.setColor(Color.black);
		g.fillRect(0, 0, faceSize, faceSize);
		g.setColor(baseColor);
		g.fill3DRect(1, 1, faceSize - 2, faceSize - 2, true);
		g.fill3DRect(2, 2, faceSize - 4, faceSize - 4, true);
		g.setColor(Color.yellow);
		g.fillOval(6, 6, faceSize - 12, faceSize - 12);
		g.setColor(Color.black);
		g.drawOval(6, 6, faceSize - 12, faceSize - 12);

		switch(type)
		{
			case SAD:
				g.drawArc(10, faceSize - 13, faceSize - 20, faceSize - 20, 135,
						-100);
				break;
			case HAPPY:
				g.drawArc(10, 10, faceSize - 20, faceSize - 20, -35, -100);
				break;
			case BORED:
				g.fillRect(12, faceSize - 12, faceSize - 23, 1);
				break;
		}

		g.fillOval(13, 13, 2, 2);
		g.fillOval(faceSize - 12 - 2, 13, 2, 2);

		return image;
	}

	/**
	 * Returns an image showing neighbor bomb count 0 is a blank image, 1-8 show
	 * number
	 * 
	 * @param i
	 * @return
	 */
	private Image buildExposedImage(int i)
	{
		Image image = this.createImage(edge, edge);
		Graphics g = image.getGraphics();

		g.setColor(baseShadow);
		g.fill3DRect(0, 0, edge - 1, edge - 1, true);
		g.setColor(Color.black);
		g.drawLine(edge - 1, 0, edge - 1, edge - 1);
		g.drawLine(0, edge - 1, edge - 1, edge - 1);

		if(i != 0)
		{
			FontMetrics fm = this.getFontMetrics(mainFont);
			int fontAscent = fm.getAscent();
			String s = i + "";
			g.setColor(colors[i]);
			g.setFont(mainFont);
			g.drawString(s, (edge - fm.stringWidth(s)) / 2, fontAscent);
		}

		return image;
	}

	/**
	 * Resets game
	 */
	private void resetGame()
	{
		NewGameFrame ngf = new NewGameFrame();
		ngf.setFields(board.getRows(), board.getCols(), board.getBombTotal());
		this.dispose();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if(e.getClickCount() == 2)
		{
			// Calculate cell coordinates from mouse coordinates
			int col = e.getX() / edge;
			int row = (e.getY() - frameBorder - scoreHeight) / edge;
			
			// If not on game board, ignore
			if(col < 0 || col >= board.getCols() || row < 0 || row >= board.getRows())
				return;
			
			board.expandIfDone(row, col);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		// Reset game if face is clicked
		int face_x = (board.getCols() * edge - faceSize) /2;
		int face_y = ((scoreHeight - faceSize) / 2) + frameBorder;
		if(e.getY() > face_y && e.getY() < (face_y + faceSize) &&
				e.getX() > face_x && e.getX() < (face_x + faceSize))
		{
			resetGame();
			return;
		}

		// Ignore if anything (except for face) is clicked after game over
		if(board.isGameOver())
		{
			return;
		}

		// Calculate cell coordinates from mouse coordinates
		int col = e.getX() / edge;
		int row = (e.getY() - frameBorder - scoreHeight) / edge;
		
		// If not on game board, ignore
		if(col < 0 || col >= board.getCols() || row < 0 || row >= board.getRows())
			return;

		// Start timer if first time
		if(startTime == -1)
			startTime = System.currentTimeMillis();

		// Right click
		if(e.isMetaDown())
			board.rightClickCell(row, col);

		// Left click
		else
			board.clickCell(row, col);

		// Repaint
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == newGameMenu)
			resetGame();
		else if(e.getSource() == hintMenu)
			board.giveHint();
		else if(e.getSource() == exitMenu)
			System.exit(0);
	}
}
