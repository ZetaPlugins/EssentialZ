package com.zetaplugins.essentialz.features;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.zetacore.annotations.InjectPlugin;
import com.zetaplugins.zetacore.annotations.Manager;
import com.zetaplugins.zetacore.annotations.PostManagerConstruct;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

@Manager
public class RulesManager {
    private List<String> lines;

    @InjectPlugin
    private EssentialZ plugin;

    @PostManagerConstruct
    public void init() {
        lines = readLines();
    }

    private File getRulesFile() {
        return new File(plugin.getDataFolder(), "rules.txt");
    }

    private void createDefaultRulesFile() {
        File rulesFile = getRulesFile();
        rulesFile.getParentFile().mkdirs();
        try {
            rulesFile.createNewFile();
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to create default rules file: " + e.getMessage(), e);
        }
    }

    @Nullable
    private List<String> readLines() {
        File rulesFile = getRulesFile();
        if (!rulesFile.exists()) {
            createDefaultRulesFile();
        }
        List<String> lines = new ArrayList<>();
        try (Scanner scanner = new Scanner(rulesFile)) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load rules file: " + e.getMessage(), e);
            return null;
        }
        return lines;
    }

    /**
     * Gets the lines of the rules file.
     * @return A list of strings representing the lines of the rules file, or null if the file could not be read.
     */
    @Nullable
    public List<String> getLines() {
        if (lines == null) lines = readLines();
        if (lines == null) return null;
        return lines;
    }

    /**
     * Checks if the rules file exists.
     * @return true if the rules file exists, false otherwise.
     */
    public boolean fileExists() {
        return getRulesFile().exists();
    }

    /**
     * Reloads the rules file from disk.
     * @return true if the file was successfully reloaded, false otherwise.
     */
    public boolean reload() {
        lines = readLines();
        return lines != null;
    }
}
