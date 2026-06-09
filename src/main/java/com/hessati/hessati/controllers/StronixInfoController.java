package com.hessati.hessati.controllers;

import com.hessati.hessati.dto.ShareProductDTO;
import com.hessati.hessati.dto.StronixInfoDTO;
import com.hessati.hessati.entities.StronixInfo;
import com.hessati.hessati.services.StronixInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stronix-info")
public class StronixInfoController {

    @Autowired
    private StronixInfoService stronixInfoService;

    @GetMapping()
    public ResponseEntity<StronixInfo> getStronixInfo() {
        StronixInfo info = stronixInfoService.getStronixInfo();
        return info != null
                ? new ResponseEntity<>(info, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping()
    public ResponseEntity<StronixInfo> updateStronixInfo(@RequestBody StronixInfoDTO stronixInfoDTO) {
        StronixInfo updated = stronixInfoService.updateStronixInfo(stronixInfoDTO);
        return updated != null
                ? new ResponseEntity<>(updated, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/share-product")
    public ResponseEntity<Boolean> shareProduct(@RequestBody ShareProductDTO shareProductInfo) {
        Boolean sended = stronixInfoService.shareProduct(shareProductInfo);
        return sended != null
                ? new ResponseEntity<>(sended, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
