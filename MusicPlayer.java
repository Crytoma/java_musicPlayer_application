package MusicPlayerApplication;

//Kacper Rajchel :: 16/06/2020

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map.Entry;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

public class MusicPlayer extends Application {

    Stage window;
    Duration duration;
    String path;
    Label currentSong;
    Media media;
    MediaPlayer mediaPlayer;
    MediaView mediaView;
    AlertBox alertBox;
    boolean songExists;

    TableView<Song> table;

    File songFolder;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;

        path = "C:/JavaFX Desktop Application/MusicPlayerApplication/SongsLibrary/09. Seek & Destroy.mp3";

        // System.out.println(table.getSelectionModel().getSelectedItem().getPath());

        media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        // mediaPlayer.setAutoPlay(true);
        mediaView = new MediaView(mediaPlayer);

        // Label to show what song is currently playing
        currentSong = new Label(new File(path).getName());
        currentSong.getStyleClass().add("label-player");

        Button play = new Button("Play");
        play.setOnAction(event -> {
            mediaPlayer.play();
        });
        play.getStyleClass().add("button-player");

        Button pause = new Button("Pause");
        pause.setOnAction(event -> {
            mediaPlayer.pause();
        });

        Button playSong = new Button("Play Song");

        playSong.setOnAction(event -> {
            playSong("C:/JavaFX Desktop Application/MusicPlayerApplication/SongsLibrary/08. The Call Of Ktulu.mp3");
        });

        Button chooseMainSongLibrary = new Button("Choose Folder");

        chooseMainSongLibrary.setOnAction(event -> {
            try {
                chooseSongFolder();
            } catch (NullPointerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        Button chooseSongFromMainLibrary = new Button("Choose Song");

        chooseSongFromMainLibrary.setOnAction(event -> {
            chooseSongFromFolder();
        });

        Slider slider = new Slider();

        slider.setMinSize(250, 5);

        HBox.setHgrow(slider, Priority.ALWAYS);

        slider.setOnMouseDragReleased(event -> {
            duration = mediaPlayer.getMedia().getDuration();
            mediaPlayer.seek(duration.multiply(slider.getValue() / 100.0));
        });

        slider.setOnMouseClicked(event -> {
            duration = mediaPlayer.getMedia().getDuration();
            mediaPlayer.seek(duration.multiply(slider.getValue() / 100.0));
        });

        TableColumn<Song, String> songTrackNumberColumn = new TableColumn<>("#");
        songTrackNumberColumn.setMinWidth(200);
        songTrackNumberColumn.setCellValueFactory(new PropertyValueFactory<>("#"));

        TableColumn<Song, String> songTitleColumn = new TableColumn<>("Title");
        songTitleColumn.setMinWidth(200);
        songTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Song, String> songArtistColumn = new TableColumn<>("Artist");
        songArtistColumn.setMinWidth(200);
        songArtistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));

        TableColumn<Song, String> songAlbumColumn = new TableColumn<>("Album");
        songAlbumColumn.setMinWidth(200);
        songAlbumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));

        TableColumn<Song, Integer> albumYearColumn = new TableColumn<>("Year");
        albumYearColumn.setMinWidth(200);
        albumYearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        table = new TableView<>();

        table.getColumns().addAll(songTitleColumn, songAlbumColumn, songArtistColumn, albumYearColumn);

        // Listener for song changes. Adds current song to list
        media.getMetadata().addListener((MapChangeListener<String, Object>) change -> {
            try {
                getSongMetaData();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });




        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(play, pause, playSong, chooseMainSongLibrary, chooseSongFromMainLibrary);

        Menu fileMenu = new Menu("_File");

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu);



        MenuItem openSong = new MenuItem("Open Song...");
        openSong.setOnAction(event -> {chooseSongFromFolder();});


