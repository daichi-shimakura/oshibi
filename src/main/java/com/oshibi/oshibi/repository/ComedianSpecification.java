package com.oshibi.oshibi.repository;

import com.oshibi.oshibi.domain.entity.ComedianProfile;
import org.springframework.data.jpa.domain.Specification;

public class ComedianSpecification {
    public static Specification<ComedianProfile> keywordSearch(String keyword){
        return (root, query, cb) ->
                cb.like(root.join("account").get("displayName"), "%" + keyword + "%");
    }

    public static Specification<ComedianProfile> unitType(String unitType){
        return (root, query, cb) ->
                cb.equal(root.get("unitType"), unitType);
    }

    public static Specification<ComedianProfile> agency(String agency){
        return (root, query, cb) ->
                cb.like(root.get("agency"), "%" + agency + "%");
    }
}
