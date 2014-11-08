package cz.vhromada.catalog.commons;

import static cz.vhromada.catalog.commons.CatalogSwingConstants.HORIZONTAL_BUTTONS_GAP_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.HORIZONTAL_BUTTON_GAP_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.HORIZONTAL_BUTTON_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.HORIZONTAL_COMPONENT_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.HORIZONTAL_DATA_DIALOG_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.HORIZONTAL_DATA_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.HORIZONTAL_GAP_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.HORIZONTAL_LABEL_DIALOG_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.HORIZONTAL_LABEL_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.HORIZONTAL_LINK_BUTTONS_GAP_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.HORIZONTAL_LONG_GAP_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.HORIZONTAL_SCROLL_PANE_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.HORIZONTAL_SHORT_GAP_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.HORIZONTAL_TIME_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.HORIZONTAL_VERY_LONG_GAP_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.VERTICAL_BUTTON_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.VERTICAL_COMPONENT_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.VERTICAL_DATA_COMPONENT_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.VERTICAL_DATA_GAP_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.VERTICAL_GAP_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.VERTICAL_LONG_GAP_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.VERTICAL_PICTURE_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.VERTICAL_SHORT_GAP_SIZE;
import static cz.vhromada.catalog.commons.CatalogSwingConstants.VERTICAL_STATS_SCROLL_PANE_SIZE;

import java.util.Map;

import javax.swing.*;

