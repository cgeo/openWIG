package gui;

import java.util.Vector;
import javax.microedition.lcdui.*;
import openwig.Engine;
import openwig.Zone;

public class MainMenu extends List implements CommandListener, Pushable {
	
	private static final int ZONES = 0;
	private static final int INVENTORY = 1;
	private static final int LOCATION = 2;
	private static final int TASKS = 3;
	
	private static Command CMD_GPS = new Command("Poloha", Command.SCREEN, 5);
	
	private static Alert reallyexit = new Alert("dotaz","opravdu ukonèit?",null,AlertType.CONFIRMATION);
	
	public MainMenu () {
		super("menu", IMPLICIT);
		addCommand(CMD_GPS);
		addCommand(Midlet.CMD_EXIT);
		setSelectCommand(Midlet.CMD_SELECT);
		setCommandListener(this);
		
		append("zones", null);
		append("inventory", null);
		append("you see", null);
		append("tasks", null);
		
		reallyexit.addCommand(new Command("Ano", Command.OK, 1));
		reallyexit.addCommand(new Command("Ne", Command.CANCEL, 2));
		reallyexit.setCommandListener(this);
		reallyexit.setTimeout(Alert.FOREVER);
	}
	
	public void commandAction(Command cmd, Displayable disp) {
		if (disp == this) {
			switch (cmd.getCommandType()) {
				case Command.ITEM:
					switch (getSelectedIndex()) {
						case ZONES:
							Midlet.push(Midlet.zones);
							break;
						case INVENTORY:
							Midlet.push(Midlet.inventory);
							break;
						case LOCATION:
							Midlet.push(Midlet.surroundings);
							break;
						case TASKS:
							Midlet.push(Midlet.tasks);
					}
					break;
				case Command.SCREEN:
					if (cmd == CMD_GPS) {
						Midlet.push(Midlet.coordinates);
					}
					break;
				case Command.EXIT:
					//Midlet.instance.destroyApp(false);
					Midlet.display.setCurrent(reallyexit,this);
					break;
			}
		} else if (disp == reallyexit) {
			if (cmd.getCommandType() == Command.OK) {
				Midlet.instance.destroyApp(false);
			} else if (cmd.getCommandType() == Command.CANCEL) {
				Midlet.display.setCurrent(this);
			}
		}
	}

	public void prepare() {
		int zones = Engine.instance.cartridge.visibleZones();
		int items = Engine.instance.player.visibleThings();
		int things = Engine.instance.cartridge.visibleThings();
		int tasks = Engine.instance.cartridge.visibleTasks();
		set(ZONES, "Zóny ("+zones+")", null);
		set(INVENTORY, "Inventáø ("+items+")", null);
		set(LOCATION, "Okolí ("+things+")", null);
		set(TASKS, "Úkoly ("+tasks+")", null);
	}
	
}
