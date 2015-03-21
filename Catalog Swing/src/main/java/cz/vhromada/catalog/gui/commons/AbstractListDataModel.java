package cz.vhromada.catalog.gui.commons;

import java.util.List;

import javax.swing.AbstractListModel;

/**
 * An abstract class represents data model for list with data.
 *
 * @param <T> type of data
 * @author Vladimir Hromada
 */
public abstract class AbstractListDataModel<T> extends AbstractListModel<String> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * List of data
     */
    private List<T> data;

    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public String getElementAt(final int index) {
        return getDisplayValue(getObjectAt(index));
    }

    /**
     * Returns data object at the specified index.
     *
     * @param index the requested index
     * @return data object at index
     */
    public T getObjectAt(final int index) {
        return data.get(index);
    }

    /**
     * Updates model.
     */
    public final void update() {
        data = getData();
    }

    /**
     * Returns data.
     *
     * @return data.
     */
    protected abstract List<T> getData();

    /**
     * Returns display value for data.
     *
     * @param dataObject data
     * @return display value for data
     */
    protected abstract String getDisplayValue(final T dataObject);

}
