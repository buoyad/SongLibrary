/**
 * @author Daniel Ayoub, Marcus Lomi
 */

package model;

import java.io.Serializable;

public class Song implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1706299580039023335L;

	public String key; // For hashmap?
	
	public String name;
	public String artist;
	public String album;
	public int year;
	
	public Song(String name, String artist, String album, int year) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
		
		this.key = (name + artist).toLowerCase();
	}

}
