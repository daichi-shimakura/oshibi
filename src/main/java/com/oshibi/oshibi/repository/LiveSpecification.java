package com.oshibi.oshibi.repository;

import com.oshibi.oshibi.domain.entity.Live;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;

public class LiveSpecification {

    private LiveSpecification() {}
    private static final String START_TIME = "startTime";

    public static Specification<Live> keywordSearch(String keyword) {
        return (root, query, cb) -> {
            if (query != null) {
                query.distinct(true);
            }
            var performers = root.join("livePerformers", JoinType.LEFT);
            var account = performers.join("comedian", JoinType.LEFT);
            return cb.or(
                    cb.like(root.get("title"), "%" + keyword + "%"),
                    cb.like(account.get("displayName"), "%" + keyword + "%")
            );
        };
    }

    public static Specification<Live> dateAfter(LocalDate dateFrom) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("date"), dateFrom);
    }

    public static Specification<Live> dateBefore(LocalDate dateTo) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("date"), dateTo);
    }

    public static Specification<Live> futureOnly() {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("date"), LocalDate.now());
    }

    public static Specification<Live> prefecture(String prefecture) {
        return (root, query, cb) ->
                cb.equal(root.join("venue").get("prefecture"), prefecture);
    }

    public static Specification<Live> liveType(String liveType) {
        return (root, query, cb) ->
                cb.equal(root.get("liveType"), liveType);
    }

    public static Specification<Live> maxPrice(Integer maxPrice) {
        return (root, query, cb) ->
                cb.or(
                        cb.lessThanOrEqualTo(root.get("priceAdvance"), maxPrice),
                        cb.lessThanOrEqualTo(root.get("priceDoor"), maxPrice)
                );
    }

    public static Specification<Live> startTimeSlot(String slot) {
        return (root, query, cb) -> switch (slot) {
            case "~17:00" -> cb.lessThan(root.get(START_TIME), LocalTime.of(17, 0));
            case "17:00~18:00" -> cb.between(root.get(START_TIME), LocalTime.of(17, 0), LocalTime.of(18, 0));
            case "18:00~19:00" -> cb.between(root.get(START_TIME), LocalTime.of(18, 0), LocalTime.of(19, 0));
            case "19:00~20:00" -> cb.between(root.get(START_TIME), LocalTime.of(19, 0), LocalTime.of(20, 0));
            case "20:00~" -> cb.greaterThanOrEqualTo(root.get(START_TIME), LocalTime.of(20, 0));
            default -> cb.conjunction();
        };

    }
}
