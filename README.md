# Guess The Number

A multiplayer number-guessing game built in Java with JavaFX. One player hosts a game, 2–10 players join over the network, and everyone guesses a number from 1 to 100 each round. If nobody gets it, the answer is revealed and a new round starts.

Built as a team of 3 for Network Programming at Wentworth Institute of Technology (Spring 2025).

## Features
- Host or join a game by IP address and port
- 2–10 players per game (set in the settings menu, default 4)
- Live waiting room that shows players as they connect or disconnect
- Guesses appear on every player's screen in real time
- Rounds keep going even if a player drops out

## How it works
- **Server** (`network/Server.java`) accepts connections and gives each player its own thread. It tracks guesses and picks the winners each round.
- **Client** (`network/Client.java`) listens for server messages and updates the JavaFX screens on the UI thread with `Platform.runLater`.
- **Packets** (`packets/`) are 9 serializable message classes (PlayerConnected, PlayerGuessed, PlayerWin, NextRound, PlayerDisconnect, and more). They all extend one shared `Message` base class, so both sides send typed objects instead of raw text.
- **UI** is 4 screens built with FXML and CSS (`guiConfigs/`).

## Tech
Java · JavaFX (FXML + CSS) · TCP sockets · Java object serialization · multithreading

## Running it

**Requirements:** JDK 21 or newer, plus the [JavaFX SDK](https://gluonhq.com/products/javafx/) for your OS. Download the SDK zip and unzip it. Works from any terminal, including VS Code's built-in one.

### Linux / macOS (quick start)
Unzip JavaFX into your home folder (so you have something like `~/javafx-sdk-25.0.5`), then from the project folder:
```bash
chmod +x run.sh   # first time only
./run.sh
```
The script finds JavaFX, compiles the game, copies the screens and assets, and launches it. If JavaFX is somewhere else, point the script at it:
```bash
FX=/path/to/javafx-sdk/lib ./run.sh
```

### Linux / macOS (manual)
```bash
export FX=/path/to/javafx-sdk/lib
javac --module-path $FX --add-modules javafx.controls,javafx.fxml -d bin src/app/*.java src/app/misc/*.java src/app/network/*.java src/app/packets/*.java
cp -r src/app/guiConfigs src/app/fonts src/app/icons bin/app/
java --module-path $FX --add-modules javafx.controls,javafx.fxml -cp bin app.Gui
```

### Windows (PowerShell)
```powershell
$FX = "C:\path\to\javafx-sdk\lib"
javac --module-path $FX --add-modules javafx.controls,javafx.fxml -d bin src/app/*.java src/app/misc/*.java src/app/network/*.java src/app/packets/*.java
Copy-Item -Recurse -Force src\app\guiConfigs, src\app\fonts, src\app\icons bin\app\
java --module-path $FX --add-modules javafx.controls,javafx.fxml -cp bin app.Gui
```

## Playing

1. **Open settings first.** Click the gear icon on the home screen and enter your name. If you're hosting, also set the max number of players and the port (default `8080`). Click **Save**.
2. **Host:** click **Create**. You'll go to the waiting room.
3. **Everyone else:** click **Join** and enter your name, the host's IP address (`localhost` if you're on the same computer), and the port.
4. When everyone has joined, the host clicks **Start**.
5. Type a number from 1 to 100 and press **Enter**. Once every player has guessed, the answer is revealed.

**Testing alone:** run the game twice (two terminals). Host in one window and join with `localhost` in the other.
