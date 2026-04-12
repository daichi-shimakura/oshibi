package com.oshibi.oshibi.repository;

import com.oshibi.oshibi.domain.entity.ComedianProfile;
import com.oshibi.oshibi.domain.entity.UnitType;
import org.springframework.data.jpa.domain.Specification;

public class ComedianSpecification {
    private ComedianSpecification() {
    }

    public static Specification<ComedianProfile> keywordSearch(String keyword) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.join("account").get("displayName")), "%" + keyword.toLowerCase() + "%");
    }

    public static Specification<ComedianProfile> unitType(String unitType) {
        return (root, query, cb) ->
                cb.equal(root.get("unitType"), UnitType.valueOf(unitType));
    }

    public static Specification<ComedianProfile> agency(String agency) {
        return (root, query, cb) ->
                cb.like(root.get("agency"), "%" + agency + "%");
    }
}
