package com.mattmartin.faithbible.audiosearchapi.elasticsearch.repositories;

import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SeriesModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SeriesRepository extends ElasticsearchRepository<SeriesModel, String> {

}
