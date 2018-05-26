package com.mattmartin.faithbible.audiosearchapi.db.repositories;

import com.mattmartin.faithbible.audiosearchapi.db.models.SeriesDBModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SeriesModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeriesRepository  extends CrudRepository<SeriesDBModel, Integer> {
    Optional<SeriesDBModel> findBySlug(final String slug);
}
