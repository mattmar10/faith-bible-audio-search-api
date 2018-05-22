package com.mattmartin.faithbible.audiosearchapi.elasticsearch.repositories;

import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SermonDocumentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SermonRepository extends ElasticsearchRepository<SermonDocumentModel, String> {

    Page<SermonDocumentModel> findBySeries(final String series, final Pageable pageable);

    Page<SermonDocumentModel> findBySpeaker(final String speaker, final Pageable pageable);



}
