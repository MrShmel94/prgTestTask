package com.game.service;

import com.game.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PlayerService {

   //List<Player> findAll(Pageable pageable);
   Page findAll(Specification<Player> playerSpecification, Pageable pageable);
   long count(Specification<Player> specification);
   ResponseEntity<?> create (Player player);
   ResponseEntity<?> findById(String id);
   ResponseEntity<?> deleteById(String id);
   ResponseEntity<?> update(String id, Player player);

}
