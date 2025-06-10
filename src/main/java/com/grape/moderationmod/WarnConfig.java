package com.grape.moderationmod;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarnConfig
{
    public static ForgeConfigSpec config;
    private static ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ban_durations;

    static
    {
        ban_durations = builder.comment("Ban durations in days, format counts = days")
                .defineList("banDurations", List.of("3=1", "4=3", "5=7"), o -> o instanceof String);
        config = builder.build();
    }

    public static Map<Integer, Integer> parseDurations()
    {
        Map<Integer, Integer> durations = new HashMap<>();
        for(String key : ban_durations.get())
        {
            String[] split = key.split("=");
            if(split.length == 2)
            {
                try
                {
                    int count = Integer.parseInt(split[0]);
                    int days = Integer.parseInt(split[1]);
                    durations.put(count, days);
                }
                catch(NumberFormatException ignored) {}
            }
        }
        return durations;
    }

}
