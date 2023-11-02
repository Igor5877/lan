package pro.gravit.launcher.client.gui.scenes.serverinfo;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import pro.gravit.launcher.Launcher;
import pro.gravit.launcher.LauncherEngine;
import pro.gravit.launcher.client.ClientLauncherProcess;
import pro.gravit.launcher.client.DirBridge;
import pro.gravit.launcher.client.gui.JavaFXApplication;
import pro.gravit.launcher.client.gui.config.RuntimeSettings;
import pro.gravit.launcher.client.gui.helper.LookupHelper;
import pro.gravit.launcher.client.gui.impl.ContextHelper;
import pro.gravit.launcher.client.gui.scenes.AbstractScene;
import pro.gravit.launcher.client.gui.scenes.debug.DebugScene;
import pro.gravit.launcher.client.gui.scenes.servermenu.ServerButtonComponent;
import pro.gravit.launcher.client.gui.scenes.servermenu.ServerMenuScene;
import pro.gravit.launcher.hasher.HashedDir;
import pro.gravit.launcher.profiles.ClientProfile;
import pro.gravit.launcher.profiles.ClientProfileVersions;
import pro.gravit.launcher.profiles.optional.OptionalView;
import pro.gravit.launcher.request.auth.SetProfileRequest;
import pro.gravit.utils.helper.*;
import javafx.scene.Node;
import javafx.scene.control.Label;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.net.URL;

public class ServerInfoScene extends AbstractScene {
    private ImageView avatar;
    private Image originalAvatarImage;
    private ServerButtonComponent serverButton;
    private Button playgames;
    // private static final String SERVER_IMAGE_PATH = "images/server-images/%s.png"; // Замініть "png" на розширення файлу вашого зображення (наприклад, jpg)
    // private ImageView serverLogo; // Додайте поле ImageView для зображення сервера
    // public ClientProfile profile;
    



    public ServerInfoScene(JavaFXApplication application) {
        super("scenes/serverinfo/serverinfo.fxml", application);
    }
    // protected ServerInfoScene(JavaFXApplication application, ClientProfile profile) {
    //     super(getFXMLPath(application, profile), application);
    //     this.profile = profile;
    // }
    // private static String getFXMLPath(JavaFXApplication application, ClientProfile profile) {
    //     String customFxmlName = String.format(SERVER_BUTTON_CUSTOM_FXML, profile.getUUID());
    //     URL customFxml = application.tryResource(customFxmlName);
    //     if (customFxml != null) {
    //         return customFxmlName;
    //     }
    //     return SERVER_IMAGE_PATH;
    // } 

    @Override
    protected void doInit() throws Exception {
        // serverLogo = LookupHelper.lookup(layout, "#serverLogo"); // Ініціалізуйте поле ImageView
        LookupHelper.<Label>lookup(layout, "#nickname").setOnMouseClicked((e) ->
                application.openURL(application.guiModuleConfig.profileURL));
        LookupHelper.<Button>lookup(layout, "#site_button").setOnMouseClicked((e) ->
                application.openURL(application.guiModuleConfig.hastebinServer));
        LookupHelper.<Button>lookup(layout, "#vote_button").setOnMouseClicked((e) ->
                application.openURL(application.guiModuleConfig.votingURL));
        playgames = LookupHelper.lookup(layout, "#playgames");
        avatar = LookupHelper.lookup(layout, "#avatar");
        originalAvatarImage = avatar.getImage();
        LookupHelper.<ImageView>lookupIfPossible(layout, "#avatar").ifPresent(
                (h) -> {
                    try {
                        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(h.getFitWidth(), h.getFitHeight());
                        clip.setArcWidth(h.getFitWidth());
                        clip.setArcHeight(h.getFitHeight());
                        h.setClip(clip);
                        h.setImage(originalAvatarImage);
                    } catch (Throwable e) {
                        LogHelper.warning("Skin head error");
                    }
                }
        );
        LookupHelper.<Button>lookup(header, "#back").setOnAction((e) -> {
            try {
                switchScene(application.gui.serverMenuScene);
            } catch (Exception exception) {
                errorHandle(exception);
            }
        });

        LookupHelper.<ButtonBase>lookup(layout, "#clientSettings").setOnAction((e) -> {
            try {
                if (application.stateService.getProfile() == null)
                    return;
                switchScene(application.gui.optionsScene);
                application.gui.optionsScene.reset();
                application.gui.optionsScene.addProfileOptionals(application.stateService.getOptionalView());
            } catch (Exception ex) {
                errorHandle(ex);
            }
        });
        LookupHelper.<ButtonBase>lookup(layout, "#settings").setOnAction((e) -> {
            try {
                switchScene(application.gui.settingsScene);
                application.gui.settingsScene.reset();
            } catch (Exception exception) {
                errorHandle(exception);
            }
        });
        reset();
    }

