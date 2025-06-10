package com.grape.moderationmod.common.commands;

import com.grape.moderationmod.WarnConfig;
import com.grape.moderationmod.WarningsData;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.UserBanList;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class WarnCommand
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("warn")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.argument("target", EntityArgument.player())
                        .executes(ctx -> warn(ctx.getSource(), EntityArgument.getPlayer(ctx, "target"), ""))
                        .then(Commands.argument("reason", StringArgumentType.greedyString())
                                .executes(ctx -> warn(ctx.getSource(), EntityArgument.getPlayer(ctx, "target"), StringArgumentType.getString(ctx, "reason")))))
        );
        dispatcher.register(Commands.literal("warnings")
                .then(Commands.argument("target", EntityArgument.player())
                        .executes(ctx -> show(ctx.getSource(), EntityArgument.getPlayer(ctx, "target"))))
                .executes(ctx -> show(ctx.getSource(), ctx.getSource().getPlayerOrException())));
    }

    private static int warn(CommandSourceStack source, ServerPlayer target, String reason) {
        WarningsData data = WarningsData.get(target.serverLevel());
        data.addWarning(target.getUUID());
        int count = data.getWarnings(target.getUUID());
        source.sendSuccess(() -> Component.literal(Component.translatable("warn.give").getString() + target.getName().getString() + " (" + count + ")"), true);
        target.sendSystemMessage(Component.literal(Component.translatable("warn.got").getString() + count));

        checkBan(target.server, target.getUUID(), target.getName().getString(), count);
        return 1;
    }

    private static int show(CommandSourceStack source, ServerPlayer target) {
        WarningsData data = WarningsData.get(target.serverLevel());
        int count = data.getWarnings(target.getUUID());
        source.sendSuccess(() -> Component.literal(target.getName().getString() + " has " + count + " warnings"), false);
        return 1;
    }
    private static void checkBan(MinecraftServer server, UUID uuid, String name, int count) {
        Map<Integer, Integer> map = WarnConfig.parseDurations();
        if (!map.containsKey(count)) return;

        int days = map.get(count);
        Instant until = Instant.now().plus(days, ChronoUnit.DAYS);
        Date banUntil = Date.from(until);

        GameProfile profile = new GameProfile(uuid, name);
        UserBanList banList = server.getPlayerList().getBans();

        UserBanListEntry banEntry = new UserBanListEntry(profile, new Date(), null, banUntil, "Reached " + count + " warnings");
        banList.add(banEntry);

        var player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);
        if (player != null) {
            player.connection.disconnect(Component.literal(
                    Component.translatable("banned.for").getString() +
                            " " + days + " " +
                            Component.translatable("banned.days").getString()
            ));
        }
    }


}
