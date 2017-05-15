package cz.matejcik.openwig.desktop;

import cz.matejcik.openwig.DialogObject;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.desktop.common.DetailPane;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JTextField;
import se.krka.kahlua.vm.KahluaTable;

/** GUI for displaying Inputs.
 * <p>
 * Uses <code>DetailPane</code>'s UI to show Wherigo Inputs.
 * @see cz.matejcik.openwig.platform.UI#pushInput(cz.matejcik.openwig.EventTable)
 */
public class InputPane extends DetailPane {

	/** button to confirm answer in text field */
	private JButton answer = new JButton("Answer");
	/** text field for Text inputs */
	private JTextField textInput = new JTextField();

	/** pool of buttons for multiple-choice inputs */
	private ArrayList<JButton> options = new ArrayList<JButton>();

	private DialogObject input;

	private DialogWindow parent;

	public InputPane (DialogWindow parent) {
		this.parent = parent;
		setTitle("Question");
		rightPanel.add(textInput);
		addButton(answer);
	}

	/** Shows an Input.
	 * <p>
	 * See description of <code>pushInput()</code> for explanation about inputs.
	 * @param input the input to show
	 * @see cz.matejcik.openwig.platform.UI#pushInput(cz.matejcik.openwig.EventTable)
	 */
	public void showInput (DialogObject dobj, String[] choices) {
		this.input = dobj;
		setDescription(input.text);
		setMedia(input.media);

		if (choices == null || choices.length == 0) {
			// hide buttons
			for (JButton button : options) button.setVisible(false);
			// show text/answer
			textInput.setVisible(true);
			textInput.setText("");
			answer.setVisible(true);
		} else {
			// hide text/answer
			textInput.setVisible(false);
			answer.setVisible(false);
			// make sure we have enough buttons
			for (int i = options.size(); i < choices.length; i++) {
				JButton jb = new JButton();
				options.add(jb);
				addButton(jb);
			}
			// set up choices
			for (int i = 0; i < choices.length; i++) {
				JButton jb = options.get(i);
				jb.setText(choices[i]);
				jb.setVisible(true);
			}
			// hide the rest
			for (int i = choices.length; i < options.size(); i++) {
				options.get(i).setVisible(false);
			}
		}
		rightPanel.revalidate();
	}

	@Override
	protected void buttonClicked (JButton button) {
		if (button == answer) {
			input.doCallback(textInput.getText());
		} else {
			input.doCallback(button.getText());
		}
		parent.disappear();
	}

	/** Called by parent window to indicate that the Dialog was cancelled.
	 * Calls the OnGetInput callback event with null parameter.
	 */
	public void cancel () {
		input.doCallback(null);
	}

}
