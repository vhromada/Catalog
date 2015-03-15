package cz.vhromada.catalog.gui.commons;

import javax.swing.ImageIcon;

/**
 * An enumeration represents picture.
 *
 * @author Vladimir Hromada
 */
public enum Picture {

    ABOUT(new ImageIcon("pics/about.jpg")),
    ADD(new ImageIcon("pics/add.jpg")),
    CANCEL(new ImageIcon("pics/cancel.jpg")),
    CATALOG(new ImageIcon("pics/catalog.jpg")),
    CHOOSE(new ImageIcon("pics/choose.jpg")),
    DOWN(new ImageIcon("pics/down.jpg")),
    DUPLICATE(new ImageIcon("pics/duplicate.jpg")),
    EXIT(new ImageIcon("pics/exit.jpg")),
    NEW(new ImageIcon("pics/new.jpg")),
    OK(new ImageIcon("pics/ok.jpg")),
    REMOVE(new ImageIcon("pics/remove.jpg")),
    SAVE(new ImageIcon("pics/save.jpg")),
    UP(new ImageIcon("pics/up.jpg")),
    UPDATE(new ImageIcon("pics/update.jpg"));

    /**
     * Icon
     */
    private ImageIcon icon;

    /**
     * Creates a new instance of Pictures.
     *
     * @param icon icon
     */
    private Picture(final ImageIcon icon) {
        this.icon = icon;
    }

    /**
     * Returns icon.
     *
     * @return icon
     */
    public ImageIcon getIcon() {
        return icon;
    }

}
