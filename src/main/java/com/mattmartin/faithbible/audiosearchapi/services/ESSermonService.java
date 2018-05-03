package com.mattmartin.faithbible.audiosearchapi.services;

import com.mattmartin.faithbible.audiosearchapi.models.SermonDocumentModel;
import com.mattmartin.faithbible.audiosearchapi.repositories.SermonRepository;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

@Service
public class ESSermonService{


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SermonRepository sermonRepository;

    @Autowired
    public ESSermonService(final SermonRepository repo){
        this.sermonRepository = repo;
    }

    public SermonDocumentModel save(final SermonDocumentModel sermon) {
        logger.info(String.format("Persisting sermon [%s].", sermon));
        return sermonRepository.save(sermon);
    }

    public void delete(final SermonDocumentModel sermonDocumentModel) {
        sermonRepository.delete(sermonDocumentModel);
    }

    public Page<SermonDocumentModel> findBySpeaker(final String speaker, final PageRequest pageRequest) {
        return sermonRepository.findBySpeaker(speaker, pageRequest);
    }


    public Page<SermonDocumentModel> findBySeries(final String series, final PageRequest pageRequest) {
        return sermonRepository.findBySeries(series, pageRequest);
    }

    public Optional<SermonDocumentModel>findById(final String id){
        return sermonRepository.findById(id);
    }

    public Page<SermonDocumentModel> findByFreeSearch(final String query, final PageRequest pageRequest) {

        final SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(multiMatchQuery(query)
                        .field("title")
                        .field("speaker")
                        .field("series")
                        .type(MultiMatchQueryBuilder.Type.BEST_FIELDS))
                .withPageable(pageRequest)
                .build();

        return sermonRepository.search(searchQuery);
    }
}
