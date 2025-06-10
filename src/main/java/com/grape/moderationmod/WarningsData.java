package com.grape.moderationmod;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WarningsData extends SavedData
{
    private static final String DATA_NAME = "warnings";
    private final Map<UUID, Integer>  warnings = new HashMap<>();

    public static WarningsData get(ServerLevel world)
    {
        return world.getDataStorage().computeIfAbsent(WarningsData::load, WarningsData::new, DATA_NAME);
    }

    public int getWarnings(UUID uuid)
    {
        return warnings.getOrDefault(uuid, 0);
    }
    public void addWarning(UUID uuid)
    {
        warnings.put(uuid, getWarnings(uuid) + 1);
        setDirty();
    }
    public Map<UUID, Integer> getAll()
    {
        return warnings;
    }



    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag warningsTag = new CompoundTag();
        for(var entry : warnings.entrySet())
        {
            warningsTag.putInt(entry.getKey().toString(), entry.getValue());
        }
        tag.put("warnings", warningsTag);
        return tag;
    }

    public static WarningsData load(CompoundTag tag)
    {
        WarningsData warningsData = new WarningsData();
        CompoundTag warningsTag = tag.getCompound("warnings");
        for(String key : warningsTag.getAllKeys())
        {
            UUID uuid = UUID.fromString(key);
            warningsData.warnings.put(uuid, warningsTag.getInt(key));
        }
        return warningsData;
    }
}
