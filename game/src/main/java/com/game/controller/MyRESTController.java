package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import com.game.service.PlayerServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class MyRESTController {

    private PlayerService playerService;

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping(value = "/rest/players")
    public ResponseEntity<?> getAllPlayers(@RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "title", required = false) String title,
                                           @RequestParam(value = "after", required = false) Long after,
                                           @RequestParam(value = "before", required = false) Long before,
                                           @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                           @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                           @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                           @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                           @RequestParam(value = "race", required = false) Race race,
                                           @RequestParam(value = "profession", required = false) Profession profession,
                                           @RequestParam(value = "banned", required = false) Boolean banned,
                                           @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize,
                                           @RequestParam(value = "order", required = false) PlayerOrder playerOrder
                                           ) {

        List<Player> playerList = PlayerServiceHelper.findByPagingCriteria(playerService,
                name, title, after,
                before, minExperience,maxExperience
                ,minLevel, maxLevel,
                race, profession,banned,
                pageNumber, pageSize,playerOrder);

        return new ResponseEntity<>(playerList, HttpStatus.OK);
    }

    @GetMapping(value = "/rest/players/count")
    public ResponseEntity<?> getCount(@RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "title", required = false) String title,
                                      @RequestParam(value = "after", required = false) Long after,
                                      @RequestParam(value = "before", required = false) Long before,
                                      @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                      @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                      @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                      @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                      @RequestParam(value = "race", required = false) Race race,
                                      @RequestParam(value = "profession", required = false) Profession profession,
                                      @RequestParam(value = "banned", required = false) Boolean banned) {
        int count = PlayerServiceHelper.countByCriteria(playerService,
                name, title, after,
                before, minExperience,maxExperience
                ,minLevel, maxLevel,
                race, profession,banned);

        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    /*@PostMapping(value = "rest/players")
    public ResponseEntity<?> createNewPlayer(@RequestBody Player player) {
        if (!PlayerServiceHelper.validatorPlayer(player)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return playerService.create(player);
    }*/
    @PostMapping(value = "/rest/players")
    public ResponseEntity<?> createShip(@RequestBody Player player) {
        System.out.println(player);
        return playerService.create(player);
    }

    @GetMapping(value = "/rest/players/{id}")
    public ResponseEntity<?> findShipById(@PathVariable String id) {
        return playerService.findById(id);
    }

    @DeleteMapping(value = "/rest/players/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return playerService.deleteById(id);
    }

    @PostMapping(value = "/rest/players/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Player player) {
        ResponseEntity<?> responseEntity = playerService.update(id, player);
        Player player1 = (Player) responseEntity.getBody();
        return responseEntity;
    }
}

