package studiplayer.audio;

import java.util.Comparator;

public class TitleComparator implements Comparator<AudioFile> {
    public int compare(AudioFile o1, AudioFile o2) {
        if (o1.equals(null) || o2.equals(null)) {
            throw new NullPointerException("One or both AudioFiles are null");
        }

        int sum = 0;
        if (o1.getTitle().equals(null)) {
            sum += 1;
        }
        if (o2.getTitle().equals(null)) {
            sum += 2;
        }

        switch (sum) {
        case 1:
            return -1;
        case 2:
            return 1;
        case 3:
            return 0;
        }

        return o1.getTitle().compareTo(o2.getTitle());
    }
}
