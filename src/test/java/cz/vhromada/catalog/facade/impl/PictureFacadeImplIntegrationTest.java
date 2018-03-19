package cz.vhromada.catalog.facade.impl;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.entity.Picture;
import cz.vhromada.catalog.facade.CatalogParentFacade;
import cz.vhromada.catalog.facade.PictureFacade;
import cz.vhromada.catalog.utils.PictureUtils;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * A class represents integration test for class {@link PictureFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
class PictureFacadeImplIntegrationTest extends AbstractParentFacadeIntegrationTest<Picture, cz.vhromada.catalog.domain.Picture> {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of {@link PlatformTransactionManager}
     */
    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * Instance of {@link PictureFacade}
     */
    @Autowired
    private PictureFacade pictureFacade;

    /**
     * Test method for {@link PictureFacade#add(Picture)} with picture with null content.
     */
    @Test
    void add_NullContent() {
        final Picture picture = newData(null);
        picture.setContent(null);

        final Result<Void> result = pictureFacade.add(picture);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "PICTURE_CONTENT_NULL", "Content mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link PictureFacade#update(Picture)} with picture with null content.
     */
    @Test
    void update_NullContent() {
        final Picture picture = newData(1);
        picture.setContent(null);

        final Result<Void> result = pictureFacade.update(picture);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "PICTURE_CONTENT_NULL", "Content mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    @Override
    protected CatalogParentFacade<Picture> getCatalogParentFacade() {
        return pictureFacade;
    }

    @Override
    protected Integer getDefaultDataCount() {
        return PictureUtils.PICTURES_COUNT;
    }

    @Override
    protected Integer getRepositoryDataCount() {
        return PictureUtils.getPicturesCount(entityManager);
    }

    @Override
    protected List<cz.vhromada.catalog.domain.Picture> getDataList() {
        return PictureUtils.getPictures();
    }

    @Override
    protected cz.vhromada.catalog.domain.Picture getDomainData(final Integer index) {
        return PictureUtils.getPicture(index);
    }

    @Override
    protected Picture newData(final Integer id) {
        return PictureUtils.newPicture(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Picture newDomainData(final Integer id) {
        return PictureUtils.newPictureDomain(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Picture getRepositoryData(final Integer id) {
        return PictureUtils.getPicture(entityManager, id);
    }

    @Override
    protected String getName() {
        return "Picture";
    }

    @Override
    protected void clearReferencedData() {
        final TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        entityManager.createNativeQuery("UPDATE movies SET picture = NULL").executeUpdate();
        entityManager.createNativeQuery("UPDATE tv_shows SET picture = NULL").executeUpdate();
        transactionManager.commit(transactionStatus);
    }

    @Override
    protected void assertDataListDeepEquals(final List<Picture> expected, final List<cz.vhromada.catalog.domain.Picture> actual) {
        PictureUtils.assertPictureListDeepEquals(expected, actual);
    }

    @Override
    protected void assertDataDeepEquals(final Picture expected, final cz.vhromada.catalog.domain.Picture actual) {
        PictureUtils.assertPictureDeepEquals(expected, actual);
    }

    @Override
    protected void assertDataDomainDeepEquals(final cz.vhromada.catalog.domain.Picture expected, final cz.vhromada.catalog.domain.Picture actual) {
        PictureUtils.assertPictureDeepEquals(expected, actual);
    }

}
