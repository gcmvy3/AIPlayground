
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

	static ButtonGroup entityTypeButtonGroup = new ButtonGroup();
	
	static JRadioButton foodButton;
	static JRadioButton droneButton;
	static JRadioButton queenButton;
	
	static JMenuItem resetButton;

	static Queen currentQueen;
	
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
		
		// Button for creating a queen and thus a new faction
		queenButton = new JRadioButton("Queen");
		queenButton.addActionListener(entityTypeListener);
		entityTypeButtonGroup.add(queenButton);
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
		createMenu.add(queenButton);
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
		optionsMenu.add(hiveMindCheckBox);
		optionsMenu.addSeparator();
		optionsMenu.add(resetButton);
	}

	public void updateFactions() // Updates the faction list to reflect the current factions
	{
		currentQueen = GamePanel.getQueens().get(0);
		
		factionMenu.removeAll();
		initFactionButtons();
		
		for(JRadioButton button : factionButtons)
		{
			factionMenu.add(button);
		}

		if(factionButtons.size() > 0)
		{
			factionButtons.get(0).setSelected(true);
		}
		
		factionMenu.updateUI();
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

				currentQueen = GamePanel.getQueens().get(0);
				
				for(Queen q : GamePanel.getQueens())
				{
					if(q.getName().equals(sourceButton.getText()))
					{
						currentQueen = q;
						break;
					}
				}
			}
		};
		
		factionButtons = new ArrayList<JRadioButton>();
		factionButtonGroup = new ButtonGroup();

		for(Queen q : GamePanel.getQueens())
		{
			JRadioButton factionButton = new JRadioButton(q.getName());
			factionButton.addActionListener(factionListener);
			factionButtons.add(factionButton);
			factionButtonGroup.add(factionButton);
		}
	}
}
