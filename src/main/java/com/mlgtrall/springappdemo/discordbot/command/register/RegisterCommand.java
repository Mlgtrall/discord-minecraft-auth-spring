package com.mlgtrall.springappdemo.discordbot.command.register;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.SlashCommandReference;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationMap;
import net.dv8tion.jda.api.interactions.commands.privileges.IntegrationPrivilege;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.CommandEditAction;

import java.util.List;

//TODO
public class RegisterCommand extends SlashCommandReference {

    public RegisterCommand(String name, String subcommandGroup, String subcommand, long id) {
        super(name, subcommandGroup, subcommand, id);
    }
}
