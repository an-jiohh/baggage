package com.example.baggage.jungchan.repository;

import com.example.baggage.jungchan.domain.Review;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ReviewRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Review review){
        em.persist(review);
    }

    public List<Review> findAll() {
        return em.createQuery("select m from Review m", Review.class)
                .getResultList();
    }

    public List<Review> findByRegion(String region){
        return em.createQuery("select m from Review m where m.region = :region",
                        Review.class)
                .setParameter("region", region)
                .getResultList();
    }

    public double getAvg(String region){
        double region1;
        try{
             region1 = em.createQuery("select AVG(m.rate) from Review m where m.region = :region",
                        Double.class)
                .setParameter("region", region)
                .getSingleResult();
        }catch (Exception e){
            region1 = 0;
        }

        return region1;
    }

}
