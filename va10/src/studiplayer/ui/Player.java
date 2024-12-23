package studiplayer.ui;

import javafx.scene.image.Image;
import studiplayer.audio.SortCriterion;
import javafx.application.Platform;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import studiplayer.audio.AudioFile;
import studiplayer.audio.NotPlayableException;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.stage.Stage;
import studiplayer.audio.PlayList;
import java.io.IOException;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.scene.control.Button;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import java.io.File;
import java.net.URL;

public class Player extends Application {
    public static final String defaultpath = "playlists/DefaultPlayList.m3u";
    public static final String DEFAULT_PLAYLIST = "playlists/DefaultPlayList.m3u";
    private static final String playlistDirectory = "path/to/playlist/directory";
    private static final String PLAYLIST_DIRECTORY = playlistDirectory;
    private static final String INITIAL_PLAY_TIME_LABEL = "00:00";
    private static final String NO_CURRENT_SONG = " - ";
    private boolean useCertPlayList = false;
    private boolean paus = false;
    private ChoiceBox<SortCriterion> sortChoiceBox = new ChoiceBox<>();

    private PlayList playList;

    private SongTable songTable;

    private PlayerThread playerThread;
    private TimerThread timerThread;
    private Button stopButton;
    private Button nextButton;
    private Button filterButton;
    private Label playListLabel;
    private Label playTimeLabel;
    private Label currentSongLabel;

    private Button playButton;
    private Button pauseButton;

    private TextField searchTextField;

    public Player() {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        lnt(primaryStage, 0);

        this.songTable = new SongTable(playList);
        BorderPane root = new BorderPane();
        root.setCenter(songTable);

        initializeStage(primaryStage);

        // TitledPane filterPane = createFilterPane(root);

        root.setTop(createFilterPane(root));
        root.setCenter(songTable);
        root.setBottom(createBottomPane(root));

        configureSongTableSelectionHandler(songTable);

        configureScene(primaryStage, root);

    }

    private void initializeStage(Stage stage) {

        String deep = "APA Player";
        stage.setTitle(deep);
    }

    private void configureSongTableSelectionHandler(SongTable songTable) {
        songTable.setRowSelectionHandler(e -> playSelectedSong(songTable.getSelectionModel()));
    }

