![BoomBot Logo](https://raw.githubusercontent.com/Lomeli12/BoomBot/master/src/main/resources/logo.png)
#  BoomBot
A simple bot for Discord

# Built-In Commands

Commands            | Argument                                             | Result                                   	                                  
------------------- | ---------------------------------------------------- | --------------------------------------------------------------------------- 
!stop-boom-bot      |                                                      | Shuts down BoomBot. **User Must Have Server Management Permissions**
!create-command     | `<command name> <command content>`	               | Creates a new custom command for the Guild. **User Must Have Channel Management Permissions**
!remove-command     | `<command 1 name> <command 2 name> ...`              | Removes one or more custom commands from the Guild. **User Must Have Channel Management Permissions**
!clear-commands     |	                                                   | Removes all custom commands for the Guild. **User Must Have Server Management Permissions**
!running            |                                                      | Shows how long BoomBot has been running for.
!reload-config      |	                                                   | Reloads config file. **User Must Have Server Management Permissions**
!kick               | `<username> <reason (optional)>`                     | Kicks user. Will message them and give them the reason, if provided. **BoomBot And User Must Have Kick Permissions**
!ban                | `<username> <# of days min 0> <reason (optional)>`   | Bans user and removes messages by them made within the specified number of days. Will message them and give them the reason, if provided. **BoomBot And User Must Have Ban Permissions**
!about              |                                                      | Gives some basic info for BoomBot.
!ban-command        | `<username> <reason (optional)>`                     | Bans user from using commands in the Guild **User Must Have Channel Management Permissions**
!remove-ban-command | `<username 1> <username 2> ...`                      | Unbans one or more users from using commands in the Guild. **User Must Have Channel Management Permissions**