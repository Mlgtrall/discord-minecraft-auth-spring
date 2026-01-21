package com.mlgtrall.springappdemo.repository;

import com.mlgtrall.springappdemo.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Long> {

    Optional<Player> findByMinecraftName(String name);

    Optional<Player> findByMinecraftUuid(UUID minecraftUuid);

    Optional<Player> findByDiscordId(Long id);
}
