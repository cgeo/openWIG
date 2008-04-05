package gui;

import javax.microedition.lcdui.*;
import openwig.ZonePoint;
import openwig.Engine;

public class Coordinates extends Form implements CommandListener, Pushable {
	
	private TextField latitude = new TextField("latitude", "47.6666", 99, TextField.DECIMAL);
	private TextField longitude = new TextField("longitude", "-122.351", 99, TextField.DECIMAL);
	
	private static Command CMD_SET = new Command("Set", Command.SCREEN, 0);
	
	public Coordinates () {
		super("coordinates");
		append(latitude);
		append(longitude);
		setCommandListener(this);
		addCommand(CMD_SET);
		addCommand(Midlet.CMD_BACK);
	}

	public void commandAction(Command cmd, Displayable disp) {
		switch (cmd.getCommandType()) {
			case Command.SCREEN:
				ZonePoint z = new ZonePoint(Double.parseDouble(latitude.getString()), Double.parseDouble(longitude.getString()), 0);
				Engine.newPosition(z);
				break;
			case Command.BACK:
				Midlet.pop();
				break;
			default:
				break;
		}
	}

	public void prepare() {
		/*latitude.setString(Double.toString(Engine.instance.player.position.latitude));
		longitude.setString(Double.toString(Engine.instance.player.position.longitude));*/
	}

}
