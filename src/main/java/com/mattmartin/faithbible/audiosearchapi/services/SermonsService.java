package com.mattmartin.faithbible.audiosearchapi.services;

import com.mattmartin.faithbible.audiosearchapi.db.models.SeriesDBModel;
import com.mattmartin.faithbible.audiosearchapi.db.models.SermonDBModel;
import com.mattmartin.faithbible.audiosearchapi.db.repositories.SeriesRepository;
import com.mattmartin.faithbible.audiosearchapi.db.repositories.SermonRepository;
import com.mattmartin.faithbible.audiosearchapi.dtos.Sermon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

@Service
public class SermonsService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private SermonRepository sermonRepository;
    private SeriesService seriesService;

    @Autowired
    public SermonsService(final SermonRepository repository, final SeriesService seriesService){
        this.sermonRepository = repository;
        this.seriesService = seriesService;
    }

    public Optional<SermonDBModel> updateSermon(final SermonDBModel toUpdate, final Sermon toUpdateFrom){

        final SermonDBModel newSermon = convertFromDTOtoModel(toUpdateFrom);
        logger.info("Updating sermon %s to %s", toUpdate, newSermon);

        final SermonDBModel updated = toUpdate.updateFromOtherSermonDBModel(newSermon);
        return Optional.of(sermonRepository.save(updated));

    }

    private SermonDBModel convertFromDTOtoModel(final Sermon sermon){

        final Optional<SeriesDBModel> seriesDBModel =
                sermon.getSeriesSlug().flatMap(slug -> seriesService.findBySlug(slug));

        final SermonDBModel sermonDBModel = new SermonDBModel();
        sermonDBModel.setId(sermon.getId());
        sermonDBModel.setTitle(sermon.getTitle());
        sermonDBModel.setSlug(sermon.getSlug());
        sermonDBModel.setSpeaker(sermon.getSpeaker());

        seriesDBModel.ifPresent(seriesModel-> {
            sermonDBModel.setSeries(seriesModel);
        });

        sermonDBModel.setDate(sermon.getDate());

        sermon.getMp3URI().ifPresent(mp3URL -> sermonDBModel.setMp3Url(mp3URL.toString()));
        sermon.getImageURI().ifPresent(imageURI -> sermonDBModel.setImageUrl(imageURI.toString()));
        sermon.getPdfURI().ifPresent(pdfURI -> sermonDBModel.setPdfUrl(pdfURI.toString()));

        sermon.getStats().ifPresent(stats -> {
            stats.getLikes().ifPresent(likes -> sermonDBModel.setLikes(likes));
            stats.getPlays().ifPresent(plays -> sermonDBModel.setPlays(plays));
            stats.getShares().ifPresent(shares -> sermonDBModel.setShares(shares));
        });

        sermon.getTags().ifPresent(tags -> sermonDBModel.setTags(new ArrayList<>(tags)));
        return sermonDBModel;
    }

    public SermonDBModel save(final SermonDBModel sermon){
        logger.info(String.format("Persisting to db sermon: %s", sermon));
        return this.sermonRepository.save(sermon);
    }

    public Optional<SermonDBModel> findById(final int sermonId){
        logger.info(String.format("Fetching sermon from db with id: %d", sermonId));
        return this.sermonRepository.findById(sermonId);
    }

    public Optional<SermonDBModel> findBySlug(final String slug){
        logger.info(String.format("Fetching sermon from db with slug: %s", slug));
        return this.sermonRepository.findBySlug(slug);
    }

    public Iterable<SermonDBModel> getAllSermons(){
        logger.info("Fetching all sermons");
        return this.sermonRepository.findAll();
    }
}
