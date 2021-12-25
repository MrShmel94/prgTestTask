package com.game.service;

import com.game.repository.PlayerDAO;
import com.game.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService{

    private PlayerDAO playerDAO;

    @Autowired
    public void setPlayerDAO(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }

    @Override
    public Page <Player> findAll(Specification<Player> specification, Pageable pageable) {
        return playerDAO.findAll(specification, pageable);
    }

    @Override
    public long count(Specification<Player> specification) {
        return playerDAO.count(specification);
    }

    @Override
    public ResponseEntity<?> create(Player player) {
        if (!PlayerServiceHelper.validatorPlayer(player)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Player createPlayer = playerDAO.save(player);
        return new ResponseEntity<>(createPlayer, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> findById(String idString) {
        Long id;
        if ((id = validateId(idString)) == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Player> player = playerDAO.findById(id);
        if (!player.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(player.get(), HttpStatus.OK);
    }

    private Long validateId(String id) {
        try {
            Long idLong = Long.parseLong(id);
            if (idLong <= 0) return null;
            else return idLong;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public ResponseEntity<?> deleteById(String idString) {
        Long id;
        if ((id = validateId(idString)) == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (!playerDAO.existsById(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            playerDAO.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> update(String idString, Player player) {
        //Если id не валидный
        Long id;
        if ((id = validateId(idString)) == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        //Если элемента с таким id нет в базе
        if (!playerDAO.existsById(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            Player playerFromBase = playerDAO.findById(id).get();

        // Если тело запроса пустое
        if (player.getName() == null && player.getTitle() == null
                && player.getRace() == null && player.getProfession() == null
                && player.getBirthday() == null && player.getBanned() == null && player.getExperience() == null)
            //Возвращяем игрока из базы
            player = playerFromBase;
        else {
            //Если поля не валидные
            /*if (!PlayerServiceHelper.validatingFields(player)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }*/
            if ((player.getName() != null && (player.getName().length() > 12 || player.getName().equals("")))
                    || (player.getTitle() != null && (player.getTitle().length() > 30 || player.getTitle().equals("")))
                    || (player.getExperience() != null && (player.getExperience() < 0 || player.getExperience() > 10_000_000))
                    || (player.getBirthday() != null && (player.getBirthday().getYear() + 1900 < 2000 || player.getBirthday().getYear() + 1900 > 3000 || player.getBirthday().getTime() < 0)))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            if (player.getName() != null) playerFromBase.setName(player.getName());
            if (player.getTitle() != null) playerFromBase.setTitle(player.getTitle());
            if (player.getRace() != null) playerFromBase.setRace(player.getRace());
            if (player.getProfession() != null) playerFromBase.setProfession(player.getProfession());
            if (player.getBirthday() != null) playerFromBase.setBirthday(player.getBirthday());
            if (player.getBanned() != null) playerFromBase.setBanned(player.getBanned());
            if (player.getExperience() != null) playerFromBase.setExperience(player.getExperience());
            playerFromBase.setLevel(PlayerServiceHelper.calculationLevel(playerFromBase));
            playerFromBase.setUntilNextLevel(PlayerServiceHelper.calculationExp(playerFromBase));
            playerDAO.save(playerFromBase);
        }
            return new ResponseEntity<>(playerFromBase, HttpStatus.OK);
        }
    }}
