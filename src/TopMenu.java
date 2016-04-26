
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

	static ArrayList<JRadioButton> factionButtons = new ArrayList<JRadioButton>();
	static ButtonGroup factionButtonGroup = new ButtonGroup();

	static JCheckBox hiveMindCheckBox;
	static JCheckBox pauseCheckBox;
	static JCheckBox foodCheckBox;
	static JCheckBox greenFactionCheckBox;

	static ButtonGroup entityTypeButtonGroup = new ButtonGroup();
	
	static JRadioButton droneButton;
	static JRadioButton foodButton;
	
	static JMenuItem resetButton;

	static Faction currentFaction;
	
	static String entityType = "Food";

	public TopMenu()
	{
		initButtons();
		initMenus();
	}

	public void initButtons()
	{
		initCreateButtons();
		initFactionButtons();
		initOptionsButtons();
	}

	public void initCreateButtons()
	{
		ActionListener entityTypeListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Object source = e.getSource();
				JRadioButton sourceButton = (JRadioButton)source;
				entityType = sourceButton.getText();
			}
		};
		
		// Button for creating food
		foodButton = new JRadioButton("Food");
		foodButton.addActionListener(entityTypeListener);
		foodButton.setSelected(true);
		entityTypeButtonGroup.add(foodButton);

		// Button for creating a drone
		droneButton = new JRadioButton("Drone");
		droneButton.addActionListener(entityTypeListener);
		entityTypeButtonGroup.add(droneButton);
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
	private void initCreateMenu()
	{
		createMenu = new JMenu("Create");
		createMenu.add(foodButton);
		createMenu.add(droneButton);
		createMenu.addSeparator();
		
		initFactionMenu();

		createMenu.add(factionMenu);
	}

	// Creates a submenu for selecting a faction
	private void initFactionMenu()
	{
		factionMenu = new JMenu("Faction");

		for(JRadioButton button : factionButtons)
		{
			factionMenu.add(button);
		}

		if(factionButtons.size() > 0)
		{
			factionButtons.get(0).setSelected(true);
		}
	}	

	// Creates a new menu and adds the option buttons to it
	private void initOptionsMenu()
	{	
		optionsMenu = new JMenu("Options");	

		optionsMenu.add(pauseCheckBox);
		optionsMenu.add(foodCheckBox);
		optionsMenu.add(greenFactionCheckBox);
		optionsMenu.add(hiveMindCheckBox);
		optionsMenu.addSeparator();
		optionsMenu.add(resetButton);
	}
	
	public void update() // Updates the menu to reflect the current state of the GamePanel
	{
		currentFaction = GamePanel.getFactions().get(0);
		
		this.removeAll();
		this.initFactionButtons();
		this.initMenus();
	}
	
	private void initFactionButtons() // Updates the list of faction buttons to reflect the current factions
	{
		// Changes the faction to that of the radio button clicked
		ActionListener factionListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Object source = e.getSource();
				JRadioButton sourceButton = (JRadioButton)source;

				currentFaction = GamePanel.getFactions().get(0);
				
				for(Faction f : GamePanel.getFactions())
				{
					if(f.getName().equals(sourceButton.getText()))
					{
						currentFaction = f;
						break;
					}
				}
			}
		};
		
		factionButtons = new ArrayList<JRadioButton>();
		factionButtonGroup = new ButtonGroup();

		for(Faction f : GamePanel.getFactions())
		{
			JRadioButton factionButton = new JRadioButton(f.getName());
			factionButton.addActionListener(factionListener);
			factionButtons.add(factionButton);
			factionButtonGroup.add(factionButton);
		}
	}
}
