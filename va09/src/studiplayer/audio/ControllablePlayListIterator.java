package studiplayer.audio;

import java.util.ArrayList;

import java.util.Comparator;
import java.util.Iterator;

import java.util.List;

public class ControllablePlayListIterator implements Iterator<AudioFile> {
    private List<AudioFile> audioFiles;
    private Iterator<AudioFile> iterator;

    public ControllablePlayListIterator(List<AudioFile> list) {
        this.audioFiles = list;
        this.iterator = audioFiles.iterator();
    }

    public ControllablePlayListIterator(List<AudioFile> list, String search, SortCriterion sort) {

        audioFiles = (search != null && !search.isEmpty()) ? filterList(list, search) : list;

        audioFiles = (sort != SortCriterion.DEFAULT) ? sortList(audioFiles, sort) : audioFiles;

        this.iterator = audioFiles.iterator();
    }

    public List<AudioFile> filterList(List<AudioFile> list, String search) {
        List<AudioFile> filteredList = new ArrayList<>();
        for (AudioFile file : list) {
            if (file instanceof TaggedFile) {
                TaggedFile taggedFile = (TaggedFile) file;
                checkAndAdd(taggedFile, search, filteredList);
            } else {
                checkAndAdd(file, search, filteredList);
            }
        }
        return filteredList;
    }

    private void checkAndAdd(AudioFile file, String search, List<AudioFile> filteredList) {
        if (file.getAuthor().contains(search) || file.getTitle().contains(search)) {
            filteredList.add(file);
        }
    }

    private List<AudioFile> sortList(List<AudioFile> list, SortCriterion sort) {
        List<AudioFile> sortedList = new ArrayList<>(list);

        Comparator<AudioFile> comparator = null;

        if (sort.equals(SortCriterion.AUTHOR)) {
            comparator = new AuthorComparator();
        } else if ((sort.equals(SortCriterion.TITLE))) {
            comparator = new TitleComparator();
        } else if ((sort.equals(SortCriterion.ALBUM))) {
            comparator = new AlbumComparator();
        } else if ((sort.equals(SortCriterion.DURATION))) {
            comparator = new DurationComparator();
        }

        sortedList.sort(comparator);

        return sortedList;

    }

    public AudioFile jumpToAudioFile(AudioFile file) {
        int index = audioFiles.indexOf(file);
        iterator = audioFiles.listIterator(index);

        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            return null;
        }
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public AudioFile next() {
        return iterator.next();
    }
}