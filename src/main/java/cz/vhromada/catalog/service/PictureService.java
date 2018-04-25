package cz.vhromada.catalog.service;

import cz.vhromada.catalog.domain.Picture;
import cz.vhromada.catalog.repository.PictureRepository;
import cz.vhromada.common.service.AbstractMovableService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

/**
 * A class represents service for pictures.
 *
 * @author Vladimir Hromada
 */
@Component("pictureService")
public class PictureService extends AbstractMovableService<Picture> {

    /**
     * Creates a new instance of PictureService.
     *
     * @param pictureRepository repository for pictures
     * @param cache             cache
     * @throws IllegalArgumentException if repository pictures is null
     *                                  or cache is null
     */
    @Autowired
    public PictureService(final PictureRepository pictureRepository, @Value("#{cacheManager.getCache('catalogCache')}") final Cache cache) {
        super(pictureRepository, cache, "pictures");
    }

    @Override
    protected Picture getCopy(final Picture data) {
        final Picture newPicture = new Picture();
        newPicture.setContent(data.getContent());
        newPicture.setPosition(data.getPosition());

        return newPicture;
    }

}
