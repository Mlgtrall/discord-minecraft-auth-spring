package com.mlgtrall.springappdemo.repository;

import com.mlgtrall.springappdemo.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Long> {

    // Spring сам создаст SQL запрос: SELECT * FROM players WHERE minecraft_name = ?
    Optional<Player> findByMinecraftName(String name);

    Optional<Player> findByMinecraftUuid(UUID minecraftUuid);

    // Можно также искать по Discord ID
    Optional<Player> findByDiscordId(String id);
}
