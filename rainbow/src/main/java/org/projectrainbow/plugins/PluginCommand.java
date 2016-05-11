package org.projectrainbow.plugins;

import PluginReference.MC_Command;
import PluginReference.MC_Player;
import com.google.common.base.Objects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.BlockPos;
import net.minecraft.src.CommandBase;
import net.minecraft.src.CommandException;
import net.minecraft.src.ICommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PluginCommand extends CommandBase {
    private final MC_Command delegate;

    public PluginCommand(MC_Command delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getCommandName() {
        try {
            return delegate.getCommandName();
        } catch (Exception e) {
            e.printStackTrace();
            return toString();
        }
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        try {
            return delegate.getHelpLine(sender instanceof MC_Player ? (MC_Player) sender : null);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void processCommand(MinecraftServer minecraftServer, ICommandSender sender, String[] args) throws CommandException {
        try {
            delegate.handleCommand(sender instanceof MC_Player ? (MC_Player) sender : null, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getCommandAliases() {
        try {
            return Objects.firstNonNull(delegate.getAliases(), Collections.<String>emptyList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<String>();
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(MinecraftServer minecraftServer, ICommandSender sender) {
        try {
            return delegate.hasPermissionToUse(sender instanceof MC_Player ? (MC_Player) sender : null);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(MinecraftServer minecraftServer, ICommandSender sender, String[] args, BlockPos blockPos) {
        try {
            return Objects.firstNonNull(delegate.getTabCompletionList(sender instanceof MC_Player ? (MC_Player) sender : null, args), Collections.<String>emptyList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<String>();
        }
    }
}
