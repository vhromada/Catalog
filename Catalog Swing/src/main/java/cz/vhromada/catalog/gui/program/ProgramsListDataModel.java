package cz.vhromada.catalog.gui.program;

import java.util.List;

import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.catalog.gui.commons.AbstractListDataModel;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for list with programs.
 *
 * @author Vladimir Hromada
 */
public class ProgramsListDataModel extends AbstractListDataModel<ProgramTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for programs
     */
    private ProgramFacade programFacade;

    /**
     * Creates a new instance of ProgramsListDataModel.
     *
     * @param programFacade facade for programs
     * @throws IllegalArgumentException if facade for programs is null
     */
    public ProgramsListDataModel(final ProgramFacade programFacade) {
        Validators.validateArgumentNotNull(programFacade, "Facade for programs");

        this.programFacade = programFacade;
        update();
    }

    @Override
    public String getElementAt(final int index) {
        return getObjectAt(index).getName();
    }

    @Override
    protected List<ProgramTO> getData() {
        return programFacade.getPrograms();
    }

}
