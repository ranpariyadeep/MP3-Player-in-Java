import java.util.LinkedList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class PlayList {
    private LinkedList<AudioFile> audioFiles;
    private int current;

    public PlayList() {
        audioFiles = new LinkedList<>();
        this.current = 0;
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
    }

    public void remove(AudioFile file) {
        audioFiles.remove(file);
    }

    public int size() {
        return audioFiles.size();
    }

    public AudioFile currentAudioFile() {
        return (current >= 0 && current < audioFiles.size()) ? audioFiles.get(current) : null;

    }

    public void nextSong() {
        current = (audioFiles.isEmpty() || current < 0 || current >= audioFiles.size()) ? 0 : (current + 1) % audioFiles.size();

    }

    public void loadFromM3U(String pathname) {
        try (BufferedReader reader = new BufferedReader(new FileReader(pathname))) {
            clearPlaylist();
            processM3ULines(reader);
        } catch (IOException e) {
            // Handle the IOException if needed
        }
    }

    private void clearPlaylist() {
        audioFiles.clear();
        current = 0;
    }

    private void processM3ULines(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (isValidM3ULine(line)) {
                AudioFile audioFile = AudioFileFactory.createAudioFile(line);
                if (audioFile != null) {
                    audioFiles.add(audioFile);
                }
            }
        }
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

    public LinkedList<AudioFile> getList() {
        return audioFiles;
    }

    // public void setSortCriterion(SortCriterion sort){}
    public void setSearch(String value) {
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public void jumpToAudioFile(AudioFile file) {
    }

}
