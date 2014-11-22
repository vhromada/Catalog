package cz.vhromada.catalog.gui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import cz.vhromada.validators.Validators;

/**
 * A class represents pictures.
 *
 * @author Vladimir Hromada
 */
public final class Pictures {

    /** Pictures (Map: Name -> Picture) */
    private static final Map<String, ImageIcon> PICTURES;

    static {
        PICTURES = new HashMap<>(11);
        PICTURES.put("about", new ImageIcon("pics/about.jpg"));
        PICTURES.put("add", new ImageIcon("pics/add.jpg"));
        PICTURES.put("cancel", new ImageIcon("pics/cancel.jpg"));
        PICTURES.put("catalog", new ImageIcon("pics/catalog.jpg"));
        PICTURES.put("down", new ImageIcon("pics/down.jpg"));
        PICTURES.put("duplicate", new ImageIcon("pics/duplicate.jpg"));
        PICTURES.put("exit", new ImageIcon("pics/exit.jpg"));
        PICTURES.put("new", new ImageIcon("pics/new.jpg"));
        PICTURES.put("ok", new ImageIcon("pics/ok.jpg"));
        PICTURES.put("remove", new ImageIcon("pics/remove.jpg"));
        PICTURES.put("save", new ImageIcon("pics/save.jpg"));
        PICTURES.put("up", new ImageIcon("pics/up.jpg"));
        PICTURES.put("update", new ImageIcon("pics/update.jpg"));
    }

    /** Creates a new instance of Pictures. */
    private Pictures() {
    }

    /**
     * Returns picture.
     *
     * @param name picture's name
     * @return picture
     * @throws IllegalArgumentException if picture's name is null
     */
    public static ImageIcon getPicture(final String name) {
        Validators.validateArgumentNotNull(name, "Picture's name");

        return PICTURES.get(name);
    }

}
