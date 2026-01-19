package com.mlgtrall.springappdemo.discordbot.listener;

import com.mlgtrall.springappdemo.model.AuthResponse;
import com.mlgtrall.springappdemo.service.PlayerService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RegCommandListener extends ListenerAdapter {

    private final PlayerService playerService;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(!event.getName().equals("reg")) return;

        String mcName = Objects.requireNonNull(event.getOption("nickname")).getAsString();
        String discordId = event.getUser().getId();

        event.deferReply(true).queue(); //true - ответ увидит только пользователь

        AuthResponse response = playerService.registerPlayer(discordId, mcName);
        if(response.getResponseCode() == AuthResponse.ResponseCode.REGISTER_SUCCESS)
            event.getHook()
                .sendMessage("Успех! Ник **" + mcName + "** зарегистрирован!")
                .queue();
        else event.getHook()
                .sendMessage("Аккаунт уже зарегистрирован!")
                .queue();

    }
}
