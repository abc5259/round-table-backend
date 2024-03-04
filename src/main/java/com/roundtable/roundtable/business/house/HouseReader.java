package com.roundtable.roundtable.business.house;

import static com.roundtable.roundtable.global.exception.errorcode.HouseErrorCode.*;

import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
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
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND));
    }

}
