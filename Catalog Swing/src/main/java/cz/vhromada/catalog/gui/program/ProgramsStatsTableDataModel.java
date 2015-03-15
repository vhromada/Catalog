package cz.vhromada.catalog.gui.program;

import java.util.List;

import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.catalog.gui.commons.AbstractStatsTableDataModel;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for table with stats for programs.
 *
 * @author Vladimir Hromada
 */
public class ProgramsStatsTableDataModel extends AbstractStatsTableDataModel<ProgramTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for programs
     */
    private ProgramFacade programFacade;

    /**
     * List of TO for program
     */
    private List<ProgramTO> programs;

    /**
     * Total count of media
     */
    private int totalMediaCount;

    /**
     * Creates a new instance of ProgramsStatsTableDataModel.
     *
     * @param programFacade facade for programs
     * @throws IllegalArgumentException if facade for programs is null
     */
    public ProgramsStatsTableDataModel(final ProgramFacade programFacade) {
        Validators.validateArgumentNotNull(programFacade, "Facade for programs");

        this.programFacade = programFacade;
        update();
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        switch (columnIndex) {
            case 0:
                return programs.size();
            case 1:
                return totalMediaCount;
            default:
                throw new IndexOutOfBoundsException("Bad column");
        }
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        return Integer.class;
    }

    @Override
    public String getColumnName(final int column) {
        switch (column) {
            case 0:
                return "Count of programs";
            case 1:
                return "Count of media";
            default:
                throw new IndexOutOfBoundsException("Bad column");
        }
    }

    @Override
    public final void update() {
        programs = programFacade.getPrograms();
        totalMediaCount = programFacade.getTotalMediaCount();
    }

}
