package com.mlgtrall.springappdemo.service;

import com.mlgtrall.springappdemo.entity.Player;
import com.mlgtrall.springappdemo.model.AuthResponse;
import com.mlgtrall.springappdemo.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    void shouldReturnAllowed_WhenPlayerExists(){
        //GIVEN (Дано)
        UUID testUuid = UUID.randomUUID();
        String testNickname = "TestPlayer";

        Player mockPlayer = Player.builder()
                .minecraftUuid(testUuid)
                .minecraftName(testNickname)
                .isVerified(true)
                .build();

        // Обучаем Mockito: "Когда вызовут findByUuid, верни нашего игрока"
        when(playerRepository.findByMinecraftUuid(testUuid)).thenReturn(Optional.of(mockPlayer));

        // WHEN (Когда выполняем действие)
        AuthResponse response = playerService.checkAccess(testUuid, testNickname);

        // THEN (Тогда проверяем результат)
        assertTrue(response.isAllowed());
        //assertEquals("Welcome back!", response.getResponseCode().getMessage());
    }

    void shouldReturnDenied_WhenPlayerDoesNotExist(){
        // GIVEN
        UUID testUuid = UUID.randomUUID();
        when(playerRepository.findByMinecraftUuid(testUuid)).thenReturn(Optional.empty());

        //WHEN
        AuthResponse response = playerService.checkAccess(testUuid, "Unknown");

        //THEN
        assertFalse(response.isAllowed());
        //contains(response.getResponseCode().getMessage(), "Not registered");
    }
}
