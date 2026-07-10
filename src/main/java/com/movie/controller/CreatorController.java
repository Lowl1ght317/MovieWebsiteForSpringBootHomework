package com.movie.controller;

import com.movie.entity.Actor;
import com.movie.entity.Director;
import com.movie.entity.Movie;
import com.movie.service.ActorService;
import com.movie.service.DirectorService;
import com.movie.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/creator")
public class CreatorController {

    private final ActorService actorService;
    private final DirectorService directorService;
    private final MovieService movieService;

    public CreatorController(ActorService actorService, DirectorService directorService, MovieService movieService) {
        this.actorService = actorService;
        this.directorService = directorService;
        this.movieService = movieService;
    }

    @GetMapping("/actor/search")
    public ResponseEntity<List<Actor>> searchActor(@RequestParam String name) {
        return ResponseEntity.ok(actorService.searchByName(name));
    }

    @GetMapping("/actor/{id}")
    public ResponseEntity<List<Movie>> getActorMovies(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMoviesByActor(id));
    }

    @GetMapping("/director/search")
    public ResponseEntity<List<Director>> searchDirector(@RequestParam String name) {
        return ResponseEntity.ok(directorService.searchByName(name));
    }

    @GetMapping("/director/{id}")
    public ResponseEntity<List<Movie>> getDirectorMovies(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMoviesByDirector(id));
    }
}