package kr.planetearth.plugins.planetlobby.command;

import kr.planetearth.plugins.planetlobby.PlanetLobby;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PermissionGivePerUUIDCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(sender instanceof ConsoleCommandSender))return false;
        File file = new File(PlanetLobby.getInstance().getDataFolder(), "uuids.txt");
        if (!file.exists()){
            System.out.println("uuids.txt 파일이 존재하지 않습니다.");
        }
        List<String> uuidList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    uuidList.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(uuidList.size() + "개의 UUID에 명령어를 실행합니다.");
        new BukkitRunnable(){
            int index = 0;
            @Override
            public void run() {
                if (uuidList.isEmpty())cancel();
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + uuidList.get(index) + " permission settemp cmi.customalias.1211online true 32h");
                index++;
                if (index == uuidList.size()){
                    System.out.println(index + "명에게 권한을 지급 했습니다.");
                    cancel();
                }
            }
        }.runTaskTimer(PlanetLobby.getInstance(), 20L, 3L);
        return true;
    }
}
