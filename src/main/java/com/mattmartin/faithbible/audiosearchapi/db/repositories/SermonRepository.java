package com.mattmartin.faithbible.audiosearchapi.db.repositories;

import com.mattmartin.faithbible.audiosearchapi.db.models.SeriesDBModel;
import com.mattmartin.faithbible.audiosearchapi.db.models.SermonDBModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SermonRepository extends CrudRepository<SermonDBModel, Integer> {

    List<SermonDBModel> findBySeries(final SeriesDBModel seriesDBModel);

    Optional<SermonDBModel> findBySlug(final String slug);

}
