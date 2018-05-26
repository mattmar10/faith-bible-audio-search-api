package com.mattmartin.faithbible.audiosearchapi.elasticsearch.repositories;

import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SeriesModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ESSeriesRepository extends ElasticsearchRepository<SeriesModel, Integer> {

}
