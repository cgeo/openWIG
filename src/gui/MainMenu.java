package gui;

import javax.microedition.lcdui.*;
import openwig.Engine;
import openwig.Zone;

public class MainMenu extends List implements CommandListener, Pushable {
	
	private static final int ZONES = 0;
	private static final int INVENTORY = 1;
	private static final int LOCATION = 2;
	private static final int TASKS = 3;
	
	private static Command CMD_GPS = new Command("Poloha", Command.SCREEN, 5);
	
	public MainMenu () {
		super("menu", IMPLICIT);
		addCommand(CMD_GPS);
		addCommand(Midlet.CMD_EXIT);
		setSelectCommand(Midlet.CMD_SELECT);
		setCommandListener(this);
	}
	
	public void commandAction(Command cmd, Displayable disp) {
		switch (cmd.getCommandType()) {
			case Command.ITEM:
				switch (getSelectedIndex()) {
					case ZONES:
						Midlet.push(Midlet.zones);
						break;
/*					case COORDS:
						Midlet.push(Midlet.coordinates);
						break;*/
					case INVENTORY:
						Midlet.push(Midlet.inventory);
						break;
					case LOCATION:
						Zone z = Engine.instance.cartridge.currentZone;
						if (z != null) {
							Midlet.push(new Things("Okolí", z));
						} else {
							Midlet.display.setCurrent(new Alert("Okolí", "nic tu není", null, AlertType.INFO));
						}
						break;
					default:
						Midlet.display.setCurrent(new Alert("chyba","nefunguje",null,AlertType.ERROR), disp);
				}
				break;
			case Command.SCREEN:
				if (cmd == CMD_GPS) {
					Midlet.push(Midlet.coordinates);
				}
				break;
			case Command.EXIT:
				Midlet.instance.destroyApp(false);
				break;
		}
	}

	public void prepare() {
		int i = getSelectedIndex();
		deleteAll();
		int zones = Engine.instance.cartridge.visibleZones();
		int items = Engine.instance.player.visibleThings();
		int things = Engine.instance.cartridge.visibleThings();
		append("Zóny ("+zones+")", null);
		append("Inventáø ("+items+")", null);
		append("Okolí ("+things+")", null);
		append("Úkoly", null);
		if (i >= 0 && i < 4) setSelectedIndex(i, true);
	}
	
}
