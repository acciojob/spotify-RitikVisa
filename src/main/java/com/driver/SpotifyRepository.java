package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data

        User user = new User("ritik","8800886677");
        Artist artist = new Artist("Arijit singh");

        Album album = new Album();
        album.setArtistName("Arijit singh");
        album.setTitle("brahmastra");

        Song song = new Song("kesariya",120);

        Playlist playlist = new Playlist("sad");






        artistAlbumMap = new HashMap<>();
        List<Album> albumList = new ArrayList<>();
        albumList.add(album);
        artistAlbumMap.put(artist,albumList);

        albumSongMap = new HashMap<>();
        List<Song> songList = new ArrayList<>();
        songList.add(song);
        albumSongMap.put(album,songList);

        playlistSongMap = new HashMap<>();
        playlistSongMap.put(playlist,songList);

        playlistListenerMap = new HashMap<>();
        List<User> userList = new ArrayList<>();
        userList.add(user);
        playlistListenerMap.put(playlist,userList);

        creatorPlaylistMap = new HashMap<>();
        creatorPlaylistMap.put(user,playlist);

        userPlaylistMap = new HashMap<>();
        List<Playlist> playlistList = new ArrayList<>();
        playlistList.add(playlist);
        userPlaylistMap.put(user,playlistList);

        songLikeMap = new HashMap<>();
        songLikeMap.put(song,userList);

        users = new ArrayList<>();
        users.add(user);

        songs = new ArrayList<>();
        songs.add(song);

        playlists = new ArrayList<>();
        playlists.add(playlist);

        albums = new ArrayList<>();
        albums.add(album);

        artists = new ArrayList<>();
        artists.add(artist);


    }

    public User createUser(String name, String mobile) {
        User user = new User();
        user.setName(name);
        user.setMobile(mobile);
        users.add(user);
        return user;
    }

    public Artist createArtist(String name) {
        Artist artist = new Artist();
        artist.setName(name);
        if(!artists.contains(artist)){
            artists.add(artist);
        }

        return artist;
    }

    public Album createAlbum(String title, String artistName) {


        Album album = new Album();
        List<Album> albumList =new ArrayList<>();


            //This API creates a new album with a given title and artist name. If the
            // artist does not exist, the API creates a new artist first.

               Artist artist = createArtist(artistName);


                album.setTitle(title);
                album.setArtistName(artistName);

                if(artistAlbumMap.containsKey(artist)){
                    artistAlbumMap.get(artist).add(album);
                }else{
                    artistAlbumMap.put(artist,albumList);
                     artistAlbumMap.get(artist).add(album);
                }


        return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        //This API creates a new song with a given title and album name. If the album does not exist, it throws an exception.
        Boolean flag = false;


        for(Album album : albums){
            if(album.getTitle().equals(albumName)){
                 flag = true;
                 break;
            }
        }

        if(flag){
            Song song = new Song();
            song.setTitle(title);
            song.setLength(length);
            song.setAlbumName(albumName);
            return song;
        }

          throw new Exception("album does not exist");

    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        //This API creates a new playlist with a given title and adds all songs with a given length to the
        // playlist.
        // The creator of the playlist is the given user, who is also the only listener at the time of playlist creation.

        Playlist playlist = new Playlist();
        playlist.setTitle(title);

        List<Song> newsongList = new ArrayList<>();
        List<User> userList = new ArrayList<>();

        playlistSongMap.put(playlist,newsongList);
        playlistListenerMap.put(playlist,userList);


        for(Song song : songs){
            if(song.getLength() == length){
                playlistSongMap.get(playlist).add(song);
            }
        }

        playlists.add(playlist);

       for(User user : users){
          if(user.getMobile().equals(mobile)){
              if(!creatorPlaylistMap.containsKey(user)){
                  creatorPlaylistMap.put(user,playlist);
              }
              if(playlistListenerMap.containsKey(playlist)){
                  playlistListenerMap.get(playlist).add(user);
              }else{
                  userList.add(user);
                  playlistListenerMap.put(playlist,userList);
              }

              return playlist;
          }

       }



        throw new Exception("User does not exist");

    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
//This API creates a new playlist with a given title and adds all songs with the given titles to the playlist.
// The creator of the playlist is the given user, who is also the only listener at the time of playlist creation.

        Playlist playlist = new Playlist();
        playlist.setTitle(title);

        List<Song> newsongList = new ArrayList<>();
        List<User> userList = new ArrayList<>();


        for(Song song : songs){
            if(song.getTitle().equals(title)){
                playlistSongMap.get(playlist).add(song);
            }
        }

        playlists.add(playlist);

        for(User user : users){
            if(user.getMobile().equals(mobile)){
                if(!creatorPlaylistMap.containsKey(user)){
                    creatorPlaylistMap.put(user,playlist);
                }
                if(playlistListenerMap.containsKey(playlist)){
                    playlistListenerMap.get(playlist).add(user);
                }else{
                    userList.add(user);
                    playlistListenerMap.put(playlist,userList);
                }

                return playlist;
            }

        }



        throw new Exception("User does not exist");


    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
//This API finds a playlist with a given title and adds the given user as a listener to that playlist.
// If the user is the creator or already a listener, it does nothing.
        User newUser = new User();
        for(User user : users){
            if(user.getMobile().equals(mobile)){
                newUser = user;
                break;
            }
        }
        if(!users.contains(newUser)){
           throw new Exception("User does not exist");
        }

        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(playlistTitle) ){
                if(!playlistListenerMap.get(playlist).contains(newUser)){
                    playlistListenerMap.get(playlist).add(newUser);
                    return playlist;
                }
            }

        }

        throw new Exception("Playlist does not exist");


    }

    public Song likeSong(String mobile, String songTitle) throws Exception {

        //This API allows a user to like a given song, which also auto-likes the corresponding artist.
        // If the user has already liked the song, it does nothing.
        User newUser = new User();
        for(User user : users){
            if(user.getMobile().equals(mobile)){
                newUser = user;
                break;
            }
        }
        if(!users.contains(newUser)){
            throw new Exception("User does not exist");
        }

        //---------------------------------------------------------
        Song newSong = new Song();

        for(Song song : songs){
            if(song.getTitle().equals(songTitle)){

                newSong = song;
                break;
            }
        }
        String albumname ="";
        if(!songLikeMap.get(newSong).contains(newUser)){

            songLikeMap.get(newSong).add(newUser);
           albumname += newSong.getAlbumName();
        }else{
            throw new Exception("Song does not exist");
        }

       for(Map.Entry<Artist,List<Album>> entry : artistAlbumMap.entrySet()){
           if(entry.getValue().contains(albumname)){
              int like= entry.getKey().getLikes() + 1;
               entry.getKey().setLikes(like);

           }
       }

        return newSong;
    }

    public String mostPopularArtist() {
        String mostPopArtist = "";
        for(Artist artist: artists){
            int max =0;

            int likes = artist.getLikes();
            if(likes>max){
                max = Math.max(likes,max);
                mostPopArtist = artist.getName();
            }
        }
      return mostPopArtist;
    }

    public String mostPopularSong() {
        int max = 0;
        String mostPopSong ="";
        for(Map.Entry<Song,List<User>> entry : songLikeMap.entrySet()){
            int len = entry.getValue().size();
            if(len >max){
                max = Math.max(len,max);
                mostPopSong = entry.getKey().getTitle();
            }
        }

        return mostPopSong;
    }
}
