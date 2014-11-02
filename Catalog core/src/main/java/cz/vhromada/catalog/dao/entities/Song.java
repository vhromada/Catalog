package cz.vhromada.catalog.dao.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * A class represents song.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "songs")
@NamedQuery(name = Song.FIND_BY_MUSIC, query = "SELECT s FROM Song s WHERE s.music.id = :music ORDER BY position, id")
public class Song implements Serializable {

	/** Name for query - find by music */
	public static final String FIND_BY_MUSIC = "Song.findByMusic";

	/** SerialVersionUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@SequenceGenerator(name = "song_generator", sequenceName = "songs_sq", allocationSize = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "song_generator")
	private Integer id;

	/** Name */
	@Column(name = "song_name")
	private String name;

	/** Length */
	@Column(name = "song_length")
	private int length;

	/** Note */
	private String note;

	/** Position */
	private int position;

	/** Music */
	@ManyToOne
	@JoinColumn(name = "music", referencedColumnName = "id")
	private Music music;

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
	 * Returns length.
	 *
	 * @return length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Sets a new value to length.
	 *
	 * @param length new value
	 */
	public void setLength(final int length) {
		this.length = length;
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

	/**
	 * Returns music.
	 *
	 * @return music
	 */
	public Music getMusic() {
		return music;
	}

	/**
	 * Sets a new value to music.
	 *
	 * @param music new value
	 */
	public void setMusic(final Music music) {
		this.music = music;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof Song) || id == null) {
			return false;
		}
		final Song song = (Song) obj;
		return id.equals(song.id);
	}

	@Override
	public int hashCode() {
		return id == null ? 0 : id.hashCode();
	}

	@Override
	public String toString() {
		return String.format("Song [id=%d, name=%s, length=%d, note=%s, position=%d, music=%s]", id, name, length, note, position, music);
	}

}
