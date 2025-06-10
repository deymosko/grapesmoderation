package com.grape.moderationmod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;



@Mod(Main.MODID)
public class Main {
    public static final String MODID = "moderationmod";

    public Main() {
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);


        modBus.addListener(this::onCommonSetup);

//        ModItems.REGISTER.register(modBus);
//        ModSounds.REGISTER.register(modBus);
    }
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {

    }
    private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

        });

    }
}