import cz.vhromada.validators.Validators;

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
	 * @throws IllegalArgumentException if layout is null
	 *                                  or components are null
	 *                                  or components contain null key
	 *                                  or components contain null value
	 */
	public static GroupLayout.Group createHorizontalLayout(final GroupLayout layout, final Map<JLabel, JLabel> components, final JButton button) {
		Validators.validateArgumentNotNull(layout, "Layout");
		Validators.validateArgumentNotNull(components, "Components");
		Validators.validateMapNotContainNull(components, "Components");

		final GroupLayout.Group componentsGroup = layout.createParallelGroup();
		createHorizontalLayout(layout, componentsGroup, components);
		if (button != null) {
			componentsGroup.addComponent(button, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE);
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
	 * @throws IllegalArgumentException if layout is null
	 *                                  or picture is null
	 *                                  or components are null
	 *                                  or buttons are null
	 *                                  or components contain null key
	 *                                  or components contain null value
	 *                                  or buttons contain null value
	 */
	public static GroupLayout.Group createHorizontalLayoutWithPicture(final GroupLayout layout, final JLabel picture, final Map<JLabel, JLabel> components,
			final JButton... buttons) {
		Validators.validateArgumentNotNull(layout, "Layout");
		Validators.validateArgumentNotNull(picture, "Picture");
		Validators.validateArgumentNotNull(components, "Components");
		Validators.validateArgumentNotNull(buttons, "Buttons");
		Validators.validateMapNotContainNull(components, "Components");
		Validators.validateArrayNotContainNull(buttons, "Buttons");

		final GroupLayout.Group componentsGroup = layout.createParallelGroup()
				.addComponent(picture, HORIZONTAL_DATA_DIALOG_SIZE, HORIZONTAL_DATA_DIALOG_SIZE, HORIZONTAL_DATA_DIALOG_SIZE);
		createHorizontalLayout(layout, componentsGroup, components);
		final GroupLayout.Group buttonGroup = layout.createSequentialGroup();
		for (JButton button : buttons) {
			buttonGroup.addComponent(button, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE);
			if (!button.equals(buttons[buttons.length - 1])) {
				buttonGroup.addGap(HORIZONTAL_LINK_BUTTONS_GAP_SIZE);
			}
		}
		componentsGroup.addGroup(buttonGroup);
		return createResult(layout, componentsGroup);
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
				.addGap(HORIZONTAL_GAP_SIZE)
				.addGroup(components)
				.addGap(HORIZONTAL_GAP_SIZE);
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
				.addComponent(label, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE)
				.addGap(HORIZONTAL_GAP_SIZE)
				.addComponent(data, HORIZONTAL_DATA_SIZE, HORIZONTAL_DATA_SIZE, HORIZONTAL_DATA_SIZE);
	}

	/**
	 * Returns horizontal layout for components.
	 *
	 * @param layout layout
	 * @param groups group layouts for components
	 * @return horizontal layout for components
	 * @throws IllegalArgumentException if layout is null
	 *                                  or group layouts for components are null
	 *                                  or group layouts for components contain null value
	 */
	public static GroupLayout.Group createHorizontalDialogLayout(final GroupLayout layout, final GroupLayout.Group... groups) {
		Validators.validateArgumentNotNull(layout, "Layout");
		Validators.validateArgumentNotNull(groups, "Group layouts for components");
		Validators.validateArrayNotContainNull(groups, "Group layouts for components");

		final GroupLayout.Group componentsGroup = layout.createParallelGroup();
		for (GroupLayout.Group group : groups) {
			componentsGroup.addGroup(group);
		}
		return layout.createSequentialGroup()
				.addGap(HORIZONTAL_LONG_GAP_SIZE)
				.addGroup(componentsGroup)
				.addGap(HORIZONTAL_LONG_GAP_SIZE);
	}

	/**
	 * Returns horizontal layout for label component with data component.
	 *
	 * @param layout layout
	 * @param label  label component
	 * @param data   data component
	 * @return horizontal layout for label component with data component
	 * @throws IllegalArgumentException if layout is null
	 *                                  or label component is null
	 *                                  or data component is null
	 */
	public static GroupLayout.Group createHorizontalComponents(final GroupLayout layout, final JComponent label, final JComponent data) {
		Validators.validateArgumentNotNull(layout, "Layout");
		Validators.validateArgumentNotNull(label, "Label component");
		Validators.validateArgumentNotNull(data, "Data component ");

		return layout.createSequentialGroup()
				.addComponent(label, HORIZONTAL_LABEL_DIALOG_SIZE, HORIZONTAL_LABEL_DIALOG_SIZE, HORIZONTAL_LABEL_DIALOG_SIZE)
				.addGap(HORIZONTAL_GAP_SIZE)
				.addComponent(data, HORIZONTAL_DATA_DIALOG_SIZE, HORIZONTAL_DATA_DIALOG_SIZE, HORIZONTAL_DATA_DIALOG_SIZE);
	}

	/**
	 * Returns horizontal layout for check boxes.
	 *
	 * @param layout     layout
	 * @param checkBoxes check boxes
	 * @return horizontal layout for check boxes
	 * @throws IllegalArgumentException if layout is null
	 *                                  or check boxes are null
	 *                                  or check boxes contain null value
	 */
	public static GroupLayout.Group createHorizontalCheckBoxesComponents(final GroupLayout layout, final JCheckBox... checkBoxes) {
		Validators.validateArgumentNotNull(layout, "Layout");
		Validators.validateArgumentNotNull(checkBoxes, "Check boxes");
		Validators.validateArrayNotContainNull(checkBoxes, "Check boxes");

		final GroupLayout.Group result = layout.createParallelGroup();
		for (JCheckBox checkBox : checkBoxes) {
			result.addComponent(checkBox, HORIZONTAL_COMPONENT_SIZE, HORIZONTAL_COMPONENT_SIZE, HORIZONTAL_COMPONENT_SIZE);
		}
		return result;
	}

	/**
	 * Returns horizontal layout for selectable components.
	 *
	 * @param layout     layout
	 * @param components components
	 * @return horizontal layout for selectable components
	 * @throws IllegalArgumentException if layout is null
	 *                                  or components are null
	 *                                  or components contain null value
	 */
	public static GroupLayout.Group createHorizontalSelectableComponents(final GroupLayout layout, final JComponent... components) {
		Validators.validateArgumentNotNull(layout, "Layout");
		Validators.validateArgumentNotNull(components, "Components");
		Validators.validateArrayNotContainNull(components, "Components");

		final GroupLayout.Group result = layout.createParallelGroup();
		for (JComponent component : components) {
			final GroupLayout.Group group = layout.createSequentialGroup()
					.addGap(HORIZONTAL_VERY_LONG_GAP_SIZE)
					.addComponent(component, HORIZONTAL_DATA_DIALOG_SIZE, HORIZONTAL_DATA_DIALOG_SIZE, HORIZONTAL_DATA_DIALOG_SIZE);
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
	 * @throws IllegalArgumentException if layout is null
	 *                                  or label is null
	 *                                  or data - hours are null
	 *                                  or data - minutes are null
	 *                                  or data - seconds are null
	 */
	public static GroupLayout.Group createHorizontalLengthComponents(final GroupLayout layout, final JComponent label, final JSpinner hours,
			final JSpinner minutes, final JSpinner seconds) {
		Validators.validateArgumentNotNull(layout, "Layout");
		Validators.validateArgumentNotNull(label, "Label");
		Validators.validateArgumentNotNull(hours, "Data - hours");
		Validators.validateArgumentNotNull(minutes, "Data - minutes");
		Validators.validateArgumentNotNull(seconds, "Data - seconds");

		final GroupLayout.SequentialGroup lengthData = layout.createSequentialGroup()
				.addComponent(hours, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE)
				.addGap(HORIZONTAL_GAP_SIZE)
				.addComponent(minutes, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE)
				.addGap(HORIZONTAL_GAP_SIZE)
				.addComponent(seconds, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE);
		return layout.createSequentialGroup()
				.addComponent(label, HORIZONTAL_LABEL_DIALOG_SIZE, HORIZONTAL_LABEL_DIALOG_SIZE, HORIZONTAL_LABEL_DIALOG_SIZE)
				.addGap(HORIZONTAL_GAP_SIZE)
				.addGroup(lengthData);
	}

	/**
	 * Returns horizontal layout for buttons.
	 *
	 * @param layout       layout
	 * @param okButton     OK button
	 * @param cancelButton cancel button
	 * @return horizontal layout for buttons
	 * @throws IllegalArgumentException if layout is null
	 *                                  or OK button is null
	 *                                  or cancel button is null
	 */
	public static GroupLayout.Group createHorizontalButtonComponents(final GroupLayout layout, final JButton okButton, final JButton cancelButton) {
		Validators.validateArgumentNotNull(layout, "Layout");
		Validators.validateArgumentNotNull(okButton, "OK button");
		Validators.validateArgumentNotNull(cancelButton, "Cancel button");

		return layout.createSequentialGroup()
				.addGap(HORIZONTAL_BUTTON_GAP_SIZE)
				.addComponent(okButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE)
				.addGap(HORIZONTAL_BUTTONS_GAP_SIZE)
				.addComponent(cancelButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE)
				.addGap(HORIZONTAL_BUTTON_GAP_SIZE);
	}

	/**
	 * Returns horizontal layout of components.
	 *
	 * @param layout         layout
	 * @param listScrollPane scroll pane for list
	 * @param tabbedPane     tabbed pane
	 * @return horizontal layout of components
	 * @throws IllegalArgumentException if layout is null
	 *                                  or scroll pane for list is null
	 *                                  or tabbed pane is null
	 */
	public static GroupLayout.Group createHorizontalLayout(final GroupLayout layout, final JScrollPane listScrollPane, final JTabbedPane tabbedPane) {
		Validators.validateArgumentNotNull(layout, "Layout");
		Validators.validateArgumentNotNull(listScrollPane, "Scroll pane for list");
		Validators.validateArgumentNotNull(tabbedPane, "Tabbed pane");

		return layout.createSequentialGroup()
				.addComponent(listScrollPane, HORIZONTAL_SCROLL_PANE_SIZE, HORIZONTAL_SCROLL_PANE_SIZE, HORIZONTAL_SCROLL_PANE_SIZE)
				.addGap(HORIZONTAL_SHORT_GAP_SIZE)
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
	 * @throws IllegalArgumentException if layout is null
	 *                                  or scroll pane for list is null
	 *                                  or tabbed pane is null
	 *                                  or scroll pane for table with stats is null
	 */
	public static GroupLayout.Group createHorizontalLayout(final GroupLayout layout, final JScrollPane listScrollPane, final JTabbedPane tabbedPane,
			final JScrollPane statsTableScrollPane) {
		Validators.validateArgumentNotNull(layout, "Layout");
		Validators.validateArgumentNotNull(listScrollPane, "Scroll pane for list");
		Validators.validateArgumentNotNull(tabbedPane, "Tabbed pane");
		Validators.validateArgumentNotNull(statsTableScrollPane, "Scroll pane for table with stats is null");

		return layout.createParallelGroup()
				.addGroup(createHorizontalLayout(layout, listScrollPane, tabbedPane))
				.addComponent(statsTableScrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
	}

	/**
	 * Returns vertical layout for components.
	 *
	 * @param layout     layout
	 * @param components components (Map: label -> data)
	 * @param button     button
	 * @return vertical layout for components
	 * @throws IllegalArgumentException if layout is null
	 *                                  or components are null
	 *                                  or components contain null key
	 *                                  or components contain null value
	 */
	public static GroupLayout.Group createVerticalLayout(final GroupLayout layout, final Map<? extends JComponent, ? extends JComponent> components,
			final JButton button) {
		Validators.validateArgumentNotNull(layout, "Layout");
		Validators.validateArgumentNotNull(components, "Components");
		Validators.validateMapNotContainNull(components, "Components");

		final GroupLayout.Group result = layout.createSequentialGroup()
				.addGap(VERTICAL_SHORT_GAP_SIZE);
		createVerticalLayout(layout, result, components);
		if (button != null) {
			result.addComponent(button, VERTICAL_BUTTON_SIZE, VERTICAL_BUTTON_SIZE, VERTICAL_BUTTON_SIZE);
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
	 * @throws IllegalArgumentException if layout is null
	 *                                  or components are null
	 *                                  or buttons are null
	 *                                  or components contain null key
	 *                                  or components contain null value
	 *                                  or buttons contain null value
	 */
	public static GroupLayout.Group createVerticalLayoutWithPicture(final GroupLayout layout, final JLabel picture,
			final Map<? extends JComponent, ? extends JComponent> components, final JButton... buttons) {
		Validators.validateArgumentNotNull(layout, "Layout");
		Validators.validateArgumentNotNull(components, "Components");
		Validators.validateArgumentNotNull(buttons, "Buttons");
		Validators.validateMapNotContainNull(components, "Components");
		Validators.validateArrayNotContainNull(buttons, "Buttons");

		final GroupLayout.Group result = layout.createSequentialGroup()
				.addGap(VERTICAL_SHORT_GAP_SIZE)
				.addComponent(picture, VERTICAL_PICTURE_SIZE, VERTICAL_PICTURE_SIZE, VERTICAL_PICTURE_SIZE)
				.addGap(VERTICAL_GAP_SIZE);
		createVerticalLayout(layout, result, components);
		final GroupLayout.Group buttonGroup = layout.createParallelGroup();
		for (JButton button : buttons) {
			buttonGroup.addComponent(button, VERTICAL_BUTTON_SIZE, VERTICAL_BUTTON_SIZE, VERTICAL_BUTTON_SIZE);
		}
		return result.addGroup(buttonGroup);
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
					.addGap(VERTICAL_GAP_SIZE);
		}
	}

	/**
	 * Returns vertical layout for label component with data component.
	 *
	 * @param layout layout
	 * @param label  label component
	 * @param data   data component
	 * @return vertical layout for label component with data component
	 * @throws IllegalArgumentException if layout is null
	 *                                  or label component is null
	 *                                  or data component is null
	 */
	public static GroupLayout.Group createVerticalComponents(final GroupLayout layout, final JComponent label, final JComponent data) {
		Validators.validateArgumentNotNull(layout, "Layout");
		Validators.validateArgumentNotNull(label, "Label component");
		Validators.validateArgumentNotNull(data, "Data component ");

		return layout.createParallelGroup()
				.addComponent(label, VERTICAL_COMPONENT_SIZE, VERTICAL_COMPONENT_SIZE, VERTICAL_COMPONENT_SIZE)
				.addGap(VERTICAL_GAP_SIZE)
				.addComponent(data, VERTICAL_COMPONENT_SIZE, VERTICAL_COMPONENT_SIZE, VERTICAL_COMPONENT_SIZE);
	}

	/**
	 * Returns vertical layout for components.
	 *
	 * @param layout       layout
	 * @param okButton     OK button
	 * @param cancelButton cancel button
	 * @param groups       group layouts for components
	 * @return vertical layout for components
	 * @throws IllegalArgumentException if layout is null
	 *                                  or OK button is null
	 *                                  or cancel button is null
	 *                                  or group layouts for components are null
	 *                                  or group layouts for components contain null value
	 */
	public static GroupLayout.Group createVerticalDialogLayout(final GroupLayout layout, final JButton okButton,
			final JButton cancelButton, final GroupLayout.Group... groups) {
		Validators.validateArgumentNotNull(layout, "Layout");
		Validators.validateArgumentNotNull(okButton, "OK button");
		Validators.validateArgumentNotNull(cancelButton, "Cancel button");
		Validators.validateArgumentNotNull(groups, "Group layouts for components");
		Validators.validateArrayNotContainNull(groups, "Group layouts for components");

		final GroupLayout.Group result = layout.createSequentialGroup()
				.addGap(VERTICAL_LONG_GAP_SIZE);
		for (GroupLayout.Group group : groups) {
			result.addGroup(group);
			if (!group.equals(groups[groups.length - 1])) {
				result.addGap(VERTICAL_GAP_SIZE);
			}
		}
		return result.addGap(VERTICAL_LONG_GAP_SIZE)
				.addGroup(createVerticalButtonComponents(layout, okButton, cancelButton))
				.addGap(VERTICAL_LONG_GAP_SIZE);
	}

	/**
	 * Returns vertical layout for selectable components.
	 *
	 * @param layout     layout
	 * @param components components
	 * @return vertical layout for selectable components
	 * @throws IllegalArgumentException if layout is null
	 *                                  or components are null
	 *                                  or components contain null value
	 */
	public static GroupLayout.Group createVerticalSelectableComponents(final GroupLayout layout, final JComponent... components) {
		Validators.validateArgumentNotNull(layout, "Layout");
		Validators.validateArgumentNotNull(components, "Components");
		Validators.validateArrayNotContainNull(components, "Components");

		final GroupLayout.Group result = layout.createSequentialGroup();
		for (JComponent component : components) {
			result.addComponent(component, VERTICAL_COMPONENT_SIZE, VERTICAL_COMPONENT_SIZE, VERTICAL_COMPONENT_SIZE);
			if (!component.equals(components[components.length - 1])) {
				result.addGap(VERTICAL_GAP_SIZE);
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
	 * @throws IllegalArgumentException if layout is null
	 *                                  or label is null
	 *                                  or data - hours are null
	 *                                  or data - minutes are null
	 *                                  or data - seconds are null
	 */
	public static GroupLayout.Group createVerticalLengthComponents(final GroupLayout layout, final JComponent label, final JSpinner hours,
			final JSpinner minutes, final JSpinner seconds) {
		Validators.validateArgumentNotNull(layout, "Layout");
		Validators.validateArgumentNotNull(label, "Label");
		Validators.validateArgumentNotNull(hours, "Data - hours");
		Validators.validateArgumentNotNull(minutes, "Data - minutes");
		Validators.validateArgumentNotNull(seconds, "Data - seconds");

		final GroupLayout.Group lengthComponents = layout.createParallelGroup()
				.addComponent(label, VERTICAL_COMPONENT_SIZE, VERTICAL_COMPONENT_SIZE, VERTICAL_COMPONENT_SIZE)
				.addComponent(hours, VERTICAL_COMPONENT_SIZE, VERTICAL_COMPONENT_SIZE, VERTICAL_COMPONENT_SIZE)
				.addComponent(minutes, VERTICAL_COMPONENT_SIZE, VERTICAL_COMPONENT_SIZE, VERTICAL_COMPONENT_SIZE)
				.addComponent(seconds, VERTICAL_COMPONENT_SIZE, VERTICAL_COMPONENT_SIZE, VERTICAL_COMPONENT_SIZE);
		return layout.createParallelGroup()
				.addComponent(label, VERTICAL_COMPONENT_SIZE, VERTICAL_COMPONENT_SIZE, VERTICAL_COMPONENT_SIZE)
				.addGap(VERTICAL_GAP_SIZE)
				.addGroup(lengthComponents);
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
			result.addComponent(button, VERTICAL_BUTTON_SIZE, VERTICAL_BUTTON_SIZE, VERTICAL_BUTTON_SIZE);
		}
		return result;
	}

	/**
	 * Returns vertical layout of components.
	 *
	 * @param layout         layout
	 * @param listScrollPane scroll pane for list
	 * @param tabbedPane     tabbed pane
	 * @return vertical layout of components
	 * @throws IllegalArgumentException if layout is null
	 *                                  or scroll pane for list is null
	 *                                  or tabbed pane is null
	 */
	public static GroupLayout.Group createVerticalLayout(final GroupLayout layout, final JScrollPane listScrollPane, final JTabbedPane tabbedPane) {
		Validators.validateArgumentNotNull(layout, "Layout");
		Validators.validateArgumentNotNull(listScrollPane, "Scroll pane for list");
		Validators.validateArgumentNotNull(tabbedPane, "Tabbed pane");

		return layout.createParallelGroup()
				.addComponent(listScrollPane, VERTICAL_DATA_COMPONENT_SIZE, VERTICAL_DATA_COMPONENT_SIZE, Short.MAX_VALUE)
				.addComponent(tabbedPane, VERTICAL_DATA_COMPONENT_SIZE, VERTICAL_DATA_COMPONENT_SIZE, Short.MAX_VALUE);
	}

	/**
	 * Returns vertical layout of components.
	 *
	 * @param layout               layout
	 * @param listScrollPane       scroll pane for list
	 * @param tabbedPane           tabbed pane
	 * @param statsTableScrollPane scroll pane for table with stats
	 * @return vertical layout of components
	 * @throws IllegalArgumentException if layout is null
	 *                                  or scroll pane for list is null
	 *                                  or tabbed pane is null
	 *                                  or scroll pane for table with stats is null
	 */
	public static GroupLayout.Group createVerticalLayout(final GroupLayout layout, final JScrollPane listScrollPane, final JTabbedPane tabbedPane,
			final JScrollPane statsTableScrollPane) {
		Validators.validateArgumentNotNull(layout, "Layout");
		Validators.validateArgumentNotNull(listScrollPane, "Scroll pane for list");
		Validators.validateArgumentNotNull(tabbedPane, "Tabbed pane");
		Validators.validateArgumentNotNull(statsTableScrollPane, "Scroll pane for table with stats is null");

		return layout.createSequentialGroup()
				.addGroup(createVerticalLayout(layout, listScrollPane, tabbedPane))
				.addGap(VERTICAL_DATA_GAP_SIZE)
				.addComponent(statsTableScrollPane, VERTICAL_STATS_SCROLL_PANE_SIZE, VERTICAL_STATS_SCROLL_PANE_SIZE, VERTICAL_STATS_SCROLL_PANE_SIZE);
	}

}
