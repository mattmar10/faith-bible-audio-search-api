package com.mattmartin.faithbible.audiosearchapi.elasticsearch.services;

import com.github.vanroy.springdata.jest.JestElasticsearchTemplate;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SermonDocumentModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.repositories.ESSermonRepository;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

@Service
public class ESSermonService{


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ESSermonRepository eSSermonRepository;
    private final JestElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public ESSermonService(final ESSermonRepository ESSermonRepository, final JestElasticsearchTemplate elasticsearchTemplate){
        this.eSSermonRepository = ESSermonRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    public SermonDocumentModel save(final SermonDocumentModel sermon) {
        logger.info(String.format("Persisting sermon [%s].", sermon));
        return eSSermonRepository.save(sermon);
    }

    public void delete(final SermonDocumentModel sermonDocumentModel) {
        logger.info(String.format("Deleting sermon [%s].", sermonDocumentModel));
        eSSermonRepository.delete(sermonDocumentModel);
    }

    public void deleteAll(){
        eSSermonRepository.deleteAll();
    }

    public Page<SermonDocumentModel> findBySpeaker(final String speaker, final PageRequest pageRequest) {
        logger.info(String.format("Searching for sermons by speaker [%s].", speaker));
        return eSSermonRepository.findBySpeaker(speaker, pageRequest);
    }


    public Page<SermonDocumentModel> findBySeries(final String series, final PageRequest pageRequest) {
        logger.info(String.format("Searching for sermons by series [%s].", series));

        final SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("series", series))
                .withPageable(pageRequest)
                .build();

        return eSSermonRepository.search(query);
    }

    public Optional<SermonDocumentModel>findById(final Integer id){
        logger.info(String.format("Fetching details for sermon [%s].", id));
        return eSSermonRepository.findById(id);
    }

    public Page<SermonDocumentModel> findByFreeSearch(final String query, final PageRequest pageRequest) {

        logger.info(String.format("Executing free form search with query [%s].", query));

        final SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(multiMatchQuery(query)
                        .field("title")
                        .field("speaker")
                        .field("series")
                        .type(MultiMatchQueryBuilder.Type.BEST_FIELDS))
                .withPageable(pageRequest)
                .build();

        return eSSermonRepository.search(searchQuery);
    }

    public Page<SermonDocumentModel> findMostRecent(final int count) {
        logger.info(String.format("Finding most recent sermon"));

        final SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery()).withPageable(PageRequest.of(0, count, Sort.Direction.DESC, "date")).build();

        return eSSermonRepository.search(searchQuery);
    }

}
