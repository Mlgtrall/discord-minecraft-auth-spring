package com.mlgtrall.springappdemo.discordbot.listener;

import com.mlgtrall.springappdemo.model.AuthResponse;
import com.mlgtrall.springappdemo.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class PardonCommandListener extends ListenerAdapter {

    private final PlayerService playerService;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        if(!event.getName().equals("pardon")) return;
        log.debug("PardonCommandListener event called!");

        event.deferReply(true).queue(); //Ответ виден только пользователю

        if(event.getMember() == null || !event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getHook()
                    .editOriginal("You don't have permission to use this command!")
                    .queue();
            return;
        }

        //String discordId = Objects.requireNonNull(Objects.requireNonNull(event.getOption("user")).getAsMember()).getId();
        String  mcName = Objects.requireNonNull(event.getOption("nickname")).getAsString();

        //AuthResponse response = playerService.unbanPlayerByDiscordId(discordId);
        AuthResponse response = playerService.unbanPlayerByMinecraftName(mcName);

        if(response.getResponseCode() == AuthResponse.ResponseCode.PARDON_SUCCESS)
            event.getHook()
                    .editOriginal("Success! Minecraft player **" + mcName + "** unbanned!")
                    .queue();
        else event.getHook()
                .editOriginal(response.getResponseCode().getMessage())
                .queue();

        //Удаление сообщения в чате
        event.getHook().deleteOriginal().queueAfter(10, TimeUnit.SECONDS);
    }
}
