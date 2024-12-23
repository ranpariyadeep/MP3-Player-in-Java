package studiplayer.audio;

public class AudioFileFactory {

    public static AudioFile createAudioFile(String path) throws NotPlayableException {
        String extension = path.substring(path.lastIndexOf('.') + 1).toLowerCase();

        switch (extension) {
        case "wav":
            return new WavFile(path);
        case "mp3":
            return new TaggedFile(path);
        case "ogg":
            return new TaggedFile(path);
        case ".oGg":
            return new TaggedFile(path);
        default:
            if (path.lastIndexOf('.') == -1) {
                throw new NotPlayableException(path, "Missing suffix");
            } else {
                throw new NotPlayableException(path, "Unknown suffix for AudioFile \"media/unknown.xyz\"");
            }
        }
    }
}
