package cz.vhromada.catalog.gui.movie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.gui.commons.CatalogSwingConstants;
import cz.vhromada.catalog.gui.commons.DialogResult;
import cz.vhromada.catalog.gui.commons.Picture;
import cz.vhromada.catalog.gui.commons.TimeDataPanel;
import cz.vhromada.validators.Validators;

/**
 * A class represents dialog for choosing media.
 *
 * @author Vladimir Hromada
 */
public class MediaChooseDialog extends JDialog {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Maximum media count
     */
    private static final int MAX_MEDIA_COUNT = 10;

    /**
     * Horizontal label size
     */
    private static final int HORIZONTAL_LABEL_DIALOG_SIZE = 100;

    /**
     * Horizontal data size
     */
    private static final int HORIZONTAL_DATA_DIALOG_SIZE = 200;

    /**
     * Horizontal button size
     */
    private static final int HORIZONTAL_BUTTON_SIZE = 96;

    /**
     * Horizontal medium panel size
     */
    private static final int HORIZONTAL_MEDIUM_PANEL_SIZE = 310;

    /**
     * Horizontal size of gap between label and data
     */
    private static final int HORIZONTAL_DATA_GAP_SIZE = 10;

    /**
     * Horizontal button gap size
     */
    private static final int HORIZONTAL_BUTTON_GAP_SIZE = 32;

    /**
     * Horizontal size of gap between button
     */
    private static final int HORIZONTAL_BUTTONS_GAP_SIZE = 54;

    /**
     * Horizontal gap size
     */
    private static final int HORIZONTAL_GAP_SIZE = 20;

    /**
     * Vertical gap size
     */
    private static final int VERTICAL_GAP_SIZE = 20;

    /**
     * Return status
     */
    private DialogResult returnStatus = DialogResult.CANCEL;

    /**
     * List of media
     */
    private List<Integer> media;

    /**
     * Label for media count
     */
    private JLabel mediaCountLabel = new JLabel("Media count");

    /**
     * Spinner for media count
     */
    private JSpinner mediaCountData = new JSpinner(new SpinnerNumberModel(1, 1, MAX_MEDIA_COUNT, 1));

    /**
     * Media panels
     */
    private List<TimeDataPanel> mediaPanels = new ArrayList<>();

    /**
     * Button OK
     */
    private JButton okButton = new JButton("OK", Picture.OK.getIcon());

    /**
     * Button Cancel
     */
    private JButton cancelButton = new JButton("Cancel", Picture.CANCEL.getIcon());

    /**
     * Creates a new instance of MediaChooseDialog.
     *
     * @param media media
     * @throws IllegalArgumentException if list of media is null
     */
    public MediaChooseDialog(final List<Integer> media) {
        super(new JFrame(), "Choose", true);

        Validators.validateArgumentNotNull(media, "List of media");

        this.media = media;
        initComponents();
        setIconImage(Picture.CHOOSE.getIcon().getImage());
    }

    /**
     * Returns return status.
     *
     * @return return status
     */
    public DialogResult getReturnStatus() {
        return returnStatus;
    }

    /**
     * Returns list of media.
     *
     * @return list of media
     * @throws IllegalStateException if list of media for hasn't been set
     */
    public List<Integer> getMedia() {
        Validators.validateFieldNotNull(media, "List of media");

        return media;
    }

    /**
     * Initializes components.
     */
    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        for (int i = 1; i <= MAX_MEDIA_COUNT; i++) {
            final TimeDataPanel mediumPanel = new TimeDataPanel("Medium " + i);
            mediumPanel.setVisible(i == 1);
            mediaPanels.add(mediumPanel);
        }

