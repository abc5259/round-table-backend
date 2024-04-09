package com.roundtable.roundtable.business.house;

import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.errorcode.HouseErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HouseValidator {

    private final HouseRepository houseRepository;

    public void validateExistHouseId(Long houseId) {
        if(!houseRepository.existsById(houseId)) {
            throw new NotFoundEntityException(HouseErrorCode.NOT_FOUND);
        }
    }
}
