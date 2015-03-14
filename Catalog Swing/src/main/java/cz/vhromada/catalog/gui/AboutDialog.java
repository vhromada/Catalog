package cz.vhromada.catalog.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import cz.vhromada.catalog.commons.CatalogSwingConstants;

/**
 * A class represents dialog about.
 *
 * @author Vladimir Hromada
 */
public class AboutDialog extends JDialog {

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Horizontal label size */
    private static final int HORIZONTAL_LABEL_SIZE = 200;

    /** Horizontal button size */
    private static final int HORIZONTAL_BUTTON_SIZE = 76;

    /** Horizontal button gap size */
    private static final int HORIZONTAL_BUTTON_GAP_SIZE = 62;

    /** Horizontal gap size */
    private static final int HORIZONTAL_GAP_SIZE = 20;

    /** Vertical gap size */
    private static final int VERTICAL_GAP_SIZE = 20;

    /** Vertical long gap size */
    private static final int VERTICAL_LONG_GAP_SIZE = 40;

    /** Font size */
    private static final int FONT_SIZE = 12;

    /** Label for name */
    private JLabel nameLabel = new JLabel("Catalog", Picture.CATALOG.getIcon(), SwingConstants.LEADING);

    /** Label for version */
    private JLabel versionLabel = new JLabel("Version: 1.0.0");

    /** Label for author */
    private JLabel authorLabel = new JLabel("Author: Vladimír Hromada");

    /** Label for copyrights */
    private JLabel rightsLabel = new JLabel("All rights reserved.");

    /** Button OK */
    private JButton okButton = new JButton("OK", Picture.OK.getIcon());

    /** Creates a new instance of AboutDialog. */
    public AboutDialog() {
        super(new JFrame(), "About", true);

        setIconImage(Picture.ABOUT.getIcon().getImage());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        final Font font = new Font("Tahoma", Font.BOLD, FONT_SIZE);

        initLabels(font, nameLabel, versionLabel, authorLabel, rightsLabel);

        okButton.setFont(font);
        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                setVisible(false);
                dispose();
            }

        });

        final GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(createHorizontalLayout(layout));
        layout.setVerticalGroup(createVerticalLayout(layout));

        okButton.requestFocusInWindow();
        pack();
        setLocationRelativeTo(getRootPane());
    }

    /**
     * Initializes labels.
     *
     * @param font   font
     * @param labels labels
     */
    private static void initLabels(final Font font, final JLabel... labels) {
        for (final JLabel label : labels) {
            label.setFocusable(false);
            label.setFont(font);
        }
    }

    /**
     * Returns horizontal layout of components.
     *
     * @param layout layout
     * @return horizontal layout of components
     */
    private GroupLayout.SequentialGroup createHorizontalLayout(final GroupLayout layout) {
        final GroupLayout.ParallelGroup labels = layout.createParallelGroup()
                .addComponent(nameLabel, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE)
                .addComponent(versionLabel, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE)
                .addComponent(authorLabel, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE)
                .addComponent(rightsLabel, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE);

        final GroupLayout.SequentialGroup button = layout.createSequentialGroup()
                .addGap(HORIZONTAL_BUTTON_GAP_SIZE)
                .addComponent(okButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE);

        final GroupLayout.ParallelGroup components = layout.createParallelGroup()
                .addGroup(labels)
                .addGroup(button);

        return layout.createSequentialGroup()
                .addGap(HORIZONTAL_GAP_SIZE)
                .addGroup(components)
                .addGap(HORIZONTAL_GAP_SIZE);
    }

    /**
     * Returns vertical layout of components.
     *
     * @param layout layout
     * @return vertical layout of components
     */
    private GroupLayout.SequentialGroup createVerticalLayout(final GroupLayout layout) {
        return layout.createSequentialGroup()
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(nameLabel, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(versionLabel, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(authorLabel, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(rightsLabel, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addGap(VERTICAL_LONG_GAP_SIZE)
                .addComponent(okButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstants.VERTICAL_BUTTON_SIZE)
                .addGap(VERTICAL_GAP_SIZE);
    }

}
