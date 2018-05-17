package com.mattmartin.faithbible.audiosearchapi.repositories;

import com.mattmartin.faithbible.audiosearchapi.models.SermonDocumentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SermonRepository extends ElasticsearchRepository<SermonDocumentModel, String> {

    Page<SermonDocumentModel> findBySeries(final String series, final Pageable pageable);

    Page<SermonDocumentModel> findBySpeaker(final String speaker, final Pageable pageable);



}
