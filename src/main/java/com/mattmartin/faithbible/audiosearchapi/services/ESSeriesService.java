package com.mattmartin.faithbible.audiosearchapi.services;

import com.github.vanroy.springdata.jest.JestElasticsearchTemplate;
import com.mattmartin.faithbible.audiosearchapi.models.SeriesModel;
import com.mattmartin.faithbible.audiosearchapi.models.SermonDocumentModel;
import com.mattmartin.faithbible.audiosearchapi.repositories.SeriesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ESSeriesService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SeriesRepository seriesRepository;
    private final JestElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public ESSeriesService(final SeriesRepository repo, final JestElasticsearchTemplate elasticsearchTemplate){
        this.seriesRepository = repo;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    public SeriesModel save(final SeriesModel seriesModel) {
        logger.info(String.format("Persisting series [%s].", seriesModel));
        return seriesRepository.save(seriesModel);
    }

    public Optional<SeriesModel> findById(final String id){
        logger.info(String.format("Fetching details for series [%s].", id));
        return seriesRepository.findById(id);
    }

}
