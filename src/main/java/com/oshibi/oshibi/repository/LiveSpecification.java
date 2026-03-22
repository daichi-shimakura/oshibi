package com.oshibi.oshibi.repository;

import com.oshibi.oshibi.domain.entity.Live;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class LiveSpecification {
    public static Specification<Live> titleContains(String keyword) {
        return (root, query, cb) ->
                cb.like(root.get("title"), "%" + keyword + "%");
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
}
