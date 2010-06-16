package cz.matejcik.openwig.desktop.common;

import cz.matejcik.openwig.*;
import cz.matejcik.openwig.desktop.Main;
import cz.matejcik.openwig.desktop.gps.GPSManager;
import cz.matejcik.openwig.desktop.gps.Simulator;

import javax.swing.*;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;


/** Generic detail pane with image, text, buttons and navigation info.
 * <p>
 * Along with WigList, this is one of the basic UI elements of Wherigo.
 * It is a two-column pane - the left column shows associated image
 * and optionally navigation info, the right shows title, description
 * text and buttons.
 * <p>
 * Subclasses use this to display details about various kinds of objects,
 * or as UI for Dialogs and Inputs.
 */
public abstract class DetailPane extends JPanel implements ActionListener {

	/** default width of image media for Wherigo */
	private static int IMAGE_WIDTH = 230;

	/** Set name of displayed item */
	protected void setTitle (String title) {
		this.title.setText(title);
	}

	/** Set main text for the details. */
	protected void setDescription (String description) {
		this.description.setText(Engine.removeHtml(description));
		this.description.setCaretPosition(0);
		title.scrollRectToVisible(new Rectangle(10, 10));
	}

	/** Set image from array of bytes */
	protected void setImage (byte[] imagedata) {
		if (imagedata == null) {
			image.setIcon(null);
		} else {
			ImageIcon i = new ImageIcon(imagedata);
			image.setIcon(i);
			image.setPreferredSize(new Dimension(i.getIconWidth(), i.getIconHeight()));
		}
	}

	/** Set image from a Media object.
	 *
	 * This function is mostly for convenience, because it
	 * silently handles the possible IOException when retrieving
	 * image data from GWC file.
	 * @param media the media object to be shown
	 */
	protected void setMedia (Media media) {
		try {
			if (media == null) image.setIcon(null);
			else setImage(Engine.mediaFile(media));
		} catch (IOException e) {

		}
	}

	/** Sets point as a target for navigation pane.
	 * If the point is not null, navigation pane is made visible
	 * and a compass display is set up.
	 * @param zp the point towards which the compass points
	 */
	protected void setNavigationPoint (ZonePoint zp) {
		target = zp;
		compass.setPoint(zp);
		navigationPane.setVisible(zp != null);
	}

	/** Adds a button into the right pane.
	 * Makes sure that the button is center-aligned, has unlimited
	 * maximum width, and sets its <code>ActionListener</code> to <code>this</code>
	 * @param button the button to install
	 */
	protected void addButton (JButton button) {
		button.setAlignmentX(CENTER_ALIGNMENT);
		button.addActionListener(this);
		SwingHelpers.setMaximumWidth(button, Short.MAX_VALUE);
		rightPanel.add(button);
	}

	protected JLabel image;
	protected JLabel title;
	protected JTextArea description;

	protected FixedWidthPanel rightPanel;

	/** initializes image, title and description components */
	private void prepareComponents () {
		image = new JLabel();
		image.setAlignmentY(TOP_ALIGNMENT);

		title = new JLabel();
		title.setAlignmentX(CENTER_ALIGNMENT);
		//title.setAlignmentY(TOP_ALIGNMENT);

		description = new JTextArea();
		description.setEditable(false);
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		description.setAlignmentX(CENTER_ALIGNMENT);
	}

	protected JLabel distance, arrow;
	protected JPanel navigationPane;
	protected ZonePointCompass compass;
	protected ZonePoint target;

	/** this button is displayed when Simulator GPS is selected.
	 * When clicked, it sends the Simulator GPS towards the currently
	 * selected ZonePoint.
	 */
	protected JButton simulateWalk = new JButton(">");

	/** makes the navigation pane and leaves it hidden by default */
	private Component makeNavigationPane () {
		navigationPane = new JPanel(new BorderLayout());
		navigationPane.setBorder(BorderFactory.createEtchedBorder());

		distance = new JLabel("Distance");
		distance.setHorizontalAlignment(JLabel.CENTER);
		arrow = new JLabel();
		arrow.setHorizontalAlignment(JLabel.CENTER);
		arrow.setVerticalAlignment(JLabel.CENTER);
		compass = new ZonePointCompass();
		arrow.setIcon(compass);

		navigationPane.add(arrow, BorderLayout.CENTER);
		navigationPane.add(distance, BorderLayout.SOUTH);

		simulateWalk.setVisible(false);
		simulateWalk.addActionListener(this);
		navigationPane.add(simulateWalk, BorderLayout.EAST);

		navigationPane.setVisible(false);

		return navigationPane;
	}

	/** create left pane */
	private Component makeLeftPart () {
		JPanel p = new JPanel(new BorderLayout());
		SwingHelpers.setPreferredWidth(p, IMAGE_WIDTH);
		SwingHelpers.setMaximumWidth(p, IMAGE_WIDTH);
		p.add(image, BorderLayout.CENTER);

		p.add(makeNavigationPane(), BorderLayout.SOUTH);

		return p;
	}

	/** create right pane */
	private Component makeRightPart () {
		FixedWidthPanel p = new FixedWidthPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		p.add(title);
		p.add(description);
		p.add(Box.createRigidArea(new Dimension(10, 10)));
		rightPanel = p;

		//return p;
		JScrollPane rsp = new JScrollPane(p);
		rsp.setBorder(null);
		rsp.setPreferredSize(Main.ITEM_DIMENSION);
		SwingHelpers.setMaximumWidth(rsp, 500);
		return rsp;
	}

	/** Create a new empty DetailPane */
	public DetailPane () {
		prepareComponents();
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		add(makeLeftPart());
		add(Box.createRigidArea(new Dimension(5, 5)));
		add(makeRightPart());
	}

	/** If the clicked button was the simulator button, calls {@link #simulatorClicked()},
	 * otherwise calls {@link #buttonClicked(button)} for concrete implementations to handle.
	 */
	public void actionPerformed (ActionEvent e) {
		JButton b = (JButton)e.getSource();
		if (b == simulateWalk) {
			simulatorClicked();
		} else {
			buttonClicked(b);
		}
	}

	/** Handles button clicks. */
	abstract protected void buttonClicked (JButton button);

	boolean simulation = false;
	/** Updates distance label, forces repaint of the compass
	 * and shows/hides the Simulator button as needed.
	 */
	public void updateNavigation () {
		if (target == null) return;
		distance.setText(Navigator.getDistanceOnly(target));
		arrow.repaint();

		boolean sim = GPSManager.getGPS() instanceof Simulator;
		if (sim != simulation) {
			simulation = sim;
			simulateWalk.setVisible(sim);
			navigationPane.validate();
		}
	}

	/** Sends the Simulator GPS to the current ZonePoint */
	protected void simulatorClicked () {
		Simulator.walkTo(target);
		updateNavigation();
	}
}
