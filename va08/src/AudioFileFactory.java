

public class AudioFileFactory {

    public static AudioFile createAudioFile(String path) {
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
                    throw new RuntimeException("Missing suffix");
                } else {
                    throw new RuntimeException("Unknown suffix for AudioFile \"media/unknown.xyz\"");
                }
        }
    }

}
