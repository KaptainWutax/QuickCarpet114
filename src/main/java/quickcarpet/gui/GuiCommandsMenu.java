package quickcarpet.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.NarratorManager;

import java.util.ArrayList;
import java.util.List;

public class GuiCommandsMenu extends Screen implements ButtonWidget.PressAction {

    private int centerX = 0;
    private int centerY = 0;
    ShortcutSelectionListWidget test;
    private List<ShortcutEntry> shortcutEntries = new ArrayList<ShortcutEntry>();
    private ButtonWidget buttonAddShortcut;

    public GuiCommandsMenu() {
        //What's this for? Well I'm lazy.
        super(NarratorManager.EMPTY);
    }

    public <T extends AbstractButtonWidget> T addButton(T button) {
        return super.addButton(button);
    }

    private void updateDimensions() {
        this.centerX = this.width / 2;
        this.centerY = this.height / 2;
    }

    @Override
    protected void init() {
        super.init();

        this.updateDimensions();

        this.test = new ShortcutSelectionListWidget(MinecraftClient.getInstance());
        ButtonWidget rules = new ButtonWidget(centerX - 50 - 2 * (width / 8), centerY - 10 - 4 * (height / 9), 100, 20, "Carpet Rules", this);
        ButtonWidget shortcuts = new ButtonWidget(centerX - 50, centerY - 10 - 4 * (height / 9), 100, 20, "Shortcuts", this);
        this.buttonAddShortcut = new ButtonWidget(centerX - 50 - 2 * (width / 8), centerY - 40 + 4 * (height / 9), 100, 20, "Add Shortcut", this);
        this.addButton(rules);
        this.addButton(shortcuts);
        this.addButton(this.buttonAddShortcut);
        this.children.add(test);
        shortcutEntries.add(new ShortcutEntry(this));
    }

    @Override
    public void render(int posX, int posY, float partialTicks) {
        this.renderDirtBackground(0);

        for(ShortcutEntry entry: this.shortcutEntries) {
            entry.render(this.shortcutEntries.indexOf(entry), posX, posY, partialTicks);
        }

        this.test.render(posX, posY, partialTicks);
        super.render(posX, posY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onPress(ButtonWidget button) {
        System.out.println("Pressed [" + button.getMessage() + "].");

        if(button.equals(this.buttonAddShortcut)) {
            this.test.addShortcutItem();
        }
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

    public class ShortcutSelectionListWidget extends AlwaysSelectedEntryListWidget {

        public ShortcutSelectionListWidget(MinecraftClient minecraftClient_1) {
            super(minecraftClient_1, GuiCommandsMenu.this.width, GuiCommandsMenu.this.height, 32, GuiCommandsMenu.this.height - 65, 50);
        }

        public void focus(ShortcutItem item) {
            this.focusOn(item);
            this.setSelected(item);
            item.entry.commandLine.changeFocus(true);
        }


        public int addShortcutItem() {
            return super.addEntry(new ShortcutItem());
        }


        protected int getScrollbarPosition() {
            return super.getScrollbarPosition() + 65;
        }

        public int getRowWidth() {
            return super.getRowWidth() + 150;
        }

        public class ShortcutItem extends AlwaysSelectedEntryListWidget.Entry {

            public ShortcutEntry entry;

            public ShortcutItem() {
                entry = new ShortcutEntry(GuiCommandsMenu.this);
            }

            @Override
            public void render(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8, float var9) {
                entry.render(0, ShortcutSelectionListWidget.this.width / 2, var2 + 25 / 2, 0);
                ShortcutSelectionListWidget.this.drawCenteredString(MinecraftClient.getInstance().textRenderer, "", ShortcutSelectionListWidget.this.width / 2, var2 + 1, 16777215);
            }

            @Override
            public boolean mouseClicked(double double_1, double double_2, int int_1) {
                GuiCommandsMenu.ShortcutSelectionListWidget.this.setSelected(this);
                return entry.mouseClicked(double_1, double_2, int_1);
            }

            @Override
            public boolean charTyped(char char_1, int int_1) {
                GuiCommandsMenu.ShortcutSelectionListWidget.this.setSelected(this);
                entry.charTyped(char_1, int_1);
                return super.charTyped(char_1, int_1);
            }

            @Override
            public boolean keyPressed(int int_1, int int_2, int int_3) {
                GuiCommandsMenu.ShortcutSelectionListWidget.this.setSelected(this);
                entry.keyPressed(int_1, int_2, int_3);
                return super.keyPressed(int_1, int_2, int_3);
            }

        }

    }


}
