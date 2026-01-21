package com.mlgtrall.springappdemo.discordbot.config;

import com.mlgtrall.springappdemo.discordbot.listener.BanCommandListener;
import com.mlgtrall.springappdemo.discordbot.listener.PardonCommandListener;
import com.mlgtrall.springappdemo.discordbot.listener.RegCommandListener;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class DiscordBotConfig {

    @Value("${DISCORD_TOKEN}") // Токен в .env
    private String token;

    @Bean
    public JDA jda(RegCommandListener regCommandListener, BanCommandListener banCommandListener, PardonCommandListener pardonCommandListener) {
        log.info("Starting JDA connection...");

        log.info("Discord bot token set to {} ...", token.substring(0, 4));


        JDA jda;
        try {
            log.debug("RegCommandListener object: {}", regCommandListener);
            log.debug("BanCommandListener object: {}", banCommandListener);
            log.debug("PardonCommandListener object: {}", pardonCommandListener);
            jda = JDABuilder.createDefault(token)
                        .enableIntents(GatewayIntent.MESSAGE_CONTENT) // Чтобы бот видел текст сообщений
                        .addEventListeners(regCommandListener, banCommandListener, pardonCommandListener)
                        .build();
            log.info("JDA build completed successfully");
        } catch (Exception e) {
            log.error("Failed to initialize Discord Bot: {}", e.getMessage());
            throw e;
        }

        log.info("Registering Discord Bot Commands...");
        // Регистрация команд
        // Глобальные команды обновляются до часа, команды для конкретного сервера — мгновенно.
        //TODO: Добавить команду /getdata [user]
        jda.updateCommands().addCommands(
                Commands.slash("reg", "Привязать Minecraft аккаунт")
                        .addOption(OptionType.USER, "user", "Пользователь дискорд", true)
                        .addOption(OptionType.STRING, "nickname", "Ник игрока в игре", true)
                        .setGuildOnly(true)
                        .setDefaultPermissions(DefaultMemberPermissions.DISABLED),
                Commands.slash("ban", "Заблокировать игрока.")
                        .addOption(OptionType.STRING, "nickname", "Ник игрока в игре", true)
                        .setGuildOnly(true)
                        .setDefaultPermissions(DefaultMemberPermissions.DISABLED),
                Commands.slash("pardon", "Разбанить игрока.")
                        .addOption(OptionType.STRING, "nickname", "Ник игрока в игре", true)
                        .setGuildOnly(true)
                        .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
        ).queue();
        log.info("All Discord Bot commands queued on update successfully!");

        return jda;
    }



}
