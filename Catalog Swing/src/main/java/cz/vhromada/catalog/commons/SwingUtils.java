package cz.vhromada.catalog.commons;

import java.util.Map;

import javax.swing.*;

/**
 * A class represents utility class for creating layouts.
 *
 * @author Vladimir Hromada
 */
public final class SwingUtils {

	/** Creates a new instance of SwingUtils. */
	private SwingUtils() {
	}

	/**
	 * Returns horizontal layout for components.
	 *
	 * @param layout     layout
	 * @param components components (Map: label -> data)
	 * @param button     button
	 * @return horizontal layout for components
	 */
	public static GroupLayout.Group createHorizontalLayout(final GroupLayout layout, final Map<JLabel, JLabel> components, final JButton button) {
		final GroupLayout.Group componentsGroup = layout.createParallelGroup();
		createHorizontalLayout(layout, componentsGroup, components);
		if (button != null) {
			componentsGroup.addComponent(button, CatalogSwingConstants.HORIZONTAL_BUTTON_SIZE, CatalogSwingConstants.HORIZONTAL_BUTTON_SIZE,
					CatalogSwingConstants.HORIZONTAL_BUTTON_SIZE);
		}
		return createResult(layout, componentsGroup);
	}

	/**
	 * Returns horizontal layout for components.
	 *
	 * @param layout     layout
	 * @param picture    picture component
	 * @param components components (Map: label -> data)
	 * @param buttons    buttons
	 * @return horizontal layout for components
	 */
	public static GroupLayout.Group createHorizontalLayoutWithPicture(final GroupLayout layout, final JLabel picture, final Map<JLabel, JLabel> components,
			final JButton... buttons) {
		final GroupLayout.Group componentsGroup = layout.createParallelGroup()
				.addComponent(picture, CatalogSwingConstants.HORIZONTAL_DATA_DIALOG_SIZE, CatalogSwingConstants.HORIZONTAL_DATA_DIALOG_SIZE,
						CatalogSwingConstants.HORIZONTAL_DATA_DIALOG_SIZE);
		createHorizontalLayout(layout, componentsGroup, components);
		final GroupLayout.Group buttonGroup = layout.createSequentialGroup();
		for (JButton button : buttons) {
			buttonGroup.addComponent(button, CatalogSwingConstants.HORIZONTAL_BUTTON_SIZE, CatalogSwingConstants.HORIZONTAL_BUTTON_SIZE,
					CatalogSwingConstants.HORIZONTAL_BUTTON_SIZE);
			if (!button.equals(buttons[buttons.length - 1])) {
				buttonGroup.addGap(CatalogSwingConstants.HORIZONTAL_LINK_BUTTONS_GAP_SIZE);
			}
		}
		componentsGroup.addGroup(buttonGroup);
		return createResult(layout, componentsGroup);
	}

	/**
	 * Returns horizontal layout for components.
	 *
	 * @param layout layout
	 * @param groups group layouts for components
	 * @return horizontal layout for components
	 */
	public static GroupLayout.Group createHorizontalDialogLayout(final GroupLayout layout, final GroupLayout.Group... groups) {
		final GroupLayout.Group componentsGroup = layout.createParallelGroup();
		for (GroupLayout.Group group : groups) {
			componentsGroup.addGroup(group);
		}
		return layout.createSequentialGroup()
				.addGap(CatalogSwingConstants.HORIZONTAL_LONG_GAP_SIZE)
				.addGroup(componentsGroup)
				.addGap(CatalogSwingConstants.HORIZONTAL_LONG_GAP_SIZE);
	}

	/**
	 * Returns horizontal layout for label component with data component.
	 *
	 * @param layout layout
	 * @param label  label component
	 * @param data   data component
	 * @return horizontal layout for label component with data component
	 */
	public static GroupLayout.Group createHorizontalComponents(final GroupLayout layout, final JComponent label, final JComponent data) {
		return layout.createSequentialGroup()
				.addComponent(label, CatalogSwingConstants.HORIZONTAL_LABEL_DIALOG_SIZE, CatalogSwingConstants.HORIZONTAL_LABEL_DIALOG_SIZE,
						CatalogSwingConstants.HORIZONTAL_LABEL_DIALOG_SIZE)
				.addGap(CatalogSwingConstants.HORIZONTAL_GAP_SIZE)
				.addComponent(data, CatalogSwingConstants.HORIZONTAL_DATA_DIALOG_SIZE, CatalogSwingConstants.HORIZONTAL_DATA_DIALOG_SIZE,
						CatalogSwingConstants.HORIZONTAL_DATA_DIALOG_SIZE);
	}

