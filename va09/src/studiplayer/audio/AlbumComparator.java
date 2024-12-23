package studiplayer.audio;

import java.util.Comparator;

public class AlbumComparator implements Comparator<AudioFile> {
    
   
  
    public int compare(AudioFile o1, AudioFile o2) {
        
        if (o1.equals(null) || o2.equals(null)) {
            throw new NullPointerException("One or both AudioFiles are null");
        }
        
        if (o1 instanceof TaggedFile && o2 instanceof TaggedFile) {
            TaggedFile t1 = (TaggedFile) o1;
            TaggedFile t2 = (TaggedFile) o2;
            if (t1.getAlbum() == null || t2.getAlbum() == null) {
                return (t1.getAlbum()== null) ? ((t2.getAlbum() == null) ? 0 : -1) : 1;
            } else {
                return t1.getAlbum().compareToIgnoreCase(t2.getAlbum());
            }
        } else {
            return Boolean.compare(o1 instanceof TaggedFile, o2 instanceof TaggedFile);
        }
    }
}
