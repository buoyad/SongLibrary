package model;

public class Song {
	
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
