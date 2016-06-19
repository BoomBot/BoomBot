package net.lomeli.boombot.addons.discovery;

import com.google.common.collect.Lists;

import java.util.List;

import net.lomeli.boombot.addons.AddonClassLoader;
import net.lomeli.boombot.addons.AddonContainer;
import net.lomeli.boombot.addons.exceptions.DuplicateAddonException;
import net.lomeli.boombot.helper.Logger;

public class AddonLoader {
    private List<AddonCandidate> candidateList;
    private List<AddonContainer> containers;

    public AddonLoader() {
        candidateList = Lists.newArrayList();
        containers = Lists.newArrayList();
    }

    public void addCandidate(AddonCandidate addonCandidate) {
        candidateList.add(addonCandidate);
    }

    public void findAddons() {
        candidateList.stream().forEach(candidate -> {
            candidate.findAddons(this);
        });
        String addonList = "";
        for (int i = 0; i < containers.size(); i++) {
            AddonContainer container = containers.get(i);
            addonList += String.format("%s {%s | %s}", container.getName(), container.getId(), container.getVersion());
            if (i != containers.size() - 1) addonList += ", ";
        }
        Logger.info("Found the following addons: %s", addonList);
    }

    public void searchForDuplicates() throws DuplicateAddonException {
        List<String> registeredIDs = Lists.newArrayList();
        for (AddonContainer container : containers) {
            if (registeredIDs.contains(container.getId()) || container.getId().equalsIgnoreCase("boombot"))
                throw new DuplicateAddonException(container.getId());
            registeredIDs.add(container.getId());
        }
    }

    public void initAddons() {
        containers.stream().filter(a -> a != null).forEach(a -> a.initAddon());
    }

    public void postAddons() {
        containers.stream().filter(a -> a != null).forEach(a -> a.postInitAddon());
    }

    public void addContainer(AddonContainer container) {
        this.containers.add(container);
    }
}
