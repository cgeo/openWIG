package cz.matejcik.openwig.desktop.common;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/** Pretty renderer for items of WigList
 * <p>
 * Displays items with icon on the left, title and subtitle
 * in the middle and a status icon on the right.
 * <p>
 * Icon is either a default graphic, or a media provided by some objects.
 * Status icon can represent for example task state or a navigation arrow.
 * If the status icon is null, it is not displayed and the name/subtitle
 * can use the free space.
 */
public class ListItemRenderer extends JPanel implements ListCellRenderer {

	private JLabel iconLabel = new JLabel();
	private JLabel nameLabel = new JLabel();
	private JLabel subtitleLabel = new JLabel();
	private JLabel statusLabel = new JLabel();

	public ListItemRenderer () {
		Dimension iconDimension = new Dimension(32, 32);
		iconLabel.setPreferredSize(iconDimension);
		iconLabel.setMaximumSize(iconDimension);
		statusLabel.setPreferredSize(iconDimension);
		statusLabel.setMaximumSize(iconDimension);

		setLayout(new BorderLayout(3, 3));
		add(iconLabel, BorderLayout.WEST);
		add(statusLabel, BorderLayout.EAST);

		nameLabel.setAlignmentX(LEFT_ALIGNMENT);
		subtitleLabel.setAlignmentX(LEFT_ALIGNMENT);

		JPanel panel = new JPanel();
		panel.setOpaque(false);
		add(panel, BorderLayout.CENTER);
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(nameLabel);
		panel.add(subtitleLabel);
		subtitleLabel.setFont(new Font("Default", Font.PLAIN, subtitleLabel.getFont().getSize()));

		setBorder(new EmptyBorder(3, 3, 3, 3));
	}

	public Component getListCellRendererComponent (JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		ListItemAdapter adapter = (ListItemAdapter)value;
		iconLabel.setIcon(adapter.getIcon());
		statusLabel.setIcon(adapter.getStatus());
		statusLabel.setVisible(adapter.getStatus() != null);
		nameLabel.setText(adapter.getName());
		subtitleLabel.setText(adapter.getSubtitle());

		setToolTipText(adapter.getName());

		return this;
	}
}
