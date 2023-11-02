package pro.gravit.launcher.client.gui.config;

import pro.gravit.launcher.LauncherInject;

import java.util.HashMap;
import java.util.Map;

public class GuiModuleConfig {
    @LauncherInject(value = "modules.javaruntime.skinurl")
    public String skinURL;
    @LauncherInject(value = "modules.javaruntime.profileurl")
    public String profileURL;
    @LauncherInject(value = "modules.javaruntime.votingurl")
    public String votingURL;
    @LauncherInject(value = "modules.javaruntime.createaccounturl")
    public String createAccountURL;
    @LauncherInject(value = "modules.javaruntime.forgotpassurl")
    public String forgotPassURL;
    @LauncherInject(value = "modules.javaruntime.hastebinserver")
    public String hastebinServer;
    @LauncherInject(value = "modules.javaruntime.forcedownloadjava")
    public boolean forceDownloadJava;
    @LauncherInject(value = "modules.javaruntime.javalist")
    public Map<String, String> javaList;
    @LauncherInject(value = "modules.javaruntime.lazy")
    public boolean lazy;
    @LauncherInject(value = "modules.javaruntime.disableofflinemode")
    public boolean disableOfflineMode;

    @LauncherInject(value = "modules.javaruntime.autoauth")
    public boolean autoAuth;

    @LauncherInject(value = "modules.javaruntime.locale")
    public String locale;

    @LauncherInject(value = "modules.javaruntime.downloadthreads")
    public int downloadThreads = 4;

    public static Object getDefault() {
        GuiModuleConfig config = new GuiModuleConfig();
        
        config.skinURL = "https://nestworld.fun/skin-api";
        config.profileURL = "https://nestworld.fun/profile";
        config.votingURL = "https://nestworld.fun/vote";
        config.createAccountURL = "https://nestworld.fun/user/register";
        config.forgotPassURL = "https://nestworld.fun/profile";
        config.hastebinServer = "https://nestworld.fun/";
        config.lazy = false;
        config.javaList = new HashMap<>();
        config.disableOfflineMode = false;
        config.autoAuth = false;
        config.locale = "Ukrainian";
        config.downloadThreads = 4;
        return config;
    }
}
