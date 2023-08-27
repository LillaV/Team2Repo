package de.msg.javatraining.donationmanager.service.filter;

import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class CampaignSpecifications {
    public Specification<Campaign> filterCampaigns(
            String nameTerm, String purposeTerm
    ) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nameTerm != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                        "%" + nameTerm.toLowerCase() + "%"));
            }

            if (purposeTerm != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("purpose")),
                        "%" + purposeTerm.toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
