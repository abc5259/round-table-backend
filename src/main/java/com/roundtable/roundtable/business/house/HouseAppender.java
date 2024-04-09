package com.roundtable.roundtable.business.house;

import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class HouseAppender {

    private final InviteCodeManager inviteCodeManager;

    private final HouseRepository houseRepository;

    public Long appendHouse(CreateHouse createHouse) {
        House house = House.of(createHouse.name(), inviteCodeManager.createInviteCode());
        houseRepository.save(house);

        return house.getId();
    }
}