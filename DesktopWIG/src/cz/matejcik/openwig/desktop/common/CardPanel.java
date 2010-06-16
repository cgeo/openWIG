package cz.matejcik.openwig.desktop.common;

import java.awt.CardLayout;
import javax.swing.JPanel;

/** Convenience class that integrates <code>JPanel</code> with
 * <code>CardLayout</code> for easier manipulation.
 * <p>
 * Because keeping two variables to accomplish one task is simply dumb.
 */
public class CardPanel extends JPanel {

	/** the internal <code>CardLayout</code> object */
	private CardLayout cards = new CardLayout();

	public CardPanel () {
		setLayout(cards);
	}

	/** Shows the specified card. */
	public void show (String key) {
		cards.show(this, key);
	}
}
