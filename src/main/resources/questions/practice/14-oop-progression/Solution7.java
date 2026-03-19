import java.util.*;

public class Solution7 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String playlistName = sc.next();
        int n = sc.nextInt();

        Playlist p = new Playlist(playlistName);
        for (int i = 0; i < n; i++) {
            String title = sc.next();
            int duration = sc.nextInt();
            p.addSong(new Song(title, duration));
        }

        String removeTitle = sc.next();
        p.removeSong(removeTitle);

        p.printSongs();
        System.out.println("Total " + p.totalDuration());
        sc.close();
    }
}

class Song {
    private String title;
    private int duration;

    public Song(String title, int duration) {
        this.title = title;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }
}

class Playlist {
    private String name;
    private ArrayList<Song> songs;

    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
    }

    public void addSong(Song s) {
        for (Song song : songs) {
            if (song.getTitle().equals(s.getTitle())) {
                return;
            }
        }
        songs.add(s);
    }

    public void removeSong(String title) {
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getTitle().equals(title)) {
                songs.remove(i);
                return;
            }
        }
    }

    public int totalDuration() {
        int total = 0;
        for (Song song : songs) {
            total += song.getDuration();
        }
        return total;
    }

    public void printSongs() {
        for (Song song : songs) {
            System.out.println(song.getTitle() + "(" + song.getDuration() + ")");
        }
    }
}
