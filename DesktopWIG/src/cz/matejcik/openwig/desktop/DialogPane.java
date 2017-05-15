package cz.matejcik.openwig.desktop;

import cz.matejcik.openwig.DialogObject;
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

	private DialogObject dialogObject;

	private DialogWindow parent;

	public DialogPane (DialogWindow parent) {
		this.parent = parent;
		addButton(button1);
		addButton(button2);
		title.setVisible(false);
	}

	public void showDialog(DialogObject dobj, String label1, String label2) {
		dialogObject = dobj;
		button1.setText(label1);
		button2.setText(label2);
		button2.setVisible(label2 != null);
		showPage();
	}

	private void showPage() {
		setDescription(dialogObject.text);
		setMedia(dialogObject.media);
	}

	@Override
	protected void buttonClicked (JButton button) {
		if (button == button1) callAndClose("Button1");
		else if (button == button2) callAndClose("Button2");
	}
	
	private void callAndClose (String what) {
		dialogObject.doCallback(what);
		parent.close();
	}

	/** Called by parent window to indicate that the Dialog was cancelled.
	 * If the callback is present, it must be called with null parameter.
	 */
	public void cancel () {
		dialogObject.doCallback(null);
	}
}