    private void configureScene(Stage stage, BorderPane root) {
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    private TitledPane createFilterPane(BorderPane root) {
        TitledPane filterPane = new TitledPane();
        filterPane.setText("Filter");

        GridPane filterGrid = createFilterGrid();
        filterPane.setContent(filterGrid);

        root.setTop(filterPane);
        filterButton.setOnAction(event -> fix());

        return filterPane;
    }

    private GridPane createFilterGrid() {
        GridPane filterGrid = new GridPane();

        searchTextField = new TextField();
        ChoiceBox<SortCriterion> sortChoiceBox = createSortChoiceBox();
        filterButton = new Button("Display");

        filterGrid.add(new Label("Search text "), 0, 0);
        filterGrid.add(searchTextField, 1, 0);
        filterGrid.add(new Label("Sort by "), 0, 1);
        filterGrid.add(sortChoiceBox, 1, 1);
        filterGrid.add(filterButton, 2, 1);

        return filterGrid;
    }

    private ChoiceBox<SortCriterion> createSortChoiceBox() {
        ChoiceBox<SortCriterion> sortChoiceBox = new ChoiceBox<>();
        sortChoiceBox.getItems().addAll(SortCriterion.values());
        sortChoiceBox.setValue(SortCriterion.DEFAULT);
        return sortChoiceBox;
    }

    private VBox createBottomPane(BorderPane root) {
        VBox bottomPane = new VBox();
        bottomPane.getChildren().addAll(createInfoGrid(), createControlButtons());
        root.setBottom(bottomPane);
        return bottomPane;
    }

    private HBox createControlButtons() {
        HBox controlButtons = new HBox(10); // Add spacing between buttons for better layout
        controlButtons.setAlignment(Pos.CENTER);

        // Create buttons
        playButton = createButton("play.jpg");
        pauseButton = createButton("pause.jpg");
        stopButton = createButton("stop.jpg");
        nextButton = createButton("next.jpg");

        // Add buttons to the container
        controlButtons.getChildren().addAll(playButton, pauseButton, stopButton, nextButton);

        // Assign button actions
        setButtonActions();

        return controlButtons;
    }

    private void setButtonActions() {
        playButton.setOnAction(e -> playCurrentSong());
        pauseButton.setOnAction(e -> pauseCurrentSong());
        stopButton.setOnAction(e -> stopCurrentSong());
        nextButton.setOnAction(e -> playNextSong());
    }

    private GridPane createInfoGrid() {
        GridPane infoGrid = new GridPane();

        playListLabel = new Label(PLAYLIST_DIRECTORY);
        VBox area = new VBox();
        playTimeLabel = new Label(INITIAL_PLAY_TIME_LABEL);
        currentSongLabel = new Label(NO_CURRENT_SONG);

        String playL = "Playlist ";
        infoGrid.add(new Label(playL), 0, 0);
        infoGrid.add(playListLabel, 1, 0);
        String cu = "CurrentSong ";
        infoGrid.add(new Label(cu), 0, 1);
        infoGrid.add(currentSongLabel, 1, 1);
        String pl = "Playtime ";
        infoGrid.add(new Label(pl), 0, 2);
        infoGrid.add(playTimeLabel, 1, 2);
        area.getChildren().add(infoGrid);

        return infoGrid;
    }

    public void lnt(Stage pri, int need) throws Exception {
        if (!useCertPlayList) {

            playList = ChossePlayList(pri);

        } else {
            String po = "playlists/playList.cert.m3u";
            setPlayList(po);
        }
    }

    public void fix() {
        playList.setSearch(searchTextField.getText().trim());
        playList.setSortCriterion(sortChoiceBox.getValue());
        songTable.refreshSongs();
        System.out.println("After filtering, the current song is： " + playList.currentAudioFile());

    }

    public void setUseCertPlayList(boolean useCertPlayList) {
        this.useCertPlayList = useCertPlayList;
    }

    public void setPlayList(String pathname1) throws NotPlayableException {
        loadPlayList(pathname1);
    }

    private Button createButton(String iconFileName) {
        Button button = new Button();
        try {
            button.setGraphic(createImageView(iconFileName));
            button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            button.setStyle("-fx-background-color: #fff;");
        } catch (IllegalArgumentException e) {
            handleMissingImage(iconFileName);
        }
        return button;
    }

    private void handleMissingImage(String iconFileName) {
        System.err.println("Error: Image '/icons/" + iconFileName + "' not found!");
        System.exit(-1); // Consider replacing with a more graceful error-handling strategy
    }

    private ImageView createImageView(String iconFileName) {
        Image image = new Image(getClass().getResourceAsStream("/icons/" + iconFileName));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        return imageView;
    }

    public PlayList ChossePlayList(Stage stage) throws NotPlayableException {
        FileChooser fileChooser = new FileChooser();
        String fileChooserName = "Open Resource File";
        fileChooser.setTitle(fileChooserName);
        return newfile(fileChooser, stage);
    }

    public PlayList newfile(FileChooser fileChooser, Stage stage) {
        File newFile = fileChooser.showOpenDialog(stage);

        if (newFile != null) {
            return loadPlayList(newFile.getAbsolutePath());

        } else {
            return loadPlayList(DEFAULT_PLAYLIST);
        }
    }

    private void startTimerThread() {
        timerThread = new TimerThread();
        timerThread.start();
        String timeThreadStart = "timerThread started ";

    }

    public void resumePlayback() {
        paus = false;
        playList.currentAudioFile().togglePause();
    }

    private void plyerthread() {
        playerThread = new PlayerThread();
        playerThread.start();
    }

    private void playCurrentSong() {
        startTimerThread();
        if (paus) {
            resumePlayback();
        } else {
            if (playerThread == null) {
                plyerthread();
            }
        }
        buttonStates(false, true, true, true);
        updateCurrentSongInfo(playList.currentAudioFile());
        AudioFile currentFile = playList.currentAudioFile();
        System.out.println("Playing " + currentFile);
    }

    private void pauseCurrentSong() {
        paus = true;

        playList.currentAudioFile().togglePause();
        buttonStates(false, true, true, true);
        updateCurrentSongInfo(playList.currentAudioFile());
    }

    private void stopCurrentSong() {
        if (playerThread != null) {
            pt();
        }

        buttonStates(true, false, false, true);
        updateCurrentSongInfo(playList.currentAudioFile());
        System.out.println("Stopping current song");
    }

    private void playNextSong() {
        playList.nextSong();
        if (playerThread != null) {
            vt();
        }
        String swi = "Switched to next audio file: ";
        System.out.println(swi + playList.currentAudioFile().toString());

        playCurrentSong();
        buttonStates(false, true, true, true);
        updateCurrentSongInfo(playList.currentAudioFile());
        String ply = "Playing next song";
        System.out.println(ply);
    }

    public void pt() {
        playerThread.terminate();
        playerThread = null;
    }

    public void vt() {
        playerThread.terminate();
        playerThread = null;
    }

    private void playSelectedSong(TableViewSelectionModel<Song> tableViewSelectionModel) {

        int selectedIndex = tableViewSelectionModel.getSelectedIndex();

        if (selectedIndex >= 0 && selectedIndex < songTable.getTableData().size()) {
            AudioFile selectedFile = songTable.getTableData().get(selectedIndex).getAudioFile();

            playList.jumpToAudioFile(selectedFile);
            updateCurrentSongInfo(selectedFile);

            if (!playButton.isDisable()) {
                playCurrentSong();
            }

            updateCurrentSongInfo(
                    songTable.getTableData().get(tableViewSelectionModel.getSelectedIndex()).getAudioFile());

        }
    }

    public PlayList loadPlayList(String pathname) {

        if (pathname == null) {

            pathname = DEFAULT_PLAYLIST;

        }

        if (pathname.isEmpty()) {
            pathname = DEFAULT_PLAYLIST;
        }
        playList = new PlayList();

        loadm3(pathname);
        return playList;
    }

    private void loadm3(String pathname) {
        try {
            playList.loadFromM3U(pathname);
            if (playList.size() != 0) {
                buttonStates(true, false, false, true);

            }
            if (playList.size() == 0) {
                buttonStates(false, false, false, false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buttonStates(boolean playB, boolean pauseB, boolean stopB, boolean nextB) {
        Platform.runLater(() -> {
            stopButton.setDisable(!stopB);
            playButton.setDisable(!playB);
            pauseButton.setDisable(!pauseB);

            nextButton.setDisable(!nextB);
        });
    }

    private void updateCurrentSongInfo(AudioFile ok) {
        Platform.runLater(() -> {
            if (ok == null) {
                currentSongLabel.setText(NO_CURRENT_SONG);
                playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL);

            } else {
                // String songInfo = song.toString();
                currentSongLabel.setText(ok.toString());
            }
        });
    }

    private class PlayerThread extends Thread {
        private volatile boolean stop = false;

        private boolean playing = false;

        public void terminate() {
            stop = true;
            if (playList.currentAudioFile() != null) {
                playList.currentAudioFile().stop();
            }
        }

        public void run() {
            playing = true;
            while (!stop) {
                if (playing && playList.currentAudioFile() != null) {
                    try {
                        playList.currentAudioFile().play();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Preserve interrupt status
                        break;
                    } catch (NotPlayableException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private class TimerThread extends Thread {
        private volatile boolean stop = false;

        public void terminate() {
            stop = true;
            this.interrupt(); // Signal the thread to stop
        }

        @Override
        public void run() {
            while (!stop && !Thread.currentThread().isInterrupted()) {
                Platform.runLater(this::updatePlayTimeLabel);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Preserve interrupt status
                    break;
                }
            }
        }

        private void updatePlayTimeLabel() {
            if (playList.currentAudioFile() != null) {
                playTimeLabel.setText(playList.currentAudioFile().formatPosition());
            } else {
                playTimeLabel.setText("00:00");
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}