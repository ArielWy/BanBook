# BanBook Plugin

**BanBook** is a unique plugin that introduces strategic and tense gameplay by allowing players to target others for a temporary ban. The plugin features customizable items (Ban Book and Revive Book) and offers dynamic player management through an intuitive interface.

## Features

1. **Ban Book & Revive Book:**
   - Server admins can define any item to act as the Ban Book and Revive Book.
   - Players can mark others as "hunted" using the Ban Book. If a hunted player dies within 24 hours (default), they receive a temporary ban.
   - The Revive Book allows players to reverse the ban and revive a banned player.

2. **Marking Players:**
   - Right-clicking the Ban Book opens a GUI displaying player heads.
   - Selecting a player marks them as hunted, and the Ban Book disappears after use.
   - Multiple players can be marked simultaneously.

3. **Configurable Alerts & Duration:**
   - The plugin allows full customization of alerts and messages for player marking, banning, and reviving.
   - The default ban duration is 7 days, and the hunted period is 24 hours, but both can be changed in the config.

4. **Player Ban & Revive System:**
   - If a marked player dies while hunted, they are banned using Minecraft’s default ban system.
   - The Revive Book displays all banned players in a GUI, allowing for easy reviving by selecting their heads.

5. **Permissions & Commands:**
   - Admins need the `banbook.admin` permission to access plugin commands.
   - Custom commands allow admins to manage Ban Book and Revive Book items and reload the config.

## Commands

- `/banbook` (alias: `/bb`):
  - **get <revivebook/banbook>:** Gives the player the Ban Book or Revive Book (if defined).
  - **set <revivebook/banbook>:** Sets the item the admin is holding as the Ban Book or Revive Book.
  - **reload:** Reloads the config file.

## Configuration

The BanBook plugin includes a fully customizable configuration file. Here's a breakdown of the config options:

```yaml
General:
  TargetedPlayerAlert: true  # Notifies all players when a player is marked as hunted.
  TargetedPlayerBanAlert: false  # Alerts when a targeted player is banned.
  TargetedPlayerReviveAlert: false  # Sends a message when a player is revived.

  BanTime: 7  # Ban duration in days.
  ImmunePlayer:  # Players who cannot be targeted.
    - "Example"
    - "AnotherPlayer"

Messages:
  TargetedPlayerMessage: "{target} was targeted by {player}!"
  TargetedPlayerBanAlert: "{target} died."
  TargetedPlayerReviveAlert: "{target} was revived by {player}!"
  BanMessage: "You have been banned!"

Items:
  BanBookSkull:
    name: §b{player}
    lore:
      - §eRight-click to set the player as hunted for the next 24 hours.
  ConfirmItem:
    name: §b§lHunt Trigger for {player}
    lore:
      - §7§oClick to initiate the hunt on {player}.
  ReviveBookSkull:
    name: §b{player}
    lore:
      - §eRight-click to revive {player} from their ban.
```

## Permissions

- `banbook.admin`: Required to execute the plugin commands (set, get, and reload).

## How to Use

1. **Marking a Player:**
   - Use the Ban Book to open a GUI.
   - Select the player’s head and confirm the hunt.
   - If the player dies within the next 24 hours (configurable), they will be banned for 7 days (configurable).

2. **Reviving a Player:**
   - Use the Revive Book to open a GUI showing banned players.
   - Select the player’s head and confirm the revival to lift the ban.

## Installation

1. Download the latest version of BanBook.
2. Drop the plugin into your server’s `plugins` folder.
3. Start the server, and configure the plugin by editing the `config.yml` file.
4. Use `/banbook reload` to apply any configuration changes.

## Compatibility

- Minecraft version 1.20+.
- Works in all game modes.
- No dependencies on other plugins, though the item acquisition can be customized through external plugins or crafting systems.

## License

This plugin is open-source and available under the MIT License.
