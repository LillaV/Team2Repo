package de.msg.javatraining.donationmanager.service.utils;

import de.msg.javatraining.donationmanager.persistence.model.Donation;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DonationSpecifications {
    public static Specification<Donation> filterDonations(
            Float minValue, Float maxValue, Float value, String currency,
            Long campaignId, String searchTerm, Long createdById,
            LocalDate createDate, LocalDate startDate, LocalDate endDate,
            Long benefactorId, Boolean approved,
            Long approvedById, LocalDate approvedDateStart, LocalDate approvedDateEnd
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            System.out.println(minValue + " " + maxValue);

            if (minValue != null && maxValue != null) {
                predicates.add(criteriaBuilder.between(root.get("amount"), minValue, maxValue));
            } else if (minValue != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), minValue));
            } else if (maxValue != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("amount"), maxValue));
            }

            if (value != null) {
                predicates.add(criteriaBuilder.equal(root.get("amount"), value));
            }

            if (currency != null) {
                predicates.add(criteriaBuilder.equal(root.get("currency"), currency));
            }

            if (campaignId != null) {
                predicates.add(criteriaBuilder.equal(root.get("campaign").get("id"), campaignId));
            }

            if (searchTerm != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("campaign").get("purpose")),
                        "%" + searchTerm.toLowerCase() + "%"));
            }

            if (createdById != null) {
                predicates.add(criteriaBuilder.equal(root.get("createdBy").get("id"), createdById));
            }

            if (createDate != null) {
                predicates.add(criteriaBuilder.equal(root.get("createDate"), createDate));
            }

            if (startDate != null && endDate != null) {
                predicates.add(criteriaBuilder.between(root.get("createDate"), startDate, endDate));
            } else if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createDate"), startDate));
            } else if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createDate"), endDate));
            }

            if (benefactorId != null) {
                predicates.add(criteriaBuilder.equal(root.get("benefactor").get("id"), benefactorId));
            }

            if (approved != null) {
                predicates.add(criteriaBuilder.equal(root.get("approved"), approved));
            }

            if (approvedById != null) {
                predicates.add(criteriaBuilder.equal(root.get("approvedBy").get("id"), approvedById));
            }

            if (approvedDateStart != null && approvedDateEnd != null) {
                predicates.add(criteriaBuilder.between(root.get("approvedDate"), approvedDateStart, approvedDateEnd));
            } else if (approvedDateStart != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("approvedDate"), approvedDateStart));
            } else if (approvedDateEnd != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("approvedDate"), approvedDateEnd));
            }

            // Combine predicates
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
