package com.zootopia.zootopia.services;

import com.zootopia.zootopia.dao.models.Places;
import com.zootopia.zootopia.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class PlacesServices {
    @Autowired
    PlaceRepository placeRepository;

    public Places findByName(String Place){
        return placeRepository.findByName(Place);
    }

    public void  save(Places place) {
        placeRepository.save(place);
    }

}
