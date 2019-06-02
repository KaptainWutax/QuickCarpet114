package quickcarpet.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import quickcarpet.gui.GuiCommandsMenu;
import quickcarpet.init.KeyBindings;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(method = "tick()V", at = @At("HEAD"))
    public void tick(CallbackInfo callbackInfo) {
        if(KeyBindings.COMMANDS_MENU.isPressed()) {
            MinecraftClient.getInstance().openScreen(new GuiCommandsMenu());
        }
    }

}
