package com.alkemy.disney.alkemylab.service.impl;

import com.alkemy.disney.alkemylab.dto.GenreDTO;
import com.alkemy.disney.alkemylab.entity.GenreEntity;
import com.alkemy.disney.alkemylab.entity.GenreMovieEntity;
import com.alkemy.disney.alkemylab.mapper.GenreMapper;
import com.alkemy.disney.alkemylab.mapper.MovieMapper;
import com.alkemy.disney.alkemylab.repository.GenreMovieRepository;
import com.alkemy.disney.alkemylab.repository.GenreRepository;
import com.alkemy.disney.alkemylab.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    @Autowired
    private GenreMapper genreMapper;
    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private GenreMovieRepository genreMovieRepository;

    public GenreDTO save(GenreDTO dto) {
        GenreEntity entity = genreMapper.genreDTO2Entity(dto);
        GenreEntity entitySaved = genreRepository.save(entity);
        GenreDTO result = genreMapper.genreEntity2DTO(entitySaved);
        if (dto.getMovies() != null)
            addMovies(dto, result);
        return result;
    }

    public List<GenreDTO> getAllGenres() {
        List<GenreEntity> entities = genreRepository.findAll();
        List<GenreDTO> result = genreMapper.genreEntity2DTOList(entities);
        result.stream().forEach(this::loadMovies);
        return result;
    }


    private void loadMovies(GenreDTO genre) {
        genre.setMovies(movieMapper.movieEntity2DTOList(genreMovieRepository.loadMovies2Genre(genre.getId())));
    }

    private void addMovies(GenreDTO dto, GenreDTO result) {
        dto.getMovies().forEach(movie -> {
            GenreMovieEntity genreMovie = new GenreMovieEntity();
            genreMovie.setGenreId(result.getId());
            genreMovie.setMovieId(movie.getId());
            genreMovieRepository.save(genreMovie);
        });
        loadMovies(result);
    }
}
