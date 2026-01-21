package com.mlgtrall.springappdemo.discordbot.listener;

import com.mlgtrall.springappdemo.model.AuthResponse;
import com.mlgtrall.springappdemo.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegCommandListener extends ListenerAdapter {

    private final PlayerService playerService;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(!event.getName().equals("reg")) return;
        log.debug("RegCommandListener event called!");

        event.deferReply(true).queue(); //Дать знать о скором ответе. Ответ увидит только пользователь


        if(event.getMember() == null || !event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getHook()
                    .sendMessage("You don't have permission to use this command!")
                    .queue();
            return;
        }

        User user = Objects.requireNonNull(event.getOption("user")).getAsUser();
        if(user.isBot() || user.isSystem()) {
            event.getHook()
                    .editOriginal("Specified user is not a human!")
                    .queue();
        }else{

            Long discordId = user.getIdLong();
            String mcName = Objects.requireNonNull(event.getOption("nickname")).getAsString();

            AuthResponse response = playerService.registerPlayer(discordId, mcName);

            if (response.getResponseCode() == AuthResponse.ResponseCode.REGISTER_SUCCESS)
                event.getHook()
                        .editOriginal("Success! Player with the name: **" + mcName + "** registered!")
                        .queue();
            else event.getHook()
                    .editOriginal(response.getResponseCode().getMessage())
                    .queue();

        }

        //Удаление висящего общения с ботом в чате
        event.getHook().deleteOriginal().queueAfter(10, TimeUnit.SECONDS);
    }

}
