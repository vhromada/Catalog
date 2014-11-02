package cz.vhromada.catalog.facade.to;

import java.io.Serializable;

/**
 * A class represents TO for program.
 *
 * @author Vladimir Hromada
 */
public class ProgramTO implements Comparable<ProgramTO>, Serializable {

	/** SerialVersionUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	private Integer id;

	/** Name */
	private String name;

	/** URL to english Wikipedia page about program */
	private String wikiEn;

	/** URL to czech Wikipedia page about program */
	private String wikiCz;

	/** Count of media */
	private int mediaCount;

	/** True if there is crack */
	private boolean crack;

	/** True if there is serial key */
	private boolean serialKey;

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
	 * Returns URL to english Wikipedia page about program.
	 *
	 * @return URL to english Wikipedia page about program
	 */
	public String getWikiEn() {
		return wikiEn;
	}

	/**
	 * Sets a new value to URL to english Wikipedia page about program.
	 *
	 * @param wikiEn new value
	 */
	public void setWikiEn(final String wikiEn) {
		this.wikiEn = wikiEn;
	}

	/**
	 * Returns URL to czech Wikipedia page about program.
	 *
	 * @return URL to czech Wikipedia page about program
	 */
	public String getWikiCz() {
		return wikiCz;
	}

	/**
	 * Sets a new value to URL to czech Wikipedia page about program.
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
		if (serialKey) {
			if (result.length() == 0) {
				result.append("Serial key");
			} else {
				result.append(", serial key");
			}
		}
		if (otherData != null && !otherData.isEmpty()) {
			if (result.length() != 0) {
				result.append(", ");
			}
			result.append(otherData);
		}

		return result.toString();
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
		if (obj == null || !(obj instanceof ProgramTO) || id == null) {
			return false;
		}
		final ProgramTO program = (ProgramTO) obj;
		return id.equals(program.id);
	}

	@Override
	public int hashCode() {
		return id == null ? 0 : id.hashCode();
	}

	@Override
	public String toString() {
		return String.format("ProgramTO [id=%d, name=%s, wikiEn=%s, wikiCz=%s, mediaCount=%d, crack=%b, serialKey=%b, otherData=%s, note=%s, position=%d]", id,
				name, wikiEn, wikiCz, mediaCount, crack, serialKey, otherData, note, position);
	}

	@Override
	public int compareTo(final ProgramTO o) {
		final int result = position - o.position;
		return result == 0 ? id - o.id : result;
	}

}
