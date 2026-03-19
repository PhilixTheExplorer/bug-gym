## Question 7
Build a small playlist system using composition and `ArrayList` operations.

- `Song` has `title` and `duration` (seconds).
- `Playlist` has `name` and an `ArrayList<Song>`.
- `addSong(Song s)` adds a song only if no existing song has the same title.
- `removeSong(String title)` removes the first matching song title; if not found, do nothing.
- `totalDuration()` returns the sum of all durations.
- `printSongs()` prints each song in list order as `title(duration)` using a for loop.

Do not modify the driver class.

### For example:
| **Input** | **Result** |
|:--------- |:-----------|
|FocusMix 5 Intro 40 Code 120 Chill 90 Code 100 End 30 Chill|Intro(40)<br>Code(120)<br>End(30)<br>Total 190|

### Starter Code
```java

public class PlaylistTester {
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
    }
}

```
