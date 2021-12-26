package net.exenco.lightshow.executor.commands;

import net.exenco.lightshow.show.stage.StageManager;
import net.exenco.lightshow.util.ShowSettings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class StartCommand extends ShowCommand {

    private final ArrayList<ShowCommand> nextList;
    private final StageManager stageManager;
    public StartCommand(ShowSettings showSettings, StageManager stageManager) {
        super(showSettings);
        this.stageManager = stageManager;
        this.nextList = new ArrayList<>();
        nextList.add(new IpCommand(showSettings, stageManager));
    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String getPermission() {
        return "lightshow.start";
    }

    @Override
    public int getPosition() {
        return 1;
    }

    @Override
    public List<ShowCommand> getNext() {
        return nextList;
    }

    @Override
    public boolean execute(CommandSender commandSender, Command command, String label, String[] args) {
        if(args.length > 1)
            return super.execute(commandSender, command, label, args);

        ShowSettings.ArtNet startMessages = showSettings.artNet();
        switch(stageManager.start()) {
            case 1 -> commandSender.sendMessage(startMessages.starting());
            case 0 -> commandSender.sendMessage(startMessages.alreadyStarted());
            case -1 -> commandSender.sendMessage(startMessages.invalidIp());
        }
        return true;
    }

    public static class IpCommand extends ShowCommand {

        private final StageManager stageManager;
        public IpCommand(ShowSettings showSettings, StageManager stageManager) {
            super(showSettings);
            this.stageManager = stageManager;
        }

        @Override
        public String getName() {
            return "%ip%";
        }

        @Override
        public String getPermission() {
            return "lightshow.start.ip";
        }

        @Override
        public int getPosition() {
            return 2;
        }

        @Override
        public List<ShowCommand> getNext() {
            return new ArrayList<>();
        }

        @Override
        public List<String> getCompletions() {
            List<String> list = new ArrayList<>();
            list.add("<ip-address>[:port]");
            return list;
        }

        @Override
        public boolean execute(CommandSender commandSender, Command command, String label, String[] args) {
            ShowSettings.ArtNet artNet = showSettings.artNet();

            String msg = args[1];
            String address = msg;
            int port =  artNet.port();
            if(msg.contains(":")) {
                String[] msgArr = msg.split(":");
                if(msgArr.length != 2) {
                    commandSender.sendMessage(artNet.invalidIp());
                    return true;
                }
                address = msgArr[0];
                port = Integer.parseInt(msgArr[1]);
            }

            switch(stageManager.start(address, port)) {
                case 1 -> commandSender.sendMessage(artNet.starting());
                case 0 -> commandSender.sendMessage(artNet.alreadyStarted());
                case -1 -> commandSender.sendMessage(artNet.invalidIp());
            }
            return true;
        }
    }
}
