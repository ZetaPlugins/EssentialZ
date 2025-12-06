# EssentialZ

--- 

![paper](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/supported/paper_vector.svg)
![purpur](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/supported/purpur_vector.svg)
[![github](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/available/github_vector.svg)](https://github.com/ZetaPlugins/EssentialZ)
[![modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/available/modrinth_vector.svg)](https://modrinth.com/plugin/essential_z)
[![hangar](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/available/hangar_vector.svg)](https://hangar.papermc.io/KartoffelChipss/EssentialZ)
[![discord-plural](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/social/discord-plural_vector.svg)](https://strassburger.org/discord)
[![gitbook](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/documentation/gitbook_vector.svg)](https://docs.zetaplugins.com/essentialz/)

EssentialZ is a powerful and versatile plugin, designed to enhance gameplay and provide server administrators with a wide range of tools and features. It is inspired by the popular EssentialsX plugin but has been rebuilt from the ground up specifically for modern Paper servers.

EssentialsZ gives you the option to fully customize your experience without any bloat. You don't like a command? Disable it.  You don't want to use the custom chat? Disable it. You want other chat colors? Change them with one line in the config. EssentialsZ strives to be highly configurable without overwhelming you with options.

> [!IMPORTANT]  
> EssentialZ is still in an alpha stage. While many features are already implemented and functional, some commands and tools are still under development.

## Features

- **Core Commands**: Provides essential commands for player management, teleportation, home setting, warps, and more.
- **Chat Management**: Customizable chat formats, prefixes, suffixes, and channels to enhance communication on your server.
- **Economy Integration**: Seamless integration with popular economy plugins to manage player balances, transactions, and shops.
- **Permissions Support**: Compatible with major permissions plugins, allowing for granular control over command access and features.
- **Moderation Tools**: Includes tools for muting, banning, kicking players, and managing player behavior. (*Coming Soon*)
- **Customizable Configuration**: Easy-to-edit configuration files that allow server admins to tailor the plugin to their specific needs.
- **Fully customizable Messages and Branding**: Change every message the plugin sends to players to fit your server's theme.
- **Disabling Commands/Features**: Disable any command or feature you don't want to use, keeping your server clean and efficient.

## Installation

1. Download the latest version of EssentialZ from [Modrinth](https://modrinth.com/plugin/essential_z) or [Hangar](https://hangar.papermc.io/KartoffelChipss/EssentialZ).
2. Place the downloaded JAR file into your server's `plugins` directory.
3. Restart your server to generate the configuration files.
4. Customize the configuration files located in the `plugins/EssentialZ` directory to suit your server's needs.

## Configuration

You can configure EssentialZ by editing the `.yml` files located in the `plugins/EssentialZ` directory. The main configuration file is `config.yml`.

If you want to customize messages, you can either change the colros and prefixes for message types in `config.yml`, or edit the individual messages for your language in the `lang` directory.

<details>
<summary>config.yml</summary>

```yml

# Set the language for the plugin.
# Available languages can be found in the 'lang' folder.
language: "en_US"

# Wether or not OP players should get notified when a new version is available when they join.
updateNotifications: true

# The max length an item name can be. Set to -1 to disable.
maxItemNameLength: 100

# The max amount of lines a lore can have. Set to -1 to disable. (128 is the max amount of lines an item can have in Minecraft)
maxLoreLines: 10

# Set the message styles for each message type.
styles:
  default:
    accentColor: "#ba7cf8"
    prefix: "&8[<gradient:#ba7cf8:#ba7cf8>EssentialZ&8] "
  error:
    accentColor: "#FF1744"
    prefix: "&8[<#FF1744>⚠&8] "
  warning:
    accentColor: "#E9D502"
    prefix: "&8[<#E9D502>⚠&8] "
  success:
    accentColor: "#4CAF50"
    prefix: "&8[<#4CAF50>✔&8] "
  movement:
    accentColor: "#6EC6FF"
    prefix: "&8[<#6EC6FF>☁&8] "
  combat:
    accentColor: "#FF1A1A"
    prefix: "&8[<#FF1A1A>⚔&8] "
  moderation:
    accentColor: "#8F9BFF"
    prefix: "&8[<#8F9BFF>☯&8] "
  items:
    accentColor: "#FFD54F"
    prefix: "&8[<#FFD54F>🎒&8] "
  stats:
    accentColor: "#4CAF50"
    prefix: "&8[<#4CAF50>★&8] "
  communication:
    accentColor: "#81C784"
    prefix: "&8[<#81C784>✉&8] "
  teamchat:
    accentColor: "#FF8A65"
    prefix: "&8[<#FF8A65>👥&8] "
  worldcontrol:
    accentColor: "#BA68C8"
    prefix: "&8[<#BA68C8>🌐&8] "
  economy:
    accentColor: "#FFD700"
    prefix: "&8[<#FFD700>💰&8] "
```

</details>

<details>
<summary>chat.yml</summary>

```yml
# Wether or not to enable custom chat formatting
enableCustomChat: true

# Format for regular chat messages
# Placeholders:
# {player_displayname} - The display name of the player (e.g. nickname)
# {player_name} - The name of the player
# {message} - The message sent by the player
# If you have PlaceholderAPI installed, you can also use its placeholders, like %vault_prefix%
chatFormat: "&7{player_displayname} &8» &f{message}"

# Maximum length for nicknames
maxNicknameLength: 16

# Format for broadcast messages
broadcastFormat: "&8[<#F06292>Broadcast&8] &7{message}"

# Wether to enable or disable joing and leaving messages
# You can configure the messages in the lang files ("joinMessage" and "quitMessage")
enableJoinMessages: true
enableLeaveMessages: true

# Wether to show a special message for first time joiners
# You can configure the message in the lang files ("welcomeMessage")
specialWelcomeJoinMessage: true
```

</details>

<details>
<summary>commands.yml</summary>

```yml
# Here you can enable or disable specific commands.
# After editing this file, be sure to reload or restart your server for changes to take effect. /esz reload will NOT apply changes made to this file.

pay: true
balance: true
hat: true
feed: false
...
```

</details>

<details>
<summary>economy.yml</summary>

```yml
# Configuration file for the economy module

# Enable or disable the economy module
# When disabled, all economy-related features will be turned off and the economy commands won't be available.
enabled: true

# Starting balance for new users
startingBalance: 0.0

currencyFormat:
  # Currency symbol
  symbol: "$"
  # Number of decimal places
  decimalPlaces: 2
  # Thousand separator
  thousandSeparator: ","
  # Decimal separator
  decimalSeparator: "."
  # Position of the currency symbol (before/after)
  symbolPosition: "before"
```

</details>

<details>
<summary>storage.yml</summary>

```yml
# === Storage ===

# The type of storage to use. You have the following options:
# "SQLite", "MySQL", "MariaDB"
type: "SQLite"

# This section is only relevant if you use a MySQL database
host: "localhost"
port: 3306
database: "essentialz"
username: "root"
password: "password"
```

</details>

## Placeholders

To use PlaceholderAPI with EssentialZ, you only need to have both plugins installed on your server. EssentialZ will automatically register its placeholders with PlaceholderAPI.

You can use the following placeholders:

- `%essentialz_balance%` - The player's current balance.
- `%essentialz_balance_raw%` - The player's current balance as a raw number (without formatting).
- `%essentialz_baltop_{rank}_balance%` - The balance of the player at the specified rank in the baltop (e.g., `%essentialz_baltop_1_balance%` for the richest player).
- `%essentialz_baltop_{rank}_balance_raw%` - The raw balance of the player at the specified rank in the baltop.
- `%essentialz_baltop_{rank}_player%` - The name of the player at the specified rank in the baltop.
- `%essentialz_baltop_godmode%` - Whether the player is in godmode (true/false).
- `%essentialz_fly%` - Whether the player is allowed to fly (true/false).

## Languages

EssentialZ supports multiple languages. You can find the language files in the `plugins/EssentialZ/lang` directory. To change the language, simply edit the `language` option in the `config.yml` file to match the desired language file name (without the `.yml` extension).

### Currently supported languages:
- English (`en_US`)

## Support

If you need help with the setup of the plugin, or found a bug, you can join our discord [here](https://strassburger.org/discord).

[![discord-plural](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/social/discord-plural_vector.svg)](https://strassburger.org/discord)

---

[![Usage](https://bstats.org/signatures/bukkit/EssentialZ.svg)](https://bstats.org/plugin/bukkit/EssentialZ/28226)
