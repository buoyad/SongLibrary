package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.BiConsumer;

public class SongList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7811596000577847026L;
	public HashMap<String, Song> list;

	/**
	 * Makes a new songliiiist
	 */
	public SongList() {
		this.list = new HashMap<String, Song>();
	}

	/**
	 * Creates a new song and adds it to the SongList
	 * @throws DuplicateSongException if the song is already in the list
	 */
	public void addSong(String name, String artist, String album, int year) throws DuplicateSongException {
		Song s = new Song(name, artist, album, year);
		this.addSong(s);
	}

	/**
	 * Adds a new song to the SongList
	 * @param s
	 * @throws DuplicateSongException
	 */
	public void addSong(Song s) throws DuplicateSongException {
		if (list.containsKey(s.key)) {
			throw new DuplicateSongException("A song with that name already exists.");
		}
		list.put(s.key, s);
	}

	/**
	 * Updates a Song that is already in the SongList
	 * @param s The Song to be updated
	 * @param oldKey Optional parameter; 
	 * 			if the Songs name / artist was changed,
	 * 			the old key should be supplied here (for removal)
	 * 			(this is probably bad)
	 */
	public void updateSong(Song s) throws DuplicateSongException {
		list.remove(s.key);
		list.put(s.key, s);
	}

	/**
	 * Removes a song from the list
	 * @param key The key for the target song
	 */
	public void removeSong(String key) {
		if (!list.containsKey(key)) {
			throw new IllegalArgumentException("That song does not exist in the list.");
		}
		list.remove(key);
	}

	public String[] getSongTitles() {
		Song[] songs = getSongs();
		String[] songTitles = new String[songs.length];
		for (int i = 0; i < songs.length; i++) {
			songTitles[i] = songs[i].name;
		}
		return songTitles;
//		ArrayList<String> songTitles = new ArrayList<String>();
//
//		list.forEach(new BiConsumer<String, Song>() {
//			public void accept(String key, Song s) {
//				String n = s.name;
//				String k = s.key;
//
//				for (int i = 0; i <= songTitles.size(); i++) {
//					if (i == songTitles.size()) {
//						songTitles.add(i, n);
//						break;
//					}
//					else if (songTitles.get(i).compareToIgnoreCase(n) >= 0) {
//						songTitles.add(i, n);
//						break;
//					}
//				}				
//			}
//		});
//
//		return songTitles.toArray(new String[songTitles.size()]);
	}
	
	public Song[] getSongs() {
		ArrayList<Song> songs = new ArrayList<Song>();
		
		list.forEach(new BiConsumer<String, Song>() {
			public void accept(String key, Song s) {
				String n = s.name;
				String k = s.key;

				for (int i = 0; i <= songs.size(); i++) {
					if (i == songs.size()) {
						songs.add(i, s);
						break;
					}
					else if (songs.get(i).key.compareToIgnoreCase(k) >= 0) {
						songs.add(i, s);
						break;
					}
				}				
			}
		});
		
		return songs.toArray(new Song[songs.size()]);
	}

	public class DuplicateSongException extends IllegalArgumentException {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7366773116572300319L;

		public DuplicateSongException(String e) {
			super(e);
		}
	}

}
