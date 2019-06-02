package quickcarpet.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.Option;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.network.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class GuiCommandsMenu extends Screen implements ButtonWidget.PressAction {

    private int centerX = 0;
    private int centerY = 0;

    private ButtonListWidget list;

    private List<ShortcutEntry> shortcutEntries = new ArrayList<ShortcutEntry>();

    public GuiCommandsMenu() {
        //What's this for? Well I'm lazy.
        super(NarratorManager.EMPTY);
    }

    private void updateDimensions() {
        this.centerX = this.width / 2;
        this.centerY = this.height / 2;
    }

    @Override
    protected void init() {
        super.init();
        this.updateDimensions();

        this.list = new ButtonListWidget(this.minecraft, this.width, this.height, 32, this.height - 32, 25);

        this.children.add(this.list);
        ButtonWidget rules = new ButtonWidget(centerX - 50 - 2 * (width / 8), centerY - 10 - 4 * (height / 9), 100, 20, "Carpet Rules", this);
        ButtonWidget shortcuts = new ButtonWidget(centerX - 50, centerY - 10 - 4 * (height / 9), 100, 20, "Shortcuts", this);
        this.addButton(rules);
        this.addButton(shortcuts);
    }

    @Override
    public void render(int posX, int posY, float partialTicks) {
        this.list.render(posX, posY, partialTicks);

        for(ShortcutEntry entry: this.shortcutEntries) {
            entry.render(this.shortcutEntries.indexOf(entry), posX, posY, partialTicks);
        }

        super.render(posX, posY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onPress(ButtonWidget button) {
        System.out.println("Pressed [" + button.getMessage() + "].");
    }

    @Override
    public boolean charTyped(char char_1, int int_1) {
        for(ShortcutEntry entry: this.shortcutEntries) {
            entry.charTyped(char_1, int_1);
        }

        return super.charTyped(char_1, int_1);
    }

    @Override
    public boolean keyPressed(int int_1, int int_2, int int_3) {
        for(ShortcutEntry entry: this.shortcutEntries) {
            entry.keyPressed(int_1, int_2, int_3);
        }

        return super.keyPressed(int_1, int_2, int_3);
    }

    @Override
    public void tick() {
        for(ShortcutEntry entry: this.shortcutEntries) {
            entry.tick();
        }

        super.tick();
    }

}
