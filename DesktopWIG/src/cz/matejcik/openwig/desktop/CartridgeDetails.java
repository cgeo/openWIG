package cz.matejcik.openwig.desktop;

import cz.matejcik.openwig.ZonePoint;
import cz.matejcik.openwig.desktop.common.DetailPane;
import cz.matejcik.openwig.formats.CartridgeFile;
import java.io.IOException;
import javax.swing.JButton;

/** Detail pane customized to show information about CartridgeFiles.
 * <p>
 * Handles buttons for starting and resuming game, but the actual
 * code for these actions is located in {@link CartridgeList}
 * @see CartridgeList
 */
public class CartridgeDetails extends DetailPane {
	private JButton startButton = new JButton("Start");
	private JButton resumeButton = new JButton("Resume");

	private CartridgeList origin;

	public CartridgeDetails (CartridgeList origin) {
		this.origin = origin;
		addButton(startButton);
		addButton(resumeButton);
		resumeButton.setVisible(false);
	}

	@Override
	protected void buttonClicked (JButton b) {
		if (b == startButton) {
			origin.startSelected();
		} else if (b == resumeButton) {
			origin.resumeSelected();
		}
	}

	/** Updates displayed information to show details of <code>cf</code>
	 *
	 * @param cf the CartridgeFile to show
	 */
	public void showDetails (CartridgeFile cf) {
		setTitle(cf.name);
		setDescription(cf.description);
		setNavigationPoint(new ZonePoint(cf.latitude, cf.longitude, 0));
		try {
			setImage(cf.getFile(cf.splashId));
			resumeButton.setVisible(cf.getSavegame().exists());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
