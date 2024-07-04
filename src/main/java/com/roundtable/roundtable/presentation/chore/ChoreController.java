package com.roundtable.roundtable.presentation.chore;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/house/{houseId}/chore")
public class ChoreController {

    @PatchMapping("/{choreId}")
    public void completeChore(
            @PathVariable("houseId") Long houseId,
            @PathVariable("choreId") Long choreId,
            @RequestPart("completeImage") MultipartFile completeImage) {
        
    }
}
