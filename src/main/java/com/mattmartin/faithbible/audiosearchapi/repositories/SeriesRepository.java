package com.mattmartin.faithbible.audiosearchapi.repositories;

import com.mattmartin.faithbible.audiosearchapi.models.SeriesModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SeriesRepository extends ElasticsearchRepository<SeriesModel, String> {

}
