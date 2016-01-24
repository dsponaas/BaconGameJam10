# BaconGameJam10

Build Jar
./gradlew desktop:dist

## Mac Build
java -jar packr.jar -platform mac -jdk "https://bitbucket.org/alexkasko/openjdk-unofficial-builds/downloads/openjdk-1.7.0-u80-unofficial-macosx-x86_64-bundle.zip" -executable SuperMattDamonSpaceCannonAdventure -appjar desktop-1.0.jar -mainclass "com/grizbenzis/bgj10/desktop/DesktopLauncher" -minimizejre "soft" -vmargs "-Xmx1G" -outdir out

## Windows Build
java -jar packr.jar -platform windows -jdk "https://bitbucket.org/alexkasko/openjdk-unofficial-builds/downloads/openjdk-1.7.0-u80-unofficial-windows-i586-image.zip" -executable SuperMattDamonSpaceCannonAdventure -appjar desktop-1.0.jar -mainclass "com/grizbenzis/bgj10/desktop/DesktopLauncher" -minimizejre "soft" -vmargs "-Xmx1G" -outdir out

## Linux Build (x86)
java -jar packr.jar -platform linux32 -jdk "https://bitbucket.org/alexkasko/openjdk-unofficial-builds/downloads/openjdk-1.7.0-u80-unofficial-linux-i586-image.zip" -executable SuperMattDamonSpaceCannonAdventure -appjar desktop-1.0.jar -mainclass "com/grizbenzis/bgj10/desktop/DesktopLauncher" -minimizejre "soft" -vmargs "-Xmx1G" -outdir out

## Linux Build (x64)
java -jar packr.jar -platform linux64 -jdk "https://bitbucket.org/alexkasko/openjdk-unofficial-builds/downloads/openjdk-1.7.0-u80-unofficial-linux-amd64-image.zip" -executable SuperMattDamonSpaceCannonAdventure -appjar desktop-1.0.jar -mainclass "com/grizbenzis/bgj10/desktop/DesktopLauncher" -minimizejre "soft" -vmargs "-Xmx1G" -outdir out
