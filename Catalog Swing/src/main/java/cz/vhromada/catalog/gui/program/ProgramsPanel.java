package cz.vhromada.catalog.gui.program;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.catalog.gui.commons.AbstractInfoDialog;
import cz.vhromada.catalog.gui.commons.AbstractOverviewDataPanel;

/**
 * A class represents panel with programs' data.
 *
 * @author Vladimir Hromada
 */
public class ProgramsPanel extends AbstractOverviewDataPanel<ProgramTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for programs
     */
    private ProgramFacade programFacade;

    /**
     * Creates a new instance of ProgramsPanel.
     *
     * @param programFacade facade for programs
     * @throws IllegalArgumentException if facade for programs is null
     */
    public ProgramsPanel(final ProgramFacade programFacade) {
        super(getProgramListDataModel(programFacade), getProgramsStatsTableDataModel(programFacade));

        this.programFacade = programFacade;
    }

    @Override
    protected AbstractInfoDialog<ProgramTO> getInfoDialog(final boolean add, final ProgramTO data) {
        return add ? new ProgramInfoDialog() : new ProgramInfoDialog(data);
    }

    @Override
    protected void deleteData() {
        programFacade.newData();
    }

    @Override
    protected void addData(final ProgramTO data) {
        programFacade.add(data);
    }

    @Override
    protected void updateData(final ProgramTO data) {
        programFacade.update(data);
    }

    @Override
    protected void removeData(final ProgramTO data) {
        programFacade.remove(data);
    }

    @Override
    protected void duplicatesData(final ProgramTO data) {
        programFacade.duplicate(data);
    }

    @Override
    protected void moveUpData(final ProgramTO data) {
        programFacade.moveUp(data);
    }

    @Override
    protected void moveDownData(final ProgramTO data) {
        programFacade.moveDown(data);
    }

    @Override
    protected JPanel getDataPanel(final ProgramTO data) {
        return new ProgramDataPanel(data);
    }

    @Override
    protected void updateDataOnChange(final JTabbedPane dataPanel, final ProgramTO data) {
    }

    /**
     * Returns data model for list with programs.
     *
     * @param facade facade for programs
     * @return data model for list with programs
     * @throws IllegalArgumentException if facade for programs is null
     */
    private static ProgramsListDataModel getProgramListDataModel(final ProgramFacade facade) {
        return new ProgramsListDataModel(facade);
    }

    /**
     * Returns data model for table with stats for programs.
     *
     * @param facade facade for programs
     * @return data model for table with stats for programs
     * @throws IllegalArgumentException if facade for programs is null
     */
    private static ProgramsStatsTableDataModel getProgramsStatsTableDataModel(final ProgramFacade facade) {
        return new ProgramsStatsTableDataModel(facade);
    }

}
