package cz.vhromada.catalog.gui.commons;

import javax.swing.table.AbstractTableModel;

/**
 * An abstract class represents data model for table with stats.
 *
 * @param <T> type of data
 * @author Vladimir Hromada
 */
public abstract class AbstractStatsTableDataModel<T> extends AbstractTableModel {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Updates model.
     */
    public abstract void update();

}