/*
        window.setOnCloseRequest(event -> 
        {
            event.consume();

            boolean answer = ConfirmationBox.display("Title Conf", "Are you sure you want to exit?");
            if (answer)
                window.close(); 
        });
*/


        MenuItem exitProgram = new MenuItem("_Exit");

        exitProgram.setOnAction(event -> 
        {
            boolean answer = ConfirmationBox.display("Title", "You sure?");
            if (answer) {window.close();}
        });



        fileMenu.getItems().add(openSong);
        fileMenu.getItems().add(new SeparatorMenuItem());
        fileMenu.getItems().add(exitProgram);



        VBox layout = new VBox();
        layout.getChildren().addAll(menuBar, table, mediaView, currentSong, slider, hBox);

        Scene scene = new Scene(layout);
        scene.getStylesheets().add("MusicPlayerApplication/Style.css");
        window.setScene(scene);
        window.setMinWidth(300);
        window.setMinHeight(200);
        window.setTitle("Media Player");
        window.show();

    }//End MusicPlayer class
 
    private ObservableList <Song> getSong()
    {
        ObservableList <Song> songs = FXCollections.observableArrayList();
        Song testSong = new Song();

        testSong.setTitle("Swaggeroo");
        Song testSon1 = new Song();

        testSon1.setTitle("Kitchen momma");


        songs.add(testSong);
        return songs;
    }

    

    //Choose a folder where you store your music to import songs.
    private void chooseSongFolder() throws NullPointerException, IOException
    {
        try 
        {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File("C:/JavaFX Desktop Application/MusicPlayerApplication/SongsLibrary"));
        dc.setTitle("Choose your song folder");
        dc.showDialog(window);
        songFolder = dc.getInitialDirectory();



        saveMusicPlayerSettings();


        } catch (NullPointerException n)
        {
                System.out.println("EMPTY FOLDER! NO MP3 FILES FOUND!");
        }
    }






    private void chooseSongFromFolder()
    {




        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("mp3", "*.mp3"),
            new FileChooser.ExtensionFilter("flac", "*.flac"));
        

        fc.setInitialDirectory(songFolder);
    
        

        File [] songsInDirectory = songFolder.listFiles();

        
        for (File f : songsInDirectory)
        {
            if (f.getName().endsWith("mp3"))
            {
                System.out.println(f.getName());
            }
        }

        File file = fc.showOpenDialog(window);
        path = file.getAbsolutePath();
        path = path.replace("\\", "/");
        media = new Media(new File(path).toURI().toString());
        mediaPlayer.stop();
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaView.setMediaPlayer(mediaPlayer);
        currentSong.setText(file.getName());


       

    }


    //For each song path given parse the song data for streaming.
    private void parseSongMetaData()
    {
        Song song = new Song();



    }

          






    private void saveInitialFolderSongs()
    {
        
    }




    private void listFilesInDirectory(File [] songs)
    {

    }





    private void playSong(String path)
    {
        media = new Media(new File(path).toURI().toString());
        mediaPlayer.stop();
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaView.setMediaPlayer(mediaPlayer);
        currentSong.setText(new File(path).getName());
        
        
        
    }


    private void getSongMetaData() throws InterruptedException
    {

        Song song = new Song();


        ObservableMap<String, Object> steve = media.getMetadata();

            //Quick fix for checking if the last metadata key has been scanned in.
                for (Entry<String, Object> entry : steve.entrySet())
                {
                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
    
                    if (entry.getKey() == "artist")
                    {
                        song.setArtist(String.valueOf(entry.getValue()));
                    }
    
                    if (entry.getKey() == "title")
                    {
                        song.setTitle(String.valueOf(entry.getValue()));
                    }
    
                    if (entry.getKey() == "album")
                    {
                        song.setAlbum(String.valueOf(entry.getValue()));
                    }
    
                    if (entry.getKey() == "year")
                    {
                        song.setYear((int)entry.getValue());
                    }
                } 


            
            if (songExists == false && !song.getArtist().isBlank() && !song.getTitle().isBlank() && !song.getAlbum().isEmpty() && song.getYear() != 0)
            {
            table.getItems().add(song);
            songExists = true;
            } 
    }

    private void saveMusicPlayerSettings() throws IOException
    {
        try
        {
            PrintWriter writer;
            writer = new PrintWriter(songFolder);
            writer.println(songFolder.getAbsolutePath());
            writer.flush();
            writer.close();
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}