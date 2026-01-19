package com.mlgtrall.springappdemo.service;

import com.mlgtrall.springappdemo.entity.Player;
import com.mlgtrall.springappdemo.model.AuthResponse;
import com.mlgtrall.springappdemo.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor // Автоматически внедряет Repository через конструктор
public class PlayerService {

    private final PlayerRepository playerRepository;


    /**
     * Метод для Discord-бота: предварительная регистрация
     */
    //TODO: Сделать админ-команду для регистрации других, потому что сейчас это регистрация пользователем с любым доступом!
    @Transactional
    public AuthResponse registerPlayer(String discordId, String minecraftName) {
        log.debug("PlayerService.registerPlayer Discord ID, Minecraft Name ({}, {})", discordId, minecraftName);

        //Уже зарегистрирован. Исключает повторную регистрацию с того же аккаунта
        if(playerRepository.findByDiscordId(discordId).isPresent())
            return new AuthResponse(true, AuthResponse.ResponseCode.REGISTERED_ALREADY_DISCORD);

        //Имя уже занято. Исключает дублирование имен
        if(playerRepository.findByMinecraftName(minecraftName).isPresent())
            return new AuthResponse(true, AuthResponse.ResponseCode.NAME_TAKEN);

        //Создание и сохранение объекта
        Player player = Player.builder()
                .discordId(discordId)
                .minecraftName(minecraftName)
                .isVerified(true)
                .build();

        log.info("New registration: DiscordID {}, MC Name {}", discordId, minecraftName);
        playerRepository.save(player);
        return new AuthResponse(true, AuthResponse.ResponseCode.REGISTER_SUCCESS);
    }



    /**
     * Основной метод проверки доступа для плагина Minecraft.
     * Реализует логику "ленивой" привязки UUID.
     */
    @Transactional
    public AuthResponse checkAccess(UUID currentUuid, String minecraftName){
        log.debug("PlayerService checkAccess called.");
        log.info("Checking access for player: {} with UUID: {}", minecraftName, currentUuid);

        // 1. Пытаемся найти по UUID (повторный вход)
        Optional<Player> playerByUuid = playerRepository.findByMinecraftUuid(currentUuid);
        if(playerByUuid.isPresent()){
            log.info("Player found by UUID: {}", currentUuid);
            return processPlayerAuth(playerByUuid.get());
        }

        // 2. Если по UUID не нашли, ищем по нику (первый вход)
        Optional<Player> playerByName = playerRepository.findByMinecraftName(minecraftName);
        if(playerByName.isPresent()){
            log.info("Player found by name: {}", minecraftName);
            Player player = playerByName.get();

            // Проверяем, не привязан ли уже к этому нику другой UUID
            if(player.getMinecraftUuid() == null){
                player.setMinecraftUuid(currentUuid); //TODO: КОСТЫЛЬ!
                player.setVerified(true);
                playerRepository.save(player);

                log.info("Fist login for player {}, UUID {} has been linked", minecraftName, currentUuid);
                return new AuthResponse(true, AuthResponse.ResponseCode.FIRST_LOGIN_SUCCESS);
            } else {
                //Кто-то пытается зайти под чужим ником
                log.warn("UUID mismatch for player {}. Expected {}, but got {}",
                        minecraftName, player.getMinecraftUuid(), currentUuid);
                return new AuthResponse(false, AuthResponse.ResponseCode.UUID_MISMATCH);

            }
        }
        log.info("Player not found in database: {}", minecraftName);
        return new AuthResponse(false, AuthResponse.ResponseCode.NOT_FOUND);

    }


    @Transactional
    public AuthResponse banPlayer(String minecraftName){
        log.debug("PlayerService banPlayer called.");
        Optional<Player> playerByName = playerRepository.findByMinecraftName(minecraftName);
        if(playerByName.isEmpty()) return new AuthResponse(false, AuthResponse.ResponseCode.NOT_FOUND);
        Player player = playerByName.get();
        if(player.isBanned()) {
            log.info("Player is already banned: {}", minecraftName);
            return new AuthResponse(false, AuthResponse.ResponseCode.BANNED);
        }
        return banPlayer(player);
    }

    @Transactional
    public AuthResponse banPlayer(UUID minecraftUUID){
        log.debug("PlayerService banPlayer called.");
        Optional<Player> playerByName = playerRepository.findByMinecraftUuid(minecraftUUID);
        if(playerByName.isEmpty()) return new AuthResponse(false, AuthResponse.ResponseCode.NOT_FOUND);
        Player player = playerByName.get();
        if(player.isBanned()) {
            log.info("Player with UUID is already banned: {}", minecraftUUID);
            return new AuthResponse(false, AuthResponse.ResponseCode.BANNED);
        }
        return banPlayer(player);
    }


    @Transactional
    @Contract("_ -> new")
    private @NonNull AuthResponse banPlayer(@NonNull Player player){
        log.info("Banning player: {}", player.getMinecraftName());
        player.setBanned(true);
        player.setVerified(false);
        playerRepository.save(player);
        log.info("Player banned: {}", player.getMinecraftName());
        return new AuthResponse(false, AuthResponse.ResponseCode.BANNED);
    }



    /**
     * Вспомогательный метод для проверки статуса игрока (бан, верификация и т.д.)
     */
    @Contract("_ -> new")
    private @NonNull AuthResponse processPlayerAuth(@NonNull Player player){
        if(player.isBanned()) return new AuthResponse(false, AuthResponse.ResponseCode.BANNED);
        if(!player.isVerified()) return new AuthResponse(false, AuthResponse.ResponseCode.NOT_VERIFIED);

        //player.setVerified(true); //TODO: Подстраховка. Нужна ли?
        return new AuthResponse(true,  AuthResponse.ResponseCode.SUCCESS);
    }


}
