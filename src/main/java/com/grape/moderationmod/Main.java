package com.grape.moderationmod;

import com.grape.moderationmod.common.commands.WarnCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;



@Mod(Main.MODID)
public class Main {
    public static final String MODID = "moderationmod";

    public Main() {
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        modBus.addListener(this::onCommonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, WarnConfig.config);
    }
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        WarnCommand.register(event.getDispatcher());
    }
    private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

        });

    }
}
