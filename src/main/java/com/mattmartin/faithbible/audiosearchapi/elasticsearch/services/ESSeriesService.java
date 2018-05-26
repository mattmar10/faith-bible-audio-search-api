package com.mattmartin.faithbible.audiosearchapi.elasticsearch.services;

import com.github.vanroy.springdata.jest.JestElasticsearchTemplate;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SeriesModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SermonDocumentModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.repositories.ESSeriesRepository;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.repositories.ESSermonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@Service
public class ESSeriesService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ESSeriesRepository esSeriesRepository;
    private final ESSermonRepository esSermonRepository;
    private final JestElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public ESSeriesService(final ESSeriesRepository repo,
                           final ESSermonRepository ESSermonRepository,
                           final JestElasticsearchTemplate elasticsearchTemplate){

        this.esSeriesRepository = repo;
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.esSermonRepository = ESSermonRepository;
    }

    public void deleteAll(){
        esSeriesRepository.deleteAll();
    }

    public SeriesModel save(final SeriesModel seriesModel) {
        logger.info(String.format("Persisting series [%s].", seriesModel));
        return esSeriesRepository.save(seriesModel);
    }

    public Optional<SeriesModel> findById(final int id){
        logger.info(String.format("Fetching details for series [%s].", id));
        return esSeriesRepository.findById(id).map(s -> validateSeriesSermons(s));
    }

    public List<SeriesModel> findMostRecentSeries(final int count){
        final SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withPageable(PageRequest.of(0, 200, Sort.Direction.DESC, "date"))
                .build();

        final Page<SermonDocumentModel> results = esSermonRepository.search(searchQuery);

        final List<SeriesModel> seriesSet = new ArrayList<>();
        for(SermonDocumentModel documentModel: results){
            documentModel.getSeriesId().ifPresent(seriesId -> {
                final Optional<SeriesModel> series = esSeriesRepository.findById(seriesId);

                series.ifPresent(s -> {
                    if(!seriesSet.contains(s)){
                        seriesSet.add(validateSeriesSermons(s));
                    }
                });

            });



            if(seriesSet.size() >= count){
                break;
            }
        }

        return seriesSet;

    }

    private SeriesModel validateSeriesSermons(final SeriesModel seriesModel){


        final List<SermonDocumentModel> validSermons =
                seriesModel.getSermons()
                        .stream()
                        .filter(s -> s.getMedia().getMp3() != null)
                        .collect(Collectors.toList());

        seriesModel.setSermons(validSermons);

        return seriesModel;
    }

}
