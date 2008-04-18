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
		
		reallyexit.addCommand(Midlet.CMD_SELECT);
		reallyexit.addCommand(Midlet.CMD_CANCEL);
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
							Vector v = Engine.instance.cartridge.currentThings();
							if (!v.isEmpty()) {
								Midlet.push(new Things("Okolí", v));
							} else {
								Midlet.display.setCurrent(new Alert("Okolí", "nic tu není", null, AlertType.INFO));
							}
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
			if (cmd == Midlet.CMD_SELECT) {
				Midlet.instance.destroyApp(false);
			} else if (cmd == Midlet.CMD_CANCEL) {
				Midlet.display.setCurrent(this);
			}
		}
	}

	public void prepare() {
		int i = getSelectedIndex();
		deleteAll();
		int zones = Engine.instance.cartridge.visibleZones();
		int items = Engine.instance.player.visibleThings();
		int things = Engine.instance.cartridge.visibleThings();
		int tasks = Engine.instance.cartridge.visibleTasks();
		append("Zóny ("+zones+")", null);
		append("Inventáø ("+items+")", null);
		append("Okolí ("+things+")", null);
		append("Úkoly ("+tasks+")", null);
		if (i >= 0 && i < 4) setSelectedIndex(i, true);
	}
	
}