    @Override
    public void reset() {
        // updateServerLogo(profile);
        // serverLogo.setImage(originalAvatarImage);
        ClientProfile profile = application.stateService.getProfile();
        ClientProfile clientProfile = application.stateService.getProfile();
        Node pvpNode = LookupHelper.lookupIfPossible(layout, "#pvp").orElse(null);
        Node difficultyNode = LookupHelper.lookupIfPossible(layout, "#difficulty").orElse(null);
        Node versionNode = LookupHelper.lookupIfPossible(layout, "#version").orElse(null);
        Node serverDescriptionNode = LookupHelper.lookupIfPossible(layout, "#serverDescription").orElse(null);
        Node servernameNode = LookupHelper.lookupIfPossible(layout, "#servername").orElse(null);
        Node nicknameNode = LookupHelper.lookupIfPossible(layout, "#nickname").orElse(null);

        if (pvpNode instanceof Label) {
            Label pvpLabel = (Label) pvpNode;
            String pvpValue = profile.getProperty("pvp");
            String labelText = pvpValue != null ? pvpValue : application.getTranslation("runtime.scenes.serverinfo.nottext");
            pvpLabel.setStyle(pvpValue == null ? "-fx-font-size: 16px;" : "");
            pvpLabel.setText(labelText);
        }

        if (difficultyNode instanceof Label) {
            Label difficultyLabel = (Label) difficultyNode;
            String difficultyValue = profile.getProperty("difficulty");
            String labelText = difficultyValue != null ? difficultyValue : application.getTranslation("runtime.scenes.serverinfo.nottext");
            difficultyLabel.setStyle(difficultyValue == null ? "-fx-font-size: 16px;" : "");
            difficultyLabel.setText(labelText);
        }

        if (versionNode instanceof Label) {
            Label versionLabel = (Label) versionNode;
            String versionValue = profile.getAssetIndex();
            String labelText = versionValue != null ? versionValue : application.getTranslation("runtime.scenes.serverinfo.nottext");
            versionLabel.setStyle(versionValue == null ? "-fx-font-size: 16px;" : "");
            versionLabel.setText(labelText);
        }

        if (serverDescriptionNode instanceof Label) {
            Label serverDescriptionLabel = (Label) serverDescriptionNode;
            String serverDescriptionValue = profile.getInfo();
            String labelText = serverDescriptionValue != null ? serverDescriptionValue : application.getTranslation("runtime.scenes.serverinfo.nottext");
            serverDescriptionLabel.setStyle(serverDescriptionValue == null ? "-fx-font-size: 16px;" : "");
            serverDescriptionLabel.setText(labelText);
        }

        if (servernameNode instanceof Label) {
            Label servernameLabel = (Label) servernameNode;
            String servernameValue = profile.getTitle();
            String labelText = servernameValue != null ? servernameValue : application.getTranslation("runtime.scenes.serverinfo.nottext");
            servernameLabel.setStyle(servernameValue == null ? "-fx-font-size: 16px;" : "");
            servernameLabel.setText(labelText);
        }

        if (nicknameNode instanceof Label) {
            Label nicknameLabel = (Label) nicknameNode;
            String nicknameValue = application.stateService.getUsername();
            String labelText = nicknameValue != null ? nicknameValue : application.getTranslation("runtime.scenes.serverinfo.nottext");
            nicknameLabel.setStyle(nicknameValue == null ? "-fx-font-size: 16px;" : "");
            nicknameLabel.setText(labelText);
        }

        // Так само для інших вузлів

    
        // Pane serverButtonContainer = LookupHelper.lookup(layout, "#serverButton");
        // serverButtonContainer.getChildren().clear();
        // serverButton = ServerMenuScene.getServerButton(application, profile);
        // serverButton.addTo(serverButtonContainer);
        // serverButton.enableSaveButton(application.getTranslation("runtime.scenes.serverinfo.serverButton.game"), null, false);
        playgames.setOnAction(e -> launchClient());
        ServerMenuScene.putAvatarToImageView(application, application.stateService.getUsername(), avatar);
    }

