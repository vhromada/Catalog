package cz.vhromada.catalog.gui.music;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for table with stats for music.
 *
 * @author Vladimir Hromada
 */
public class MusicStatsTableDataModel extends AbstractTableModel {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Error message for bad column
     */
    private static final String BAD_COLUMN_ERROR_MESSAGE = "Bad column";

    /**
     * Facade for music
     */
    private MusicFacade musicFacade;

    /**
     * List of TO for music
     */
    private List<MusicTO> musicList;

    /**
     * Total count of media
     */
    private int totalMediaCount;

    /**
     * Cunt of songs from all music
     */
    private int songsCount;

    /**
     * Total length of all songs
     */
    private Time totalLength;

    /**
     * Creates a new instance of MusicStatsTableDataModel.
     *
     * @param musicFacade facade for music
     * @throws IllegalArgumentException if facade for music is null
     */
    public MusicStatsTableDataModel(final MusicFacade musicFacade) {
        Validators.validateArgumentNotNull(musicFacade, "Facade for music");

        this.musicFacade = musicFacade;
        update();
    }

    /**
     * Returns the value for the cell at columnIndex and rowIndex.
     *
     * @param rowIndex    the row whose value is to be queried
     * @param columnIndex the column whose value is to be queried
     * @return the value at the specified cell
     */
    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        switch (columnIndex) {
            case 0:
                return musicList.size();
            case 1:
                return totalMediaCount;
            case 2:
                return songsCount;
            case 3:
                return totalLength.toString();
            default:
                throw new IndexOutOfBoundsException(BAD_COLUMN_ERROR_MESSAGE);
        }
    }

    /**
     * Returns the number of columns in the model.
     *
     * @return the number of columns in the model
     */
    @Override
    public int getColumnCount() {
        return 4;
    }

    /**
     * Returns the number of rows in the model.
     *
     * @return the number of rows in the model
     */
    @Override
    public int getRowCount() {
        return 1;
    }

    /**
     * Returns class of data regardless of columnIndex.
     *
     * @param columnIndex the column being queried
     * @return class of data
     */
    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
                return Integer.class;
            case 2:
                return Integer.class;
            case 3:
                return String.class;
            default:
                throw new IndexOutOfBoundsException(BAD_COLUMN_ERROR_MESSAGE);
        }
    }

    /**
     * Returns a default name for the column.
     *
     * @param column the column being queried
     * @return a string containing the default name of column
     */
    @Override
    public String getColumnName(final int column) {
        switch (column) {
            case 0:
                return "Count of music";
            case 1:
                return "Count of media";
            case 2:
                return "Count of songs";
            case 3:
                return "Total length";
            default:
                throw new IndexOutOfBoundsException(BAD_COLUMN_ERROR_MESSAGE);
        }
    }

    /**
     * Updates model.
     */
    public final void update() {
        musicList = musicFacade.getMusic();
        totalMediaCount = musicFacade.getTotalMediaCount();
        songsCount = musicFacade.getSongsCount();
        totalLength = musicFacade.getTotalLength();
    }

}
