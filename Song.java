package MusicPlayerApplication;

//Kacper Rajchel :: 16/06/2020

public class Song 
{
    private String artist;
    private String album;
    private String name;
    private String title;
    private String path;
    private int trackNumber;
    private int year;

    public Song(String artist, String album, String name, String title, int trackNumber, int year)
    {
        this.artist = artist;
        this.album = album;
        this.name = name;
        this.title = title;
        this.trackNumber = trackNumber;
        this.year = year;
    }

    public Song(String artist, String album, String title, int year)
    {
        this.artist = artist;
        this.album = album;
        this.title = title;
        this.year = year;
    }

    public Song()
    {
        
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTrackNumber() {
        return this.trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString()
    {
        return "Song name " + getTitle() + " by " + getArtist() + " from the album " + getAlbum() + " published in the year " + getYear();
    }
}