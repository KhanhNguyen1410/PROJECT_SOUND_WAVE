package com.sound.wave.controller.song;

import com.sound.wave.model.Song;
import com.sound.wave.service.song.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.Socket;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private ISongService iSongService;

    @GetMapping()
    public ResponseEntity<Iterable<Song>> findAllSong(){
        return new ResponseEntity<>(iSongService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/getsong")
    public ResponseEntity<Song> findSongById(@RequestBody Long id) {
        Song song = iSongService.findSongById(id);
        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Song> saveNewSong(@Valid @RequestBody Song song, BindingResult bindingResult){
        if (!bindingResult.hasFieldErrors()) {
            return new ResponseEntity<>(iSongService.save(song), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Song> deleteSong(@PathVariable("id") Long id){
        Optional<Song> song = iSongService.findById(id);
        if (song.get() == null){
            return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        iSongService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable("id") Long id, @RequestBody Song song ){
        Optional<Song> currentSong = iSongService.findById(id);
        if (currentSong == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentSong.get().setName(song.getName());
        currentSong.get().setDescription(song.getDescription());
        currentSong.get().setAvatar(song.getAvatar());
        currentSong.get().setMusician(song.getMusician());
        currentSong.get().setSinger(song.getSinger());
        currentSong.get().setUser(song.getUser());
        currentSong.get().setCategory(song.getCategory());
        currentSong.get().setAlbum(song.getAlbum());

        iSongService.save(currentSong.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/my-songs/{user_id}")
    public ResponseEntity<Iterable<Song>> findSongByUser(@PathVariable long user_id){
        return new ResponseEntity<>(iSongService.findAllByUserId(user_id), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Song> findSongById(@PathVariable long id){
        return new ResponseEntity<>(iSongService.findById(id).get(), HttpStatus.OK);
    }
    @PostMapping("/{id}")
    public ResponseEntity<Song> countViews(@PathVariable long id, @RequestBody Song song){
        Song songCurrent = iSongService.findById(id).get();
        long views = song.getViews();
        views++;
        if (songCurrent == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        songCurrent.setName(song.getName());
        songCurrent.setDescription(song.getDescription());
        songCurrent.setAvatar(song.getAvatar());
        songCurrent.setMusician(song.getMusician());
        songCurrent.setSinger(song.getSinger());
        songCurrent.setUser(song.getUser());
        songCurrent.setCategory(song.getCategory());
        songCurrent.setAlbum(song.getAlbum());
        songCurrent.setViews(views);
        return new ResponseEntity<>(iSongService.save(songCurrent), HttpStatus.OK);

    }

}
