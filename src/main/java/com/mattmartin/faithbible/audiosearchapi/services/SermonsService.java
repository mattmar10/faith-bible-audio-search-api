package com.mattmartin.faithbible.audiosearchapi.services;

import com.mattmartin.faithbible.audiosearchapi.db.models.SermonDBModel;
import com.mattmartin.faithbible.audiosearchapi.db.repositories.SermonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SermonsService {

    private SermonRepository sermonRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SermonsService(final SermonRepository repository){
        this.sermonRepository = repository;
    }

    public SermonDBModel save(final SermonDBModel sermon){
        logger.info(String.format("Persisting to db sermon: %s", sermon));
        return this.sermonRepository.save(sermon);
    }

    public Optional<SermonDBModel> findById(final int sermonId){
        logger.info(String.format("Fetching sermon from db with id: %d", sermonId));
        return this.sermonRepository.findById(sermonId);
    }

    public Optional<SermonDBModel> findBySlug(final String slug){
        logger.info(String.format("Fetching sermon from db with slug: %s", slug));
        return this.sermonRepository.findBySlug(slug);
    }
}