    // private void updateServerLogo(ClientProfile profile) {
    //     if (profile != null) {
    //         String serverImagePath = String.format(SERVER_IMAGE_PATH, profile.getUUID());
    //         Image image = new Image(getClass().getResourceAsStream(serverImagePath));
    //         serverLogo.setImage(image);
    //     }
    // }
    

    @Override
    public String getName() {
        return null;
    }

    private void downloadClients(ClientProfile profile, JavaHelper.JavaVersion javaVersion, HashedDir jvmHDir) {
        Path target = DirBridge.dirUpdates.resolve(profile.getAssetDir());
        LogHelper.info("Start update to %s", target.toString());
        Consumer<HashedDir> next = (assetHDir) -> {
            Path targetClient = DirBridge.dirUpdates.resolve(profile.getDir());
            LogHelper.info("Start update to %s", targetClient.toString());
            application.gui.updateScene.sendUpdateRequest(profile.getDir(), targetClient, profile.getClientUpdateMatcher(), true, application.stateService.getOptionalView(), true, (clientHDir) -> {
                LogHelper.info("Success update");
                try {
                    doLaunchClient(target, assetHDir, targetClient, clientHDir, profile, application.stateService.getOptionalView(), javaVersion, jvmHDir);
                } catch (Throwable e) {
                    LogHelper.error(e);
                    ContextHelper.runInFxThreadStatic(() -> application.gui.updateScene.addLog(String.format("launchClient error %s:%s", e.getClass().getName(), e.getMessage())));
                }
            });
        };
        if(profile.getVersion().compareTo(ClientProfileVersions.MINECRAFT_1_6_4) <= 0) {
            application.gui.updateScene.sendUpdateRequest(profile.getAssetDir(), target, profile.getAssetUpdateMatcher(), true, null, false, next);
        } else {
            application.gui.updateScene.sendUpdateAssetRequest(profile.getAssetDir(), target, profile.getAssetUpdateMatcher(), true, profile.getAssetIndex(), next);
        }
    }

    private void doLaunchClient(Path assetDir, HashedDir assetHDir, Path clientDir, HashedDir clientHDir, ClientProfile profile, OptionalView view, JavaHelper.JavaVersion javaVersion, HashedDir jvmHDir) {
        RuntimeSettings.ProfileSettings profileSettings = application.getProfileSettings();
        if(javaVersion == null) {
            javaVersion = application.javaService.getRecommendJavaVersion(profile);
        }
        if(javaVersion == null) {
            javaVersion = JavaHelper.JavaVersion.getCurrentJavaVersion();
        }
        ClientLauncherProcess clientLauncherProcess = new ClientLauncherProcess(clientDir, assetDir, javaVersion,
                clientDir.resolve("resourcepacks"), profile, application.stateService.getPlayerProfile(), view,
                application.stateService.getAccessToken(), clientHDir, assetHDir, jvmHDir);
        clientLauncherProcess.params.ram = profileSettings.ram;
        clientLauncherProcess.params.offlineMode = application.offlineService.isOfflineMode();
        if (clientLauncherProcess.params.ram > 0) {
            clientLauncherProcess.jvmArgs.add("-Xms" + clientLauncherProcess.params.ram + 'M');
            clientLauncherProcess.jvmArgs.add("-Xmx" + clientLauncherProcess.params.ram + 'M');
        }
        clientLauncherProcess.params.fullScreen = profileSettings.fullScreen;
        clientLauncherProcess.params.autoEnter = profileSettings.autoEnter;
        contextHelper.runCallback(() -> {
            Thread writerThread = CommonHelper.newThread("Client Params Writer Thread", true, () -> {
                try {
                    clientLauncherProcess.runWriteParams(new InetSocketAddress("127.0.0.1", Launcher.getConfig().clientPort));
                    if (!profileSettings.debug) {
                        LogHelper.debug("Params writted successful. Exit...");
                        LauncherEngine.exitLauncher(0);
                    }
                } catch (Throwable e) {
                    LogHelper.error(e);
                    if (getCurrentStage().getVisualComponent() instanceof DebugScene) { //TODO: FIX
                        DebugScene debugScene = (DebugScene) getCurrentStage().getVisualComponent();
                        debugScene.append(String.format("Launcher fatal error(Write Params Thread): %s: %s", e.getClass().getName(), e.getMessage()));
                        if (debugScene.currentProcess != null && debugScene.currentProcess.isAlive()) {
                            debugScene.currentProcess.destroy();
                        }
                    }
                }
            });
            writerThread.start();
            application.gui.debugScene.writeParametersThread = writerThread;
            clientLauncherProcess.start(true);
            contextHelper.runInFxThread(() -> {
                switchScene(application.gui.debugScene);
                application.gui.debugScene.onProcess(clientLauncherProcess.getProcess());
            });
        });
    }

