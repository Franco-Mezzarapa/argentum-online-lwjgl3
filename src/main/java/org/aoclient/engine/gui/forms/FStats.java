package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import org.aoclient.engine.game.models.*;

import java.io.IOException;

import static org.aoclient.engine.audio.Sound.SND_CLICK;
import static org.aoclient.engine.audio.Sound.playSound;
import static org.aoclient.network.protocol.Protocol.*;

public final class FStats extends Form {
    
    private final int backgroundImage;

    public FStats() {
        try {
            backgroundImage = loadTexture("VentanaEstadisticas");
            requestAttributes();
            requestFame();
            requestSkills();
            requestMiniStats();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render() {
        ImGui.setNextWindowSize(466, 447, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar
                | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoMove);

        this.checkMoveFrm();

        ImGui.setWindowFocus();
        ImGui.setCursorPos(0, 0);
        ImGui.image(backgroundImage, 466, 447);

        drawAttributes();
        drawReputations();
        drawSkills();
        drawStatistics();

        ImGui.setCursorPos(12, 416);
        if (ImGui.button("Cerrar", 438, 24)) {
            playSound(SND_CLICK);
            close();
        }

        ImGui.end();
    }

    private void drawAttributes() {
        short y = 40;
        for (Attribute attributes : Attribute.values()) {
            ImGui.setCursorPos(100, y += 17);
            ImGui.text(String.valueOf(USER.getAttributes()[attributes.ordinal()]));
        }
    }

    private void drawReputations() {
        short y = 158;
        for (Reputation reputation : Reputation.values()) {
            if (reputation.ordinal() == Reputation.THIEF.ordinal()) continue;
            ImGui.setCursorPos(100, y += 17);
            ImGui.text(String.valueOf(USER.getReputations()[reputation.ordinal()]));
        }
        ImGui.setCursorPos(100, y += 17);
        ImGui.text(USER.isCriminal() ? "Criminal" : "Ciudadano");
    }

    private void drawSkills() {
        float y = 40;
        for (int skill : USER.getSkills()) {
            ImGui.setCursorPos(320, y += 17.25f);
            ImGui.text(String.valueOf(skill));
        }
    }

    private void drawStatistics() {
        float y = 300;
        for (KillCounter counter : KillCounter.values()) {
            ImGui.setCursorPos(160, y += 15.5f);
            ImGui.text(String.valueOf(USER.getKillCounter((byte) counter.ordinal())));
        }
        ImGui.setCursorPos(65, y += 15);
        ImGui.text(Role.values()[USER.getRole()].name());
        ImGui.setCursorPos(160, y += 15);
        ImGui.text(String.valueOf(USER.getJailTime()));
    }
}
