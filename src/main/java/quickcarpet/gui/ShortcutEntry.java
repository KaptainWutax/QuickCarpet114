package quickcarpet.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.MouseOptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.controls.ControlsListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;

import java.util.List;

public class ShortcutEntry {

    private GuiCommandsMenu screen;
    private List<Element> screenElements;
    public TextFieldWidget commandLine;
    public ButtonWidget keyButton;

    public ShortcutEntry(GuiCommandsMenu screen) {
        this.init();
        this.screen = screen;
        this.screenElements = ((List<Element>)this.screen.children());
    }

    public void init() {
        this.commandLine = new TextFieldWidget(
                MinecraftClient.getInstance().textRenderer,
                0,0, 100, 20, null, "test field"
        );
        this.commandLine.setMaxLength(100);
        this.commandLine.setCursor(0);

        this.keyButton = new ButtonWidget(0, 0, 40, 20, "KEY", this.screen);

        if(this.screenElements != null) {
            this.screenElements.add(this.commandLine);
        }
    }

    public void render(int index, int posX, int posY, float partialTicks) {
        this.keyButton.x = posX + 2 * this.screen.width / 12;
        this.keyButton.y = posY;
        this.commandLine.setX(posX - 3 * this.screen.width / 8);
        this.commandLine.y = posY;
        this.commandLine.setWidth(250);
        this.commandLine.render(posX, posY, partialTicks);
        this.keyButton.render(posX, posY, partialTicks);
    }

    public void tick() {
        this.commandLine.tick();
    }

    public void charTyped(char char_1, int int_1) {
        this.commandLine.charTyped(char_1, int_1);
    }

    public void keyPressed(int int_1, int int_2, int int_3) {
        this.commandLine.keyPressed(int_1, int_2, int_3);
    }

    public boolean mouseClicked(double double_1, double double_2, int int_1) {
        this.commandLine.setCursor(0);
        return this.commandLine.changeFocus(true);
    }

}
