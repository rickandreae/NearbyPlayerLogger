package com.nearbyplayerlogger;

import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import java.awt.*;

public class NearbyPlayerLoggerPanel extends PluginPanel
{
    private final DefaultListModel<String> playerListModel;
    private final JList<String> playerList;
    private final JScrollPane scrollPane;

    public NearbyPlayerLoggerPanel()
    {
        super();
        setLayout(new BorderLayout());

        // List model to hold player names
        playerListModel = new DefaultListModel<>();

        // JList to display names
        playerList = new JList<>(playerListModel);
        playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Scroll pane for when the list is long
        scrollPane = new JScrollPane(playerList);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Add a player to the panel.
     * This is thread-safe with Swing.
     *
     * @param name Player name to add
     */
    public void addPlayer(String name)
    {
        SwingUtilities.invokeLater(() -> {
            if (!playerListModel.contains(name))
            {
                playerListModel.addElement(name);
                // Auto-scroll to the last entry
                playerList.ensureIndexIsVisible(playerListModel.size() - 1);
            }
        });
    }

    /**
     * Clear all players from the panel.
     */
    public void clearPlayers()
    {
        SwingUtilities.invokeLater(playerListModel::clear);
    }
}