package com.example.baggage.bungchan.service;

import com.example.baggage.domain.Lodgment;
import lombok.RequiredArgsConstructor;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BcLodgmentService {
    @PersistenceContext
    private EntityManager em;


    public Lodgment lodgmentCompare(String name, String region){

        String query = "SELECT license , address , name FROM LODGING "
                + "WHERE address LIKE CONCAT('%', :region, '%') AND name = :name";

        List<Lodgment> result = em.createNativeQuery(query)
                .setParameter("region", region)
                .setParameter("name", name)
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(Transformers.aliasToBean(Lodgment.class))
                .getResultList();

        if (!result.isEmpty())
        {
            result.get(0).setCode(0);
            return result.get(0);
        }else {
            Lodgment lodgment = new Lodgment();
            lodgment.setCode(1);
            return lodgment;
        }

    }
}
