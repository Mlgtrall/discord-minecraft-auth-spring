package com.mlgtrall.springappdemo.controller;

import com.mlgtrall.springappdemo.model.AuthResponse;
import com.mlgtrall.springappdemo.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/players") // Все методы будут начинаться с этого пути
@RequiredArgsConstructor
@Slf4j
public class PlayerController {

    private final PlayerService playerService;

//    // Метод для регистрации Discord-Бота: POST http://localhost:8080/api/players/register
//    @PostMapping("/register")
//    public Player register(@RequestParam String discordId,
//                           @RequestParam String minecraftName){
//        return playerService.registerPlayer(discordId, minecraftName);
//    }

    /**
     * Эндпоинт для плагина.
     * Вызов: GET /api/players/check/550e8400-e29b-41d4-a716-446655440000?name=Steve
     */
    @GetMapping("/check/{uuid}")
    public AuthResponse checkAccess(@PathVariable UUID uuid, @RequestParam String name){
        log.debug("PlayerController checkAccess called.");
        log.debug("Incoming GET request.");
        log.debug("Checking access for Minecraft UUID: {}, Minecraft Name: {}", uuid, name);
        AuthResponse response = playerService.checkAccess(uuid, name);
        log.debug("Returning access response: {}", response);
        return response;
    }

    /**
     * Эндпоинт для плагина.
     * Вызов: POST /api/players/ban
     * @param uuid - Minecraft UUID
     * @param name - Minecraft nickname
     */
    @PostMapping("/ban")
    public AuthResponse banPlayer(@RequestParam(name = "uuid") UUID uuid, @RequestParam(name = "nickname", required = false) String name){
        log.debug("PlayerController banPlayer called.");
        log.debug("Incoming POST request.");
        //TODO: Наверное не нужна проверка на null по умолчанию
        if(uuid == null){
            log.info("UUID is null.");
            return new AuthResponse(false, AuthResponse.ResponseCode.SERVER_ERROR.joinMessage("Bad Request! UUID is null."));
        }
        log.debug("Baning player: {}, Minecraft UUID: {}", uuid, name);
        AuthResponse response = playerService.banPlayer(uuid);
        log.debug("Returning ban response: {}", response);
        return response;
    }

//
//    @GetMapping("/check/{name}")
//    public AuthResponse checkAccess(String name){
//        return playerService.checkAccess(name);
//    }
//
//
//
//    @GetMapping("/check/{uuid}")
//    public AuthResponse checkAccess(UUID uuid){
//        return playerService.checkAccess(uuid);
//    }
//
}
