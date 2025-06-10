package com.grape.moderationmod;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WarningsData extends SavedData
{
    public static class Entry {
        public int count;
        public long expireAt;
    }

    private static final String DATA_NAME = "warnings";
    private static final long WARN_DURATION = Duration.ofDays(7).toMillis();
    private final Map<UUID, Entry>  warnings = new HashMap<>();

    public static WarningsData get(ServerLevel world)
    {
        return world.getDataStorage().computeIfAbsent(WarningsData::load, WarningsData::new, DATA_NAME);
    }

    private void cleanup(UUID uuid)
    {
        Entry e = warnings.get(uuid);
        if (e != null && e.expireAt < System.currentTimeMillis()) {
            warnings.remove(uuid);
            setDirty();
        }
    }

    public int getWarnings(UUID uuid)
    {
        cleanup(uuid);
        Entry e = warnings.get(uuid);
        return e == null ? 0 : e.count;
    }

    public void addWarning(UUID uuid)
    {
        Entry e = warnings.computeIfAbsent(uuid, k -> new Entry());
        e.count++;
        e.expireAt = System.currentTimeMillis() + WARN_DURATION;
        setDirty();
    }

    public void setBanExpiration(UUID uuid, long banUntil)
    {
        Entry e = warnings.computeIfAbsent(uuid, k -> new Entry());
        e.expireAt = banUntil + WARN_DURATION;
        setDirty();
    }

    public Map<UUID, Entry> getAll()
    {
        return warnings;
    }



    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag warningsTag = new CompoundTag();
        for(var entry : warnings.entrySet())
        {
            CompoundTag e = new CompoundTag();
            e.putInt("count", entry.getValue().count);
            e.putLong("expire", entry.getValue().expireAt);
            warningsTag.put(entry.getKey().toString(), e);
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
            CompoundTag e = warningsTag.getCompound(key);
            Entry entry = new Entry();
            entry.count = e.getInt("count");
            entry.expireAt = e.getLong("expire");
            warningsData.warnings.put(uuid, entry);
        }
        return warningsData;
    }
}
