package studiplayer.audio;

import java.util.Comparator;

public class AuthorComparator implements Comparator<AudioFile> {
    public int compare(AudioFile o1, AudioFile o2) {
        if (o1.equals(null) || o2.equals(null)) {
            throw new NullPointerException("One or both AudioFiles are null");
        }

        int author1Null = (o1.getAuthor().equals(null)) ? 1 : 0;
        int author2Null = (o2.getAuthor().equals(null)) ? 1 : 0;

        switch (author1Null + author2Null) {
        case 0: // Both authors are not null
            return o1.getAuthor().compareTo(o2.getAuthor());
        case 1:
            if (o1.getAuthor().equals(null)) {
                return -1;
            } else {
                return 1;
            }

        default: // Both authors are null
            return 0;
        }
    }
}