	/**
	 * Returns horizontal layout for check boxes.
	 *
	 * @param layout     layout
	 * @param checkBoxes check boxes
	 * @return horizontal layout for check boxes
	 */
	public static GroupLayout.Group createHorizontalCheckBoxesComponents(final GroupLayout layout, final JCheckBox... checkBoxes) {
		final GroupLayout.Group result = layout.createParallelGroup();
		for (JCheckBox checkBox : checkBoxes) {
			result.addComponent(checkBox, CatalogSwingConstants.HORIZONTAL_COMPONENT_SIZE, CatalogSwingConstants.HORIZONTAL_COMPONENT_SIZE,
					CatalogSwingConstants.HORIZONTAL_COMPONENT_SIZE);
		}
		return result;
	}

	/**
	 * Returns horizontal layout for selectable components.
	 *
	 * @param layout     layout
	 * @param components components
	 * @return horizontal layout for selectable components
	 */
	public static GroupLayout.Group createHorizontalSelectableComponents(final GroupLayout layout, final JComponent... components) {
		final GroupLayout.Group result = layout.createParallelGroup();
		for (JComponent component : components) {
			final GroupLayout.Group group = layout.createSequentialGroup()
					.addGap(CatalogSwingConstants.HORIZONTAL_VERY_LONG_GAP_SIZE)
					.addComponent(component, CatalogSwingConstants.HORIZONTAL_DATA_DIALOG_SIZE, CatalogSwingConstants.HORIZONTAL_DATA_DIALOG_SIZE,
							CatalogSwingConstants.HORIZONTAL_DATA_DIALOG_SIZE);
			result.addGroup(group);
		}
		return result;
	}

	/**
	 * Returns horizontal layout for length components.
	 *
	 * @param layout  layout
	 * @param label   label
	 * @param hours   data - hours
	 * @param minutes data - minutes
	 * @param seconds data - seconds
	 * @return horizontal layout for length components
	 */
	public static GroupLayout.Group createHorizontalLengthComponents(final GroupLayout layout, final JComponent label, final JSpinner hours,
			final JSpinner minutes, final JSpinner seconds) {
		final GroupLayout.SequentialGroup lengthData = layout.createSequentialGroup()
				.addComponent(hours, CatalogSwingConstants.HORIZONTAL_TIME_SIZE, CatalogSwingConstants.HORIZONTAL_TIME_SIZE,
						CatalogSwingConstants.HORIZONTAL_TIME_SIZE)
				.addGap(CatalogSwingConstants.HORIZONTAL_GAP_SIZE)
				.addComponent(minutes, CatalogSwingConstants.HORIZONTAL_TIME_SIZE, CatalogSwingConstants.HORIZONTAL_TIME_SIZE,
						CatalogSwingConstants.HORIZONTAL_TIME_SIZE)
				.addGap(CatalogSwingConstants.HORIZONTAL_GAP_SIZE)
				.addComponent(seconds, CatalogSwingConstants.HORIZONTAL_TIME_SIZE, CatalogSwingConstants.HORIZONTAL_TIME_SIZE,
						CatalogSwingConstants.HORIZONTAL_TIME_SIZE);
		return layout.createSequentialGroup()
				.addComponent(label, CatalogSwingConstants.HORIZONTAL_LABEL_DIALOG_SIZE, CatalogSwingConstants.HORIZONTAL_LABEL_DIALOG_SIZE,
						CatalogSwingConstants.HORIZONTAL_LABEL_DIALOG_SIZE)
				.addGap(CatalogSwingConstants.HORIZONTAL_GAP_SIZE)
				.addGroup(lengthData);
	}

