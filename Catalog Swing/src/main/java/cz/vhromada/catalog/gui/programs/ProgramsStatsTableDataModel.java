package cz.vhromada.catalog.gui.programs;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for table with stats for programs.
 *
 * @author Vladimir Hromada
 */
public class ProgramsStatsTableDataModel extends AbstractTableModel {

	/** SerialVersionUID */
	private static final long serialVersionUID = 1L;

	/** Facade for programs */
	private ProgramFacade programFacade;

	/** List of TO for program */
	private List<ProgramTO> programs;

	/** Total count of media */
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
				return programs.size();
			case 1:
				return totalMediaCount;
			default:
				throw new IndexOutOfBoundsException("Bad column");
		}
	}

	/**
	 * Returns the number of columns in the model.
	 *
	 * @return the number of columns in the model
	 */
	@Override
	public int getColumnCount() {
		return 2;
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
		return Integer.class;
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
				return "Count of programs";
			case 1:
				return "Count of media";
			default:
				throw new IndexOutOfBoundsException("Bad column");
		}
	}

	/** Updates model. */
	public final void update() {
		programs = programFacade.getPrograms();
		totalMediaCount = programFacade.getTotalMediaCount();
	}

}
