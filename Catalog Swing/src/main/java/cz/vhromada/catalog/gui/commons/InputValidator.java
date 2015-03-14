package cz.vhromada.catalog.gui.commons;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import cz.vhromada.validators.Validators;

/**
 * An abstract class represents input validator.
 *
 * @author Vladimir Hromada
 */
public abstract class InputValidator implements DocumentListener {

    /** Button */
    private JButton button;

    /**
     * Creates a new instance of InputValidator.
     *
     * @param button button
     * @throws IllegalArgumentException if button is null
     */
    public InputValidator(final JButton button) {
        Validators.validateArgumentNotNull(button, "Button");

        this.button = button;
    }

    /**
     * Gives notification that there was an insert into the document.
     *
     * @param e the document event
     */
    @Override
    public void insertUpdate(final DocumentEvent e) {
        button.setEnabled(isInputValid());
    }

    /**
     * Gives notification that a portion of the document has been removed.
     *
     * @param e the document event
     */
    @Override
    public void removeUpdate(final DocumentEvent e) {
        button.setEnabled(isInputValid());
    }

    /**
     * Gives notification that an attribute or set of attributes changed.
     *
     * @param e the document event
     */
    @Override
    public void changedUpdate(final DocumentEvent e) {
        button.setEnabled(isInputValid());
    }

    /**
     * Returns true if input is valid.
     *
     * @return true if input is valid
     */
    public abstract boolean isInputValid();

}
