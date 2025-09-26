package com.nearbyplayerlogger;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

@PluginDescriptor(
        name = "Nearby Player Logger"
)
public class NearbyPlayerLoggerPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private NearbyPlayerLoggerConfig config;

    @Inject
    private ConfigManager configManager;

    private final Set<String> seenPlayers = new HashSet<>();
    private NearbyPlayerLoggerPanel panel;
    private NavigationButton navButton;

    @Override
    protected void startUp()
    {
        // Initialize panel
        panel = new NearbyPlayerLoggerPanel();

        // Load icon
        BufferedImage icon = null;
        try
        {
            icon = ImageIO.read(getClass().getResourceAsStream("icon.png"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // Create navigation button
        navButton = NavigationButton.builder()
                .tooltip("Nearby Players")
                .icon(icon)
                .panel(panel)
                .build();

        // Add to toolbar
        clientToolbar.addNavigation(navButton);

        // Clear seen players
        seenPlayers.clear();
    }

    @Override
    protected void shutDown()
    {
        if (navButton != null)
        {
            clientToolbar.removeNavigation(navButton);
        }
        if (panel != null)
        {
            panel.clearPlayers();
        }
        seenPlayers.clear();
    }

    @Subscribe
    public void onPlayerSpawned(PlayerSpawned event)
    {
        String name = event.getPlayer().getName();
        if (name != null && seenPlayers.add(name))
        {
            panel.addPlayer("Player: " + name);
        }
    }

    @Provides
    NearbyPlayerLoggerConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(NearbyPlayerLoggerConfig.class);
    }
}