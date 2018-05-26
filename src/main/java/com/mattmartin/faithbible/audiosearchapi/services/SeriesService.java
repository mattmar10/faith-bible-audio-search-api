package com.mattmartin.faithbible.audiosearchapi.services;

import com.google.common.collect.Lists;
import com.mattmartin.faithbible.audiosearchapi.db.models.SeriesDBModel;
import com.mattmartin.faithbible.audiosearchapi.db.repositories.SeriesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeriesService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private SeriesRepository seriesRepository;

    @Autowired
    public SeriesService(final SeriesRepository seriesRepository){
        this.seriesRepository = seriesRepository;
    }

    public SeriesDBModel save(final SeriesDBModel series){
        logger.info(String.format("Persisting to db series: %s", series));
        return seriesRepository.save(series);
    }

    public Optional<SeriesDBModel> findBySlug(final String slug){
        logger.info(String.format("Searching for series by slug %s", slug));
        return seriesRepository.findBySlug(slug);
    }

    public Optional<SeriesDBModel> findById(final int id){
        return seriesRepository.findById(id);
    }

    public List<SeriesDBModel> getAll(){
        return Lists.newArrayList(seriesRepository.findAll());
    }
}
