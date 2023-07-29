package com.example.baggage.bungchan.service;

import com.example.baggage.dto.LodgmentDTO;
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


    public LodgmentDTO lodgmentCompare(String name, String region){

        String query = "SELECT license , address , name FROM LODGING "
                + "WHERE address LIKE CONCAT('%', :region, '%') AND name = :name";

        List<LodgmentDTO> result = em.createNativeQuery(query)
                .setParameter("region", region)
                .setParameter("name", name)
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(Transformers.aliasToBean(LodgmentDTO.class))
                .getResultList();

        if (!result.isEmpty())
        {
            result.get(0).setCode(0);
            return result.get(0);
        }else {
            LodgmentDTO lodgmentDTO = new LodgmentDTO();
            lodgmentDTO.setCode(1);
            return lodgmentDTO;
        }

    }
}
