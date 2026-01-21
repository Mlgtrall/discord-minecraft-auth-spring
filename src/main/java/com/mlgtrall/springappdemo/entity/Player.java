package com.mlgtrall.springappdemo.entity;

import jakarta.persistence.*; //TODO: изучить
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity //TODO: изучить
@Table(name = "players")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Id
    @Column(name = "discord_id", unique = true, nullable = false)
    private Long discordId;

    @Column(name = "minecraft_uuid", unique = true)
    private UUID minecraftUuid;

    @Column(name = "minecraft_name")
    private String minecraftName;

    @Column(name = "is_verified", nullable = false)
    @Builder.Default
    private boolean isVerified = false;

    @Column(name = "is_banned", nullable = false)
    @Builder.Default
    private boolean isBanned = false;

    @Column(name = "is_admin", nullable = false)
    @Builder.Default
    private boolean isAdmin = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Автоматическая установка даты при создании записи
    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}
