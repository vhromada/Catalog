package cz.vhromada.catalog.gui.programs;

import java.util.List;

import javax.swing.*;

import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for list with programs.
 *
 * @author Vladimir Hromada
 */
public class ProgramsListDataModel extends AbstractListModel<String> {

	/** SerialVersionUID */
	private static final long serialVersionUID = 1L;

	/** Facade for programs */
	private ProgramFacade programFacade;

	/** List of TO for program */
	private List<ProgramTO> programs;

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

	/**
	 * Returns the length of the list.
	 *
	 * @return the length of the list
	 */
	@Override
	public int getSize() {
		return programs.size();
	}

	/**
	 * Returns the value at the specified index.
	 *
	 * @param index the requested index
	 * @return the value at index
	 */
	@Override
	public String getElementAt(final int index) {
		return getProgramAt(index).getName();
	}

	/**
	 * Returns TO for program at the specified index.
	 *
	 * @param index the requested index
	 * @return TO for program at index
	 */
	public ProgramTO getProgramAt(final int index) {
		return programs.get(index);
	}

	/** Updates model. */
	public final void update() {
		programs = programFacade.getPrograms();
	}

}
