package studiplayer.audio;

import java.util.Map;
import java.util.StringJoiner;
import java.util.Set;
import studiplayer.basic.TagReader;

public class TaggedFile extends SampledFile {

    private String album;

    public TaggedFile() {
        super();
    }

    public TaggedFile(String path) throws NotPlayableException {
        super(path);
        readAndStoreTags();
    }

    public String getAlbum() {
      
        if (album != null) {
            String alb = album.trim();
            return alb;
        } else {
            return "";
        }
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void readAndStoreTags() throws NotPlayableException {
        try {
            Map<String, Object> tagMap = TagReader.readTags(getPathname());

            if (tagMap.containsKey("title")) {
                setTitle(tagMap.get("title").toString());
            }

            if (tagMap.containsKey("author")) {
                setAuthor(tagMap.get("author").toString());
            }

            if (tagMap.containsKey("album")) {
                setAlbum(tagMap.get("album").toString());
            }

            if (tagMap.containsKey("duration") && tagMap.get("duration") instanceof Long) {
                setDuration((Long) tagMap.get("duration"));
            }
        } catch (Exception e) {
            throw new NotPlayableException(getPathname(), "file doent readable");
        }
    }

    public String toString() {
        StringJoiner result = new StringJoiner(" - ");
        for (int i = 1; i <= 4; i++) {
            String part = checkAndGetString(i);
            if (part != null && !part.isEmpty()) {
                result.add(part);
            }
        }
        return result.toString();
    }

    private String checkAndGetString(int code) {
        switch (code) {
        case 1:
            return getAuthor();
        case 2:
            return getTitle();
        case 3:
            return getAlbum();
        case 4:
            return formatDuration();
        default:
            return null;
        }
    }

}
