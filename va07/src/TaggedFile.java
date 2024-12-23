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

        for (String tag : tagMap.keySet()) {
            Object value = tagMap.get(tag);

            if ("title".equals(tag)) {
                setTitle(value != null ? value.toString() : null);
            } else if ("author".equals(tag)) {
                setAuthor(value != null ? value.toString() : null);
            } else if ("album".equals(tag)) {
                setAlbum(value != null ? value.toString() : null);
            } else if ("duration".equals(tag)) {
                if (value instanceof Long && value != null) {
                    setDuration((Long) value);
                }
            }

            /*
             * switch (tag) { case "title": setTitle(value != null ? value.toString() :
             * null); break; case "author": setAuthor(value != null ? value.toString() :
             * null); break; case "album": setAlbum(value != null ? value.toString() :
             * null); break; case "duration": if (value instanceof Long && value != null) {
             * setDuration((Long) value); } break; default: break; }
             */ }

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
