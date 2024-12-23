
import java.util.Map;

import java.util.Set;
import studiplayer.basic.TagReader;

class TaggedFile extends SampledFile {

    private String album;

    public TaggedFile() {
        super();
    }

    public TaggedFile(String path) {
        super(path);
        readAndStoreTags();
    }

    public String getAlbum() {
        if (album != null) {
            return album.trim();
        } else {
            return "";
        }
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void readAndStoreTags() {
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
    }

    public String toString() {

        StringBuilder result = new StringBuilder();

        // Add title
        if (getAuthor() != null && !getAuthor().isEmpty()) {
            result.append(getAuthor()).append(" - ");
        }

        if (getTitle() != null && !getTitle().isEmpty()) {
            result.append(getTitle()).append(" - ");
        }

        // Add author

        // Add album
        if (getAlbum() != null && !getAlbum().isEmpty()) {
            result.append(getAlbum()).append(" - ");
        }

        // Add duration
        if (formatDuration() != null && !formatDuration().isEmpty()) {
            result.append(formatDuration());
        }

        return result.toString();
    }

}
