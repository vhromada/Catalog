package cz.vhromada.catalog.gui.commons;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * A class represents table cell renderer for changing alignment of the cell.
 *
 * @author Vladimir Hromada
 */
public class StatsTableCellRenderer extends DefaultTableCellRenderer {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of StatsTableCellRenderer.
     */
    public StatsTableCellRenderer() {
        setVerticalAlignment(CENTER);
        setHorizontalAlignment(CENTER);
    }

}
