package com.mattmartin.faithbible.audiosearchapi.services;

import com.mattmartin.faithbible.audiosearchapi.models.SermonDocumentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

interface SermonService {

    SermonDocumentModel save(final SermonDocumentModel sermon);

    void delete(final SermonDocumentModel sermonDocumentModel);

    Page<SermonDocumentModel> findBySpeaker(final String speaker, final PageRequest pageRequest);

    Page<SermonDocumentModel> findBySeries(final String series, final PageRequest pageRequest);

    Page<SermonDocumentModel> findByFreeSearch(final String querey, final PageRequest pageRequest);
}