    private String getJavaDirName(Path javaPath) {
        String prefix = DirBridge.dirUpdates.toAbsolutePath().toString();
        if (javaPath == null || !javaPath.startsWith(prefix)) {
            return null;
        }
        Path result = DirBridge.dirUpdates.relativize(javaPath);
        return result.toString();
    }

    private void showJavaAlert(ClientProfile profile) {
        if((JVMHelper.ARCH_TYPE == JVMHelper.ARCH.ARM32 || JVMHelper.ARCH_TYPE == JVMHelper.ARCH.ARM64) && profile.getVersion().compareTo(ClientProfileVersions.MINECRAFT_1_12_2) <= 0) {
            application.messageManager.showDialog(application.getTranslation("runtime.scenes.serverinfo.javaalert.lwjgl2.header"),
                    String.format(application.getTranslation("runtime.scenes.serverinfo.javaalert.lwjgl2.description"), profile.getRecommendJavaVersion()), () -> {}, () -> {}, true);
        } else {
            application.messageManager.showDialog(application.getTranslation("runtime.scenes.serverinfo.javaalert.header"),
                    String.format(application.getTranslation("runtime.scenes.serverinfo.javaalert.description"), profile.getRecommendJavaVersion()), () -> {}, () -> {}, true);
        }
    }

    private void launchClient() {
        ClientProfile profile = application.stateService.getProfile();
        if (profile == null)
            return;
        processRequest(application.getTranslation("runtime.overlay.processing.text.setprofile"), new SetProfileRequest(profile), (result) -> contextHelper.runInFxThread(() -> {
            hideOverlay(0, (ev) -> {
                RuntimeSettings.ProfileSettings profileSettings = application.getProfileSettings();
                JavaHelper.JavaVersion javaVersion = null;
                for(JavaHelper.JavaVersion v : application.javaService.javaVersions) {
                    if(v.jvmDir.toAbsolutePath().toString().equals(profileSettings.javaPath)) {
                        javaVersion = v;
                    }
                }
                if(javaVersion == null && profileSettings.javaPath != null && !application.guiModuleConfig.forceDownloadJava) {
                    try {
                        javaVersion = JavaHelper.JavaVersion.getByPath(Paths.get(profileSettings.javaPath));
                    } catch (Throwable e) {
                        if(LogHelper.isDevEnabled()) {
                            LogHelper.error(e);
                        }
                        LogHelper.warning("Incorrect java path %s", profileSettings.javaPath);
                    }
                }
                if(javaVersion == null || application.javaService.isIncompatibleJava(javaVersion, profile)) {
                    javaVersion = application.javaService.getRecommendJavaVersion(profile);
                }
                if(javaVersion == null) {
                    showJavaAlert(profile);
                    return;
                }
                String jvmDirName = getJavaDirName(javaVersion.jvmDir);
                if (jvmDirName != null) {
                    final JavaHelper.JavaVersion finalJavaVersion = javaVersion;
                    try {
                        switchScene(application.gui.updateScene);
                    } catch (Exception e) {
                        errorHandle(e);
                    }
                    application.gui.updateScene.sendUpdateRequest(jvmDirName, javaVersion.jvmDir, null, true, application.stateService.getOptionalView(), false, (jvmHDir) -> {
                        if(JVMHelper.OS_TYPE == JVMHelper.OS.LINUX || JVMHelper.OS_TYPE == JVMHelper.OS.MACOSX) {
                            Path javaFile = finalJavaVersion.jvmDir.resolve("bin").resolve("java");
                            if(Files.exists(javaFile)) {
                                if(!javaFile.toFile().setExecutable(true)) {
                                    LogHelper.warning("Set permission for %s unsuccessful", javaFile.toString());
                                }
                            }
                        }
                        downloadClients(profile, finalJavaVersion, jvmHDir);
                    });
                } else {
                    try {
                        switchScene(application.gui.updateScene);
                    } catch (Exception e) {
                        errorHandle(e);
                    }
                    downloadClients(profile, javaVersion, null);
                }
            });
        }), null);
    }
}
