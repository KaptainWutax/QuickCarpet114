package quickcarpet.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;

import java.util.List;

public class ShortcutEntry {

    private Screen screen;
    List<Element> screenElements;
    public TextFieldWidget commandLine;

    public ShortcutEntry(Screen screen) {
        this.init();
        this.screenElements = ((List<Element>)this.screen.children());
    }

    public void init() {
        this.commandLine = new TextFieldWidget(
                MinecraftClient.getInstance().textRenderer,
                50,50, 100, 20, null, "test field"
        );
        this.commandLine.setMaxLength(23);
        this.commandLine.setCursor(0);
        this.commandLine.changeFocus(true);

        this.screenElements.add(this.commandLine);
    }

    public void render(int index, int posX, int posY, float partialTicks) {
        this.commandLine.render(posX, posY, partialTicks);
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

}
