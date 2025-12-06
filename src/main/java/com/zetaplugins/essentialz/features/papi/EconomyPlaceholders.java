package com.zetaplugins.essentialz.features.papi;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.features.economy.manager.EconomyManager;
import com.zetaplugins.zetacore.annotations.*;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Manager
public class EconomyPlaceholders {
    private List<Map.Entry<UUID, Double>> cachedTopBalances = new ArrayList<>();
    private long lastCacheTime = 0;
    private final long cacheDurationMillis = 30_000; // 30 seconds
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @InjectPlugin
    private EssentialZ plugin;

    @InjectManager
    private EconomyManager economyManager;

    @Papi(identifier = "balance")
    public String balancePlaceholder(OfflinePlayer player) {
        double balance = economyManager.getBalance(player);
        return economyManager.format(balance);
    }

    @Papi(identifier = "balance_raw")
    public String balanceRawPlaceholder(OfflinePlayer player) {
        double balance = economyManager.getBalance(player);
        return String.valueOf(balance);
    }

    @Papi(identifier = "baltop_{rank}_balance")
    public String baltopBalancePlaceholder(OfflinePlayer player, @PapiParam("rank") int rank) {
        if (rank < 1) return "Invalid rank";

        updateCacheIfNeeded();

        lock.readLock().lock();
        try {
            if (rank > cachedTopBalances.size()) return "N/A";
            Double balance = cachedTopBalances.get(rank - 1).getValue();
            return economyManager.format(balance);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Papi(identifier = "baltop_{rank}_balance_raw")
    public String baltopBalanceRawPlaceholder(OfflinePlayer player, @PapiParam("rank") int rank) {
        if (rank < 1) return "Invalid rank";

        updateCacheIfNeeded();

        lock.readLock().lock();
        try {
            if (rank > cachedTopBalances.size()) return "N/A";
            Double balance = cachedTopBalances.get(rank - 1).getValue();
            return String.valueOf(balance);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Papi(identifier = "baltop_{rank}_player")
    public String baltopPlayerPlaceholder(OfflinePlayer player, @PapiParam("rank") int rank) {
        if (rank < 1) return "Invalid rank";

        updateCacheIfNeeded();

        lock.readLock().lock();
        try {
            if (rank > cachedTopBalances.size()) return "N/A";
            UUID playerUUID = cachedTopBalances.get(rank - 1).getKey();
            OfflinePlayer topPlayer = plugin.getServer().getOfflinePlayer(playerUUID);
            return topPlayer.getName() != null ? topPlayer.getName() : "Unknown";
        } finally {
            lock.readLock().unlock();
        }
    }

    private void updateCacheIfNeeded() {
        long now = System.currentTimeMillis();
        if (now - lastCacheTime < cacheDurationMillis) return;

        lock.writeLock().lock();
        try {
            Map<UUID, Double> topBalances = economyManager.getTopBalances(10);
            cachedTopBalances = topBalances.entrySet()
                    .stream()
                    .sorted(Map.Entry.<UUID, Double>comparingByValue().reversed())
                    .toList();
            lastCacheTime = now;
        } finally {
            lock.writeLock().unlock();
        }
    }
}