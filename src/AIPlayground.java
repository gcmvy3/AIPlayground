
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

/*
 * This is your runner class. Your main() method is located here.
 */

public class AIPlayground
{
	private static BorderLayout mainLayout = new BorderLayout();
	
	private static JFrame mainWindow = new JFrame("Template");

	private static GamePanel gamePanel;
	
	private static int windowWidth = 1300;
	private static int windowHeight = 690;
	
	public static void main(String[] args) 
	{	
		initializeWindow();
		
		addGamePanel();
		
		startGame();
	}
	
	public static void initializeWindow()	//Initializes a JFrame
	{
		mainWindow.setSize(windowWidth, windowHeight);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setBackground(Color.WHITE);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setLayout(mainLayout);
		
		mainWindow.setJMenuBar(new TopMenu());
		
		mainWindow.setVisible(true);
	}
	
	public static void addGamePanel()	//Creates a new GamePanel
	{
		gamePanel = new GamePanel(windowWidth, windowHeight);

		mainWindow.add(gamePanel, BorderLayout.CENTER);
		mainWindow.pack();
	}
	
	public static void startGame()	//Starts the game
	{
		gamePanel.run();
	}
}
