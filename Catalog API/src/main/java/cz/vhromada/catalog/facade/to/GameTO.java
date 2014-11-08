package cz.vhromada.catalog.facade.to;

import java.io.Serializable;

/**
 * A class represents TO for game.
 *
 * @author Vladimir Hromada
 */
public class GameTO implements Comparable<GameTO>, Serializable {

	/** SerialVersionUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	private Integer id;

	/** Name */
	private String name;

	/** URL to english Wikipedia page about game */
	private String wikiEn;

	/** URL to czech Wikipedia page about game */
	private String wikiCz;

	/** Count of media */
	private int mediaCount;

	/** True if there is crack */
	private boolean crack;

	/** True if there is serial key */
	private boolean serialKey;

	/** True if there is patch */
	private boolean patch;

	/** True if there is trainer */
	private boolean trainer;

	/** True if there is data for trainer */
	private boolean trainerData;

	/** True if there is editor */
	private boolean editor;

	/** True if there are saves */
	private boolean saves;

	/** Other data */
	private String otherData;

	/** Note */
	private String note;

	/** Position */
	private int position;

	/**
	 * Returns ID.
	 *
	 * @return ID
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets a new value to ID.
	 *
	 * @param id new value
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * Returns name.
	 *
	 * @return name
	 */

	public String getName() {
		return name;
	}

	/**
	 * Sets a new value to name.
	 *
	 * @param name new value
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Returns URL to english Wikipedia page about game.
	 *
	 * @return URL to english Wikipedia page about game
	 */
	public String getWikiEn() {
		return wikiEn;
	}

	/**
	 * Sets a new value to URL to english Wikipedia page about game.
	 *
	 * @param wikiEn new value
	 */
	public void setWikiEn(final String wikiEn) {
		this.wikiEn = wikiEn;
	}

	/**
	 * Returns URL to czech Wikipedia page about game.
	 *
	 * @return URL to czech Wikipedia page about game
	 */
	public String getWikiCz() {
		return wikiCz;
	}

	/**
	 * Sets a new value to URL to czech Wikipedia page about game.
	 *
	 * @param wikiCz new value
	 */
	public void setWikiCz(final String wikiCz) {
		this.wikiCz = wikiCz;
	}

	/**
	 * Returns count of media.
	 *
	 * @return count of media
	 */
	public int getMediaCount() {
		return mediaCount;
	}

	/**
	 * Sets a new value to count of media.
	 *
	 * @param mediaCount new value
	 * @throws IllegalArgumentException if new value isn't positive number
	 */
	public void setMediaCount(final int mediaCount) {
		this.mediaCount = mediaCount;
	}

	/**
	 * Returns true if there is crack.
	 *
	 * @return true if there is crack
	 */
	public boolean hasCrack() {
		return crack;
	}

	/**
	 * Sets a new value to if there is crack.
	 *
	 * @param crack new value
	 */
	public void setCrack(final boolean crack) {
		this.crack = crack;
	}

	/**
	 * Returns true if there is serial key.
	 *
	 * @return true if there is serial key
	 */
	public boolean hasSerialKey() {
		return serialKey;
	}

	/**
	 * Sets a new value to if there is serial key.
	 *
	 * @param serialKey new value
	 */
	public void setSerialKey(final boolean serialKey) {
		this.serialKey = serialKey;
	}

	/**
	 * Returns true if there is patch.
	 *
	 * @return true if there is patch
	 */
	public boolean hasPatch() {
		return patch;
	}

	/**
	 * Sets a new value to if there is patch.
	 *
	 * @param patch new value
	 */
	public void setPatch(final boolean patch) {
		this.patch = patch;
	}

	/**
	 * Returns true if there is trainer.
	 *
	 * @return true if there is trainer
	 */
	public boolean hasTrainer() {
		return trainer;
	}

	/**
	 * Sets a new value to if there is trainer.
	 *
	 * @param trainer new value
	 */
	public void setTrainer(final boolean trainer) {
		this.trainer = trainer;
	}

	/**
	 * Returns true if there is data for trainer.
	 *
	 * @return true if there is data for trainer
	 */
	public boolean hasTrainerData() {
		return trainerData;
	}

	/**
	 * Sets a new value to if there is data for trainer.
	 *
	 * @param trainerData new value
	 */
	public void setTrainerData(final boolean trainerData) {
		this.trainerData = trainerData;
	}

	/**
	 * Returns true if there is editor.
	 *
	 * @return true if there is editor
	 */
	public boolean hasEditor() {
		return editor;
	}

	/**
	 * Sets a new value to if there is editor.
	 *
	 * @param editor new value
	 */
	public void setEditor(final boolean editor) {
		this.editor = editor;
	}

	/**
	 * Returns true if there are saves.
	 *
	 * @return true if there are saves
	 */
	public boolean haveSaves() {
		return saves;
	}

	/**
	 * Sets a new value to if there are saves.
	 *
	 * @param saves new value
	 */
	public void setSaves(final boolean saves) {
		this.saves = saves;
	}

	/**
	 * Returns other data.
	 *
	 * @return other data
	 */
	public String getOtherData() {
		return otherData;
	}

	/**
	 * Sets a new value to other data.
	 *
	 * @param otherData new value
	 */
	public void setOtherData(final String otherData) {
		this.otherData = otherData;
	}

	/**
	 * Returns additional data.
	 *
	 * @return additional data
	 */
	public String getAdditionalData() {
		final StringBuilder result = new StringBuilder();
		if (crack) {
			result.append("Crack");
		}
		addToResult(result, serialKey, "serial key");
		addToResult(result, patch, "patch");
		addToResult(result, trainer, "trainer");
		addToResult(result, trainerData, "data for trainer");
		addToResult(result, editor, "editor");
		addToResult(result, saves, "saves");
		if (otherData != null && !otherData.isEmpty()) {
			if (result.length() != 0) {
				result.append(", ");
			}
			result.append(otherData);
		}

		return result.toString();
	}

	private void addToResult(final StringBuilder result, final boolean value, final String string) {
		if (value) {
			if (result.length() == 0) {
				result.append(string.substring(0, 1).toUpperCase());
				result.append(string.substring(1));
			} else {
				result.append(", ");
				result.append(string);
			}
		}
	}

	/**
	 * Returns note.
	 *
	 * @return note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * Sets a new value to note.
	 *
	 * @param note new value
	 */
	public void setNote(final String note) {
		this.note = note;
	}

	/**
	 * Returns position.
	 *
	 * @return position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Sets a new value to position.
	 *
	 * @param position new value
	 */
	public void setPosition(final int position) {
		this.position = position;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof GameTO) || id == null) {
			return false;
		}
		final GameTO game = (GameTO) obj;
		return id.equals(game.id);
	}

	@Override
	public int hashCode() {
		return id == null ? 0 : id.hashCode();
	}

	@Override
	public String toString() {
		return String.format("GameTO [id=%d, name=%s, wikiEn=%s, wikiCz=%s, mediaCount=%d, crack=%b, serialKey=%b, patch=%b, trainer=%b, trainerData=%b, "
				+ "editor=%b, saves=%b, otherData=%s, note=%s, position=%d]", id, name, wikiEn, wikiCz, mediaCount, crack, serialKey, patch, trainer,
				trainerData, editor, saves, otherData, note, position);
	}

	@Override
	public int compareTo(final GameTO o) {
		final int result = position - o.position;
		return result == 0 ? id - o.id : result;
	}

}