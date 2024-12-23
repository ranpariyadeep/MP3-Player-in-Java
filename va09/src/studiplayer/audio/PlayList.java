package studiplayer.audio;

import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

public class PlayList implements Iterable<AudioFile> {
    private List<AudioFile> audioFiles;
    private String search;
    private SortCriterion sortCriterion;
    private AudioFile currentSong;
    private Iterator<AudioFile> iterator;

    public PlayList() {
        this.audioFiles = new ArrayList<>();
        this.currentSong = null;
        this.sortCriterion = SortCriterion.DEFAULT;
        this.iterator = audioFiles.iterator();

    }

    public PlayList(String m3uPathname) {
        this();
        File file = new File(m3uPathname);
        boolean canRead = file.canRead();
        if (!canRead) {
            throw new RuntimeException("File is not exist");
        }
        loadFromM3U(m3uPathname);
    }

    public void add(AudioFile file) {
        audioFiles.add(file);
        resetIterator();
    }

    public void remove(AudioFile file) {
        audioFiles.remove(file);
        resetIterator();
    }

    public int size() {
        return audioFiles.size();
    }

    public AudioFile currentAudioFile() {
        return currentSong;
    }

    public void nextSong() {
        if (!iterator.hasNext()) {
            resetIterator();
        } else {
            currentSong = iterator.next();
        }

    }

    public void loadFromM3U(String pathname) {
        audioFiles.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(pathname))) {

            processM3ULines(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processM3ULines(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (isValidM3ULine(line)) {
                try {
                    AudioFile audioFile = AudioFileFactory.createAudioFile(line);
                    if (audioFile != null) {
                        audioFiles.add(audioFile);
                    }
                } catch (NotPlayableException e) {
                    e.printStackTrace();

                }
            }
        }
        resetIterator();
    }

    private boolean isValidM3ULine(String line) {
        return !line.startsWith("#") && !line.trim().isEmpty();
    }

    public void saveAsM3U(String pathname) {
        try {
            BufferedWriter writer = openFileForWriting(pathname);
            writeAudioFilesToM3U(writer);
            closeWriter(writer);
        } catch (IOException e) {
            // Handle the IOException if needed
        }
    }

    private BufferedWriter openFileForWriting(String pathname) throws IOException {
        return new BufferedWriter(new FileWriter(pathname));
    }

    private void writeAudioFilesToM3U(BufferedWriter writer) throws IOException {
        for (AudioFile audioFile : audioFiles) {
            writer.write(audioFile.getPathname());
            writer.newLine();
        }
    }

    private void closeWriter(BufferedWriter writer) {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            // Handle the IOException if needed
        }
    }

    public List<AudioFile> getList() {
        return audioFiles;
    }

    public SortCriterion getSortCriterion() {
        return sortCriterion;
    }

    public void setSortCriterion(SortCriterion sort) {
        this.sortCriterion = sort;
        resetIterator();
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String value) {
        this.search = value;
        resetIterator();
    }

    public void jumpToAudioFile(AudioFile file) {
        if (audioFiles.indexOf(file) != -1) {
            iterator = audioFiles.listIterator(audioFiles.indexOf(file));
            currentSong = iterator.next();
        } else {
            resetIterator();
        }
    }

    public Iterator<AudioFile> iterator() {
        return new ControllablePlayListIterator(audioFiles, search, sortCriterion);

    }

    public String toString() {
        StringBuilder bilder = new StringBuilder("[");
        int index = 0;
        int size = audioFiles.size();
        while (index < size) {
            if (index > 0) {
                bilder.append(", ");
            }
            bilder.append(audioFiles.get(index).toString());
            index++;
        }
        bilder.append("]");
        return bilder.toString();
    }

    private void resetIterator() {
        iterator = new ControllablePlayListIterator(audioFiles, search, sortCriterion);
        currentSong = iterator.hasNext() ? iterator.next() : null;
    }

}