        mediaCountData.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent e) {
                final int mediaCount = (Integer) mediaCountData.getValue();
                for (int i = 0; i < mediaPanels.size(); i++) {
                    mediaPanels.get(i).setVisible(i < mediaCount);
                    pack();
                }
            }

        });

        if (media.isEmpty()) {
            mediaCountData.setValue(1);
        } else {
            mediaCountData.setValue(media.size());
            for (int i = 0; i < media.size(); i++) {
                mediaPanels.get(i).setLength(new Time(media.get(i)));
            }
        }

        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                okAction();
            }

        });

        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                cancelAction();
            }

        });

        final GroupLayout layout = new GroupLayout(getRootPane());
        getRootPane().setLayout(layout);
        layout.setHorizontalGroup(createHorizontalLayout(layout));
        layout.setVerticalGroup(createVerticalLayout(layout));

        pack();
        setLocationRelativeTo(getRootPane());
    }

    /**
     * Performs action for button OK.
     */
    private void okAction() {
        returnStatus = DialogResult.OK;
        media.clear();
        final int mediaCount = (Integer) mediaCountData.getValue();
        for (int i = 0; i < mediaCount; i++) {
            media.add(mediaPanels.get(i).getLength().getLength());
        }
        close();
    }

    /**
     * Performs action for button Cancel.
     */
    private void cancelAction() {
        returnStatus = DialogResult.CANCEL;
        media = null;
        close();
    }

    /**
     * Closes dialog.
     */
    private void close() {
        setVisible(false);
        dispose();
    }

    /**
     * Returns horizontal layout of components.
     *
     * @param layout layout
     * @return horizontal layout of components
     */
    private GroupLayout.Group createHorizontalLayout(final GroupLayout layout) {
        final GroupLayout.Group buttons = layout.createSequentialGroup()
                .addGap(HORIZONTAL_BUTTON_GAP_SIZE)
                .addComponent(okButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE)
                .addGap(HORIZONTAL_BUTTONS_GAP_SIZE)
                .addComponent(cancelButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE)
                .addGap(HORIZONTAL_BUTTON_GAP_SIZE);


        final GroupLayout.Group components = layout.createParallelGroup()
                .addGroup(createHorizontalComponents(layout, mediaCountLabel, mediaCountData));
        for (final TimeDataPanel mediumPanel : mediaPanels) {
            components.addComponent(mediumPanel, HORIZONTAL_MEDIUM_PANEL_SIZE, HORIZONTAL_MEDIUM_PANEL_SIZE, HORIZONTAL_MEDIUM_PANEL_SIZE);
        }
        components.addGroup(buttons);

        return layout.createSequentialGroup()
                .addGap(HORIZONTAL_GAP_SIZE)
                .addGroup(components)
                .addGap(HORIZONTAL_GAP_SIZE);
    }

    /**
     * Returns horizontal layout for label component with data component.
     *
     * @param layout         layout
     * @param labelComponent label component
     * @param dataComponent  data component
     * @return horizontal layout for label component with data component
     */
    private GroupLayout.Group createHorizontalComponents(final GroupLayout layout, final JComponent labelComponent, final JComponent dataComponent) {
        return layout.createSequentialGroup()
                .addComponent(labelComponent, HORIZONTAL_LABEL_DIALOG_SIZE, HORIZONTAL_LABEL_DIALOG_SIZE, HORIZONTAL_LABEL_DIALOG_SIZE)
                .addGap(HORIZONTAL_DATA_GAP_SIZE)
                .addComponent(dataComponent, HORIZONTAL_DATA_DIALOG_SIZE, HORIZONTAL_DATA_DIALOG_SIZE, HORIZONTAL_DATA_DIALOG_SIZE);
    }

    /**
     * Returns vertical layout of components.
     *
     * @param layout layout
     * @return vertical layout of components
     */
    private GroupLayout.Group createVerticalLayout(final GroupLayout layout) {
        final GroupLayout.Group buttons = layout.createParallelGroup()
                .addComponent(okButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstants.VERTICAL_BUTTON_SIZE)
                .addComponent(cancelButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstants.VERTICAL_BUTTON_SIZE);

        final GroupLayout.Group components = layout.createSequentialGroup()
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, mediaCountLabel, mediaCountData))
                .addGap(VERTICAL_GAP_SIZE);
        for (final TimeDataPanel mediumPanel : mediaPanels) {
            components.addComponent(mediumPanel, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                    CatalogSwingConstants.VERTICAL_BUTTON_SIZE);
        }
        components.addGap(VERTICAL_GAP_SIZE)
                .addGroup(buttons)
                .addGap(VERTICAL_GAP_SIZE);

        return components;
    }

    /**
     * Returns vertical layout for label component with data component.
     *
     * @param layout         layout
     * @param labelComponent label component
     * @param dataComponent  data component
     * @return vertical layout for label component with data component
     */
    private GroupLayout.Group createVerticalComponents(final GroupLayout layout, final JComponent labelComponent, final JComponent dataComponent) {
        return layout.createParallelGroup()
                .addComponent(labelComponent, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addComponent(dataComponent, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE);
    }

}
