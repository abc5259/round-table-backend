package com.roundtable.roundtable.implement.house;

import com.roundtable.roundtable.global.exception.HouseException.HouseNotFoundException;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.house.HouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HouseReader {
    private final HouseRepository houseRepository;

    public House findById(Long houseId) {
        return houseRepository.findById(houseId)
                .orElseThrow(HouseNotFoundException::new);
    }

}
