package com.example.baggage.jiho.service;

import com.example.baggage.domain.Mobom;
import com.example.baggage.jiho.repository.MobomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MobomService {

    private final MobomRepository mobomRepository;

    //@PostConstruct
    public void initMobomData() throws IOException {
        if(mobomRepository.count() == 0){
            ClassPathResource resource = new ClassPathResource("mobomList.csv");
            List<Mobom> mobomList = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8).stream()
                    .map(line -> {
                        String[] split = line.split(",");
                        System.out.println("split = " + split);
                        return new Mobom(split[1], split[3], split[5]);
                    }).collect(Collectors.toList());
            mobomRepository.saveAll(mobomList);
        }
    }
}
