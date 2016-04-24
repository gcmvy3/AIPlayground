
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;

// TopMenu is a custom JMenuBar that appears at the top of the screen
// TODO: Clean and comment this class

public class TopMenu extends JMenuBar
{
	private static final long serialVersionUID = 1L;

	static JMenu createMenu, optionsMenu;
	static JMenu factionMenu;

	static ButtonGroup factionButtons = new ButtonGroup();

	static JCheckBox hiveMindCheckBox;
	static JCheckBox pauseCheckBox;
	static JCheckBox foodCheckBox;
	static JCheckBox greenFactionCheckBox;
	static JRadioButton redButton;
	static JRadioButton blueButton;
	
	static JMenuItem droneButton;
	static JMenuItem foodButton;
	static JMenuItem resetButton;
	
	
	public TopMenu()
	{
		initButtons();
		initMenus();
	}
	
	public void initButtons()
	{
		initCreateButtons();
		initOptionsButtons();
	}
	
	public void initCreateButtons()
	{
		// Button for creating food
		foodButton = new JMenuItem("Food");
		foodButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			}
		});		
		
		// Button for creating a drone
		droneButton = new JMenuItem("Drone");
		droneButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			}
		});		
		
		// A radio button to choose a faction
		ActionListener factionListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(e.getSource().equals(redButton))
				{
					//TODO: Wire this
					//Faction = red
				}
				else if(e.getSource().equals(blueButton))
				{
					//Faction = blue
				}
			}
		};

		redButton  = new JRadioButton("Red");
		blueButton  = new JRadioButton("Blue");
		redButton.addActionListener(factionListener);
		blueButton.addActionListener(factionListener);
	}
	
	public void initOptionsButtons()
	{		
		// Toggles whether or not to spawn food randomly
		foodCheckBox = new JCheckBox("Spawn Food");
		foodCheckBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				GamePanel.toggleFoodSpawn();
			}
		});
		
		// Toggles a third, green faction in the middle
		greenFactionCheckBox = new JCheckBox("Green Faction");
		greenFactionCheckBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				GamePanel.toggleGreenFaction();
			}
		});
		
		// TBD
		hiveMindCheckBox = new JCheckBox("Hive Mind");
		hiveMindCheckBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// TODO Wire this
				//GamePanel.toggleHiveMind();
			}
		});
		
		// Pauses and unpauses the game
		pauseCheckBox = new JCheckBox("Pause");
		pauseCheckBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				GamePanel.togglePause();
			}
		});
		
		// Removes all entities and starts over
		resetButton = new JMenuItem("Reset");
		resetButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				GamePanel.resetWorld();
			}
		});
	}
	
	// Initializes the submenus
	public void initMenus()
	{
		initCreateMenu();
		initOptionsMenu();
		
		this.add(createMenu);
		this.add(optionsMenu);
	}
	
	// Creates a submenu for adding things to the map
	public void initCreateMenu()
	{
		createMenu = new JMenu("Create");
		createMenu.add(foodButton);
		createMenu.addSeparator();
		createMenu.add(droneButton);
		
		initFactionMenu();
		
		createMenu.add(factionMenu);
	}

	// Creates a submenu for selecting a faction
	public void initFactionMenu()
	{
		factionMenu = new JMenu("Faction");

		factionButtons.add(redButton);
		factionButtons.add(blueButton);

		redButton.setSelected(true);

		factionMenu.add(redButton);
		factionMenu.add(blueButton);
	}	
	
	// Creates a new menu and adds the option buttons to it
	public void initOptionsMenu()
	{	
		optionsMenu = new JMenu("Options");	
		
		optionsMenu.add(pauseCheckBox);
		optionsMenu.add(foodCheckBox);
		optionsMenu.add(greenFactionCheckBox);
		optionsMenu.add(hiveMindCheckBox);
		optionsMenu.addSeparator();
		optionsMenu.add(resetButton);
	}
}
