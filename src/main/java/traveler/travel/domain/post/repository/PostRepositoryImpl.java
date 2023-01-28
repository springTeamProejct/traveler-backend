package traveler.travel.domain.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import traveler.travel.domain.account.enums.Gender;
import traveler.travel.domain.post.entity.Post;
import traveler.travel.domain.post.enums.Category;
import traveler.travel.global.dto.PostSearchCondition;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.function.Supplier;

import static traveler.travel.domain.post.entity.QPost.*;
import static traveler.travel.domain.post.entity.QTravel.travel;
import static traveler.travel.global.dto.PostSearchCondition.*;

public class PostRepositoryImpl implements PostSearchRepository{
    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Post> search(PostSearchCondition condition, Pageable pageable) {

        List<Post> result = queryFactory
                .selectFrom(post)
                .leftJoin(post.travel, travel)
                .where(
                        notDeleted(),
                        categoryEq(condition.getCategory()),
                        minAgeGoe(condition.getMinAge()),
                        maxAgeLoe(condition.getMaxAge()),
                        gatherYn(condition.getGatherYn()),
                        genderEq(condition.getGender()),
                        isSearchable(condition.getKeywordType(), condition.getKeyword())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .leftJoin(post.travel, travel)
                .where(
                        notDeleted(),
                        categoryEq(condition.getCategory()),
                        minAgeGoe(condition.getMinAge()),
                        maxAgeLoe(condition.getMaxAge()),
                        gatherYn(condition.getGatherYn()),
                        genderEq(condition.getGender()),
                        isSearchable(condition.getKeywordType(), condition.getKeyword())
                );


        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }

    private BooleanBuilder isSearchable(KeywordType keywordType, String keyword) {

        if (keywordType == KeywordType.TITLE) {
            return titleCt(keyword);
        } else if (keywordType == KeywordType.CONTENT) {
            return contentCt(keyword);
        } else if (keywordType == KeywordType.LOCATION) {
            return locationCt(keyword);
        } else if (keywordType == KeywordType.ALL || keywordType == null) {
            return titleCt(keyword).or(contentCt(keyword)).or(locationCt(keyword));
        } else {
            return null;
        }
    }
    private BooleanBuilder categoryEq(Category category) {
        return nullSafeBuilder(() -> post.category.eq(category));
    }

    private BooleanBuilder titleCt(String title) {
        return nullSafeBuilder(() -> post.title.contains(title));
    }

    private BooleanBuilder contentCt(String content) {
        return nullSafeBuilder(() -> post.content.contains(content));
    }

    private BooleanBuilder locationCt(String location) {
        return nullSafeBuilder(() -> travel.location.contains(location));
    }

    private BooleanBuilder gatherYn(Boolean gatherYn) {
        return nullSafeBuilder(() -> travel.gatherYn.eq(gatherYn));
    }

    private BooleanBuilder maxAgeLoe(Integer maxAge) {
        return nullSafeBuilder(() -> travel.maxAge.loe(maxAge));
    }

    private BooleanBuilder minAgeGoe(Integer minAge) {
        return nullSafeBuilder(() -> travel.minAge.goe(minAge));
    }

    private BooleanBuilder genderEq(Gender gender) {
        return nullSafeBuilder(() -> travel.gender.eq(gender));
    }

    private BooleanBuilder notDeleted() {
        return nullSafeBuilder(post.deletedAt::isNull);
    }


    private BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> f) {
        try {
            return new BooleanBuilder(f.get());
        } catch (IllegalArgumentException | NullPointerException e) {
            return new BooleanBuilder();
        }
    }
}
