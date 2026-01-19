package com.mlgtrall.springappdemo.discordbot.listener;

import com.mlgtrall.springappdemo.model.AuthResponse;
import com.mlgtrall.springappdemo.repository.PlayerRepository;
import com.mlgtrall.springappdemo.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class BanCommandListener extends ListenerAdapter {

    private PlayerService playerService;

    @Override
    public void onSlashCommandInteraction(@NonNull SlashCommandInteractionEvent event) {
        log.debug("BanCommandListener event called!");
        event.deferReply(true).queue(); //Ответ виден только пользователю

        if(event.getName().equals("ban")) {
            //TODO: Протестировать без проверки Permission, так как проверка указана в инициализации команды в DiscordBotConfig
            if(event.getMember() == null || !event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                event.reply("You don't have permission to use this command!")
                        .queue();
                return;
            }
        }

        //Логика команды
        String mcName = Objects.requireNonNull(event.getOption("nickname")).getAsString();

        AuthResponse response = playerService.banPlayer(mcName);

        if(response.getResponseCode() == AuthResponse.ResponseCode.NOT_FOUND)
            event.reply("That player does not exist in database!").queue();
        else event.reply("Player banned!").queue();

    }
}
