package cz.matejcik.openwig.desktop;

import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Media;
import cz.matejcik.openwig.desktop.common.DetailPane;
import javax.swing.JButton;
import se.krka.kahlua.vm.LuaClosure;

/** GUI for displaying Dialogs
 * <p>
 * Uses <code>DetailPane</code>'s layout to show Wherigo dialogs.
 * @see cz.matejcik.openwig.platform.UI#pushDialog(java.lang.String[], cz.matejcik.openwig.Media[], java.lang.String, java.lang.String, se.krka.kahlua.vm.LuaClosure)
 */
public class DialogPane extends DetailPane {
	
	private JButton button1 = new JButton("Button1");
	private JButton button2 = new JButton("Button2");

	/** current page of dialog */
	private int page;
	/** texts of individual pages */
	private String[] texts;
	/** pictures for individual pages */
	private Media[] media;

	/** optional callback after the dialog is closed */
	private LuaClosure callback;

	private DialogWindow parent;

	public DialogPane (DialogWindow parent) {
		this.parent = parent;
		addButton(button1);
		addButton(button2);
		title.setVisible(false);
	}

	/** Shows a Dialog.
	 * <p>
	 * See description of <code>pushDialog</code> for explanation of the call.
	 * @see cz.matejcik.openwig.platform.UI#pushDialog(java.lang.String[], cz.matejcik.openwig.Media[], java.lang.String, java.lang.String, se.krka.kahlua.vm.LuaClosure)
	 */
	public void showDialog (String[] texts, Media[] media, String btn1, String btn2, LuaClosure callback) {
		this.callback = callback;
		this.texts = texts;
		this.media = media;
		button1.setText(btn1 == null ? "OK" : btn1);
		button2.setText(btn2);
		button2.setVisible(btn2 != null);
		page = -1;
		flipPage();
	}

	private void flipPage () {
		page++;
		if (page >= texts.length) {
			callAndClose("Button1");
		} else {
			setDescription(texts[page]);
			setMedia(media[page]);
		}
	}

	@Override
	protected void buttonClicked (JButton button) {
		if (button == button1) flipPage();
		else if (button == button2) callAndClose("Button2");
	}
	
	private void callAndClose (String what) {
		if (callback != null) Engine.invokeCallback(callback, what);
		parent.close();
	}

	/** Called by parent window to indicate that the Dialog was cancelled.
	 * If the callback is present, it must be called with null parameter.
	 */
	public void cancel () {
		if (callback != null) Engine.invokeCallback(callback, null);
	}
}
