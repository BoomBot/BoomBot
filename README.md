![BoomBot Logo](https://raw.githubusercontent.com/Lomeli12/BoomBot/master/src/main/resources/logo.png)
#  BoomBot
A simple bot for Discord

# Built-In Commands

Commands            | Argument                                             | Result                                   	                                  
------------------- | ---------------------------------------------------- | --------------------------------------------------------------------------- 
!?stop-bot          |                                                      | Shuts down BoomBot. **User Must Have Server Management Permissions**
!?mkcom             | `<command name> <command content>`                   | Creates a new custom command for the Guild. **User Must Have Channel Management Permissions**
!?rmcom             | `<command 1 name> <command 2 name> ...`              | Removes one or more custom commands from the Guild. **User Must Have Channel Management Permissions**
!?clearcoms         |                                                      | Removes all custom commands for the Guild. **User Must Have Server Management Permissions**
!?restrict          |                                                      | Limits use of commands to Channel Managers. **User Must Have Channel Management Permissions**
!?derestrict        |                                                      | Removes restriction of commands. **User Must Have Channel Management Permissions**
!?running           |                                                      | Shows how long BoomBot has been running for.
!?reload-config     |                                                      | Reloads config file. **User Must Have Server Management Permissions**
!?kick              | `@<username> <reason (optional)>`                    | Kicks user. Will message them and give them the reason, if provided. **BoomBot And User Must Have Kick Permissions**
!?ban               | `@<username> <# of days min 0> <reason (optional)>`  | Bans user and removes messages by them made within the specified number of days. Will message them and give them the reason, if provided. **BoomBot And User Must Have Ban Permissions**
!?unban             | `@<username 1> @<username 2> ...`                    | Unbans one or more users from the Guild. 
!?about             |                                                      | Gives some basic info for BoomBot. **BoomBot And User Must Have Ban Permissions**
!?bancom            | `@<username> <reason (optional)>`                    | Bans user from using commands in the Guild **User Must Have Channel Management Permissions**
!?unbancom          | `@<username 1> @<username 2> ...`                    | Unbans one or more users from using commands in the Guild. **User Must Have Channel Management Permissions**
!?options           | `<config option name> <value>`                       | Change and save a config option and applies the change. **User Must Have Server Management Permissions**

# How To Build BoomBot

## Step 1: Setup JDK 8
The Java JDK is used to compile BoomBot, specifically JDK 8.

1. Download and install the Java JDK 8. (Skip to the next step if you already have it installed)
	* [Windows/Mac download link](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).  Scroll down, accept the `Oracle Binary Code License Agreement for Java SE`, and download it (if you have a 64-bit OS, please download the 64-bit version).
	    * Windows: Set environment variables for the JDK.
	        * Go to `Control Panel\System and Security\System`, and click on `Advanced System Settings` on the left-hand side.
            * Click on `Environment Variables`.
            * Under `System Variables`, click `New`.
            * For `Variable Name`, input `JAVA_HOME`.
            * For `Variable Value`, input something similar to `C:\Program Files\Java\jdk1.8.0_XX` with *XX* representing the version of JDK 8 you installed, and click `Ok`.
            * Scroll down to a variable named `Path`, and double-click on it.
            * Append `;%JAVA_HOME%\bin` EXACTLY AS SHOWN and click `Ok`.  Make sure the location is correct; double-check just to make sure.
            * Open up your command line and run `javac`.  If it spews out a bunch of possible options and the usage, then you're good to go.
	* Linux: Installation methods for certain popular flavors of Linux are listed below.  If your distribution is not listed, follow the instructions specific to your package manager or install it manually [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).
		* Gentoo: `emerge dev-java/oracle-jdk-bin`
		* Archlinux: `pacman -S jdk8-openjdk`
		* Ubuntu/Debian: `apt-get install openjdk-8-jdk`
		* Fedora: `yum install java-1.8.0-openjdk-devel`
2. Download this repo locally/updating your local repo
    * Using Git: If you have *git* installed and are using it via command line/terminal.
        * Download this repo fresh: `git clone https://github.com/BoomBot/BoomBot`
        * Updating your local repo:
            * Open up command line/terminal instance and navigate to your local repo's folder.
            * Pull latest changes: `git pull`
                * If you're having trouble pulling the latest changes or made changes you'd like to revert to use:
                    `git fetch --all`
                    `git reset --hard origin/master`
    * Download As Zip: Click the *Download ZIP* button in the top left corner and unzip it.
3. Compiling it via Gradle
    * Open up command line/terminal instance and navigate to your local repo's folder.
    * Use Gradle to compile
        * Windows: `gradlew.bat build`
        * Linux/Mac: `./gradlew build`
    * Your compiled *BoomBot jar* should be in `build/libs`. Remember to copy the `lang` folder and have it in the same folder as BoomBot.