	/**
	 * Returns horizontal layout for buttons.
	 *
	 * @param layout       layout
	 * @param okButton     OK button
	 * @param cancelButton cancel button
	 * @return horizontal layout for buttons
	 */
	public static GroupLayout.Group createHorizontalButtonComponents(final GroupLayout layout, final JButton okButton, final JButton cancelButton) {
		return layout.createSequentialGroup()
				.addGap(CatalogSwingConstants.HORIZONTAL_BUTTON_GAP_SIZE)
				.addComponent(okButton, CatalogSwingConstants.HORIZONTAL_BUTTON_SIZE, CatalogSwingConstants.HORIZONTAL_BUTTON_SIZE,
						CatalogSwingConstants.HORIZONTAL_BUTTON_SIZE)
				.addGap(CatalogSwingConstants.HORIZONTAL_BUTTONS_GAP_SIZE)
				.addComponent(cancelButton, CatalogSwingConstants.HORIZONTAL_BUTTON_SIZE, CatalogSwingConstants.HORIZONTAL_BUTTON_SIZE,
						CatalogSwingConstants.HORIZONTAL_BUTTON_SIZE)
				.addGap(CatalogSwingConstants.HORIZONTAL_BUTTON_GAP_SIZE);
	}

	/**
	 * Returns horizontal layout of components.
	 *
	 * @param layout         layout
	 * @param listScrollPane scroll pane for list
	 * @param tabbedPane     tabbed pane
	 * @return horizontal layout of components
	 */
	public static GroupLayout.Group createHorizontalLayout(final GroupLayout layout, final JScrollPane listScrollPane, final JTabbedPane tabbedPane) {
		return layout.createSequentialGroup()
				.addComponent(listScrollPane, CatalogSwingConstants.HORIZONTAL_SCROLL_PANE_SIZE, CatalogSwingConstants.HORIZONTAL_SCROLL_PANE_SIZE,
						CatalogSwingConstants.HORIZONTAL_SCROLL_PANE_SIZE)
				.addGap(CatalogSwingConstants.HORIZONTAL_SHORT_GAP_SIZE)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
	}

	/**
	 * Returns horizontal layout of components.
	 *
	 * @param layout               layout
	 * @param listScrollPane       scroll pane for list
	 * @param tabbedPane           tabbed pane
	 * @param statsTableScrollPane scroll pane for table with stats
	 * @return horizontal layout of components
	 */
	public static GroupLayout.Group createHorizontalLayout(final GroupLayout layout, final JScrollPane listScrollPane, final JTabbedPane tabbedPane,
			final JScrollPane statsTableScrollPane) {
		return layout.createParallelGroup()
				.addGroup(createHorizontalLayout(layout, listScrollPane, tabbedPane))
				.addComponent(statsTableScrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
	}

	/**
	 * Creates horizontal layout for components.
	 *
	 * @param layout     layout
	 * @param group      layout group
	 * @param components components (Map: label -> data)
	 */
	private static void createHorizontalLayout(final GroupLayout layout, final GroupLayout.Group group, final Map<JLabel, JLabel> components) {
		for (Map.Entry<JLabel, JLabel> data : components.entrySet()) {
			group.addGroup(createHorizontalDataComponents(layout, data.getKey(), data.getValue()));
		}
	}

	/**
	 * Returns horizontal layout for components.
	 *
	 * @param layout     layout
	 * @param components group layout for components
	 * @return horizontal layout for components
	 */
	private static GroupLayout.Group createResult(final GroupLayout layout, final GroupLayout.Group components) {
		return layout.createSequentialGroup()
				.addGap(CatalogSwingConstants.HORIZONTAL_GAP_SIZE)
				.addGroup(components)
				.addGap(CatalogSwingConstants.HORIZONTAL_GAP_SIZE);
	}

	/**
	 * Returns horizontal layout for label component with data component.
	 *
	 * @param layout layout
	 * @param label  label
	 * @param data   data
	 * @return horizontal layout for label component with data component
	 */
	private static GroupLayout.Group createHorizontalDataComponents(final GroupLayout layout, final JLabel label, final JLabel data) {
		return layout.createSequentialGroup()
				.addComponent(label, CatalogSwingConstants.HORIZONTAL_LABEL_SIZE, CatalogSwingConstants.HORIZONTAL_LABEL_SIZE,
						CatalogSwingConstants.HORIZONTAL_LABEL_SIZE)
				.addGap(CatalogSwingConstants.HORIZONTAL_GAP_SIZE)
				.addComponent(data, CatalogSwingConstants.HORIZONTAL_DATA_SIZE, CatalogSwingConstants.HORIZONTAL_DATA_SIZE,
						CatalogSwingConstants.HORIZONTAL_DATA_SIZE);
	}

	/**
	 * Returns vertical layout for components.
	 *
	 * @param layout     layout
	 * @param components components (Map: label -> data)
	 * @param button     button
	 * @return vertical layout for components
	 */
	public static GroupLayout.Group createVerticalLayout(final GroupLayout layout, final Map<? extends JComponent, ? extends JComponent> components,
			final JButton button) {
		final GroupLayout.Group result = layout.createSequentialGroup()
				.addGap(CatalogSwingConstants.VERTICAL_SHORT_GAP_SIZE);
		createVerticalLayout(layout, result, components);
		if (button != null) {
			result.addComponent(button, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
					CatalogSwingConstants.VERTICAL_BUTTON_SIZE);
		}
		return result;
	}

	/**
	 * Returns vertical layout for components.
	 *
	 * @param layout     layout
	 * @param picture    picture component
	 * @param components components (Map: label -> data)
	 * @param buttons    buttons
	 * @return vertical layout for components
	 */
	public static GroupLayout.Group createVerticalLayoutWithPicture(final GroupLayout layout, final JLabel picture,
			final Map<? extends JComponent, ? extends JComponent> components, final JButton... buttons) {
		final GroupLayout.Group result = layout.createSequentialGroup()
				.addGap(CatalogSwingConstants.VERTICAL_SHORT_GAP_SIZE)
				.addComponent(picture, CatalogSwingConstants.VERTICAL_PICTURE_SIZE, CatalogSwingConstants.VERTICAL_PICTURE_SIZE,
						CatalogSwingConstants.VERTICAL_PICTURE_SIZE)
				.addGap(CatalogSwingConstants.VERTICAL_GAP_SIZE);
		createVerticalLayout(layout, result, components);
		final GroupLayout.Group buttonGroup = layout.createParallelGroup();
		for (JButton button : buttons) {
			buttonGroup.addComponent(button, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
					CatalogSwingConstants.VERTICAL_BUTTON_SIZE);
		}
		return result.addGroup(buttonGroup);
	}


	/**
	 * Returns vertical layout for label component with data component.
	 *
	 * @param layout layout
	 * @param label  label component
	 * @param data   data component
	 * @return vertical layout for label component with data component
	 */
	public static GroupLayout.Group createVerticalComponents(final GroupLayout layout, final JComponent label, final JComponent data) {
		return layout.createParallelGroup()
				.addComponent(label, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
						CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
				.addGap(CatalogSwingConstants.VERTICAL_GAP_SIZE)
				.addComponent(data, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
						CatalogSwingConstants.VERTICAL_COMPONENT_SIZE);
	}

	/**
	 * Returns vertical layout for components.
	 *
	 * @param layout       layout
	 * @param okButton     OK button
	 * @param cancelButton cancel button
	 * @param groups       group layouts for components
	 * @return vertical layout for components
	 */
	public static GroupLayout.Group createVerticalDialogLayout(final GroupLayout layout, final JButton okButton,
			final JButton cancelButton, final GroupLayout.Group... groups) {
		final GroupLayout.Group result = layout.createSequentialGroup()
				.addGap(CatalogSwingConstants.VERTICAL_LONG_GAP_SIZE);
		for (GroupLayout.Group group : groups) {
			result.addGroup(group);
			if (!group.equals(groups[groups.length - 1])) {
				result.addGap(CatalogSwingConstants.VERTICAL_GAP_SIZE);
			}
		}
		return result.addGap(CatalogSwingConstants.VERTICAL_LONG_GAP_SIZE)
				.addGroup(createVerticalButtonComponents(layout, okButton, cancelButton))
				.addGap(CatalogSwingConstants.VERTICAL_LONG_GAP_SIZE);
	}

	/**
	 * Returns vertical layout for selectable components.
	 *
	 * @param layout     layout
	 * @param components components
	 * @return vertical layout for selectable components
	 */
	public static GroupLayout.Group createVerticalSelectableComponents(final GroupLayout layout, final JComponent... components) {
		final GroupLayout.Group result = layout.createSequentialGroup();
		for (JComponent component : components) {
			result.addComponent(component, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
					CatalogSwingConstants.VERTICAL_COMPONENT_SIZE);
			if (!component.equals(components[components.length - 1])) {
				result.addGap(CatalogSwingConstants.VERTICAL_GAP_SIZE);
			}
		}
		return result;
	}

	/**
	 * Returns vertical layout for length components.
	 *
	 * @param layout  layout
	 * @param label   label
	 * @param hours   data - hours
	 * @param minutes data - minutes
	 * @param seconds data - seconds
	 * @return vertical layout for length components
	 */
	public static GroupLayout.Group createVerticalLengthComponents(final GroupLayout layout, final JComponent label, final JSpinner hours,
			final JSpinner minutes, final JSpinner seconds) {
		final GroupLayout.Group lengthComponents = layout.createParallelGroup()
				.addComponent(label, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
						CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
				.addComponent(hours, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
						CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
				.addComponent(minutes, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
						CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
				.addComponent(seconds, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
						CatalogSwingConstants.VERTICAL_COMPONENT_SIZE);
		return layout.createParallelGroup()
				.addComponent(label, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
						CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
				.addGap(CatalogSwingConstants.VERTICAL_GAP_SIZE)
				.addGroup(lengthComponents);
	}

	/**
	 * Returns vertical layout of components.
	 *
	 * @param layout         layout
	 * @param listScrollPane scroll pane for list
	 * @param tabbedPane     tabbed pane
	 * @return vertical layout of components
	 */
	public static GroupLayout.Group createVerticalLayout(final GroupLayout layout, final JScrollPane listScrollPane, final JTabbedPane tabbedPane) {
		return layout.createParallelGroup()
				.addComponent(listScrollPane, CatalogSwingConstants.VERTICAL_DATA_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_DATA_COMPONENT_SIZE,
						Short.MAX_VALUE)
				.addComponent(tabbedPane, CatalogSwingConstants.VERTICAL_DATA_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_DATA_COMPONENT_SIZE,
						Short.MAX_VALUE);
	}

	/**
	 * Returns vertical layout of components.
	 *
	 * @param layout               layout
	 * @param listScrollPane       scroll pane for list
	 * @param tabbedPane           tabbed pane
	 * @param statsTableScrollPane scroll pane for table with stats
	 * @return vertical layout of components
	 */
	public static GroupLayout.Group createVerticalLayout(final GroupLayout layout, final JScrollPane listScrollPane, final JTabbedPane tabbedPane,
			final JScrollPane statsTableScrollPane) {
		return layout.createSequentialGroup()
				.addGroup(createVerticalLayout(layout, listScrollPane, tabbedPane))
				.addGap(CatalogSwingConstants.VERTICAL_DATA_GAP_SIZE)
				.addComponent(statsTableScrollPane, CatalogSwingConstants.VERTICAL_STATS_SCROLL_PANE_SIZE,
						CatalogSwingConstants.VERTICAL_STATS_SCROLL_PANE_SIZE, CatalogSwingConstants.VERTICAL_STATS_SCROLL_PANE_SIZE);
	}

	/**
	 * Creates vertical layout for components.
	 *
	 * @param layout     layout
	 * @param group      layout group
	 * @param components components (Map: label -> data)
	 */
	private static void createVerticalLayout(final GroupLayout layout, final GroupLayout.Group group,
			final Map<? extends JComponent, ? extends JComponent> components) {
		for (Map.Entry<? extends JComponent, ? extends JComponent> data : components.entrySet()) {
			group.addGroup(createVerticalComponents(layout, data.getKey(), data.getValue()))
					.addGap(CatalogSwingConstants.VERTICAL_GAP_SIZE);
		}
	}

	/**
	 * Returns vertical layout for buttons.
	 *
	 * @param layout  layout
	 * @param buttons buttons
	 * @return vertical layout for buttons
	 */
	private static GroupLayout.Group createVerticalButtonComponents(final GroupLayout layout,
			final JButton... buttons) {
		final GroupLayout.Group result = layout.createParallelGroup();
		for (JButton button : buttons) {
			result.addComponent(button, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
					CatalogSwingConstants.VERTICAL_BUTTON_SIZE);
		}
		return result;
	}

}
