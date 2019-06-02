package quickcarpet.init;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class KeyBindings {

    public static LinkedHashSet<KeyBinding> KEY_REGISTRY = new LinkedHashSet<>();
    public static LinkedHashSet<String> CATEGORY_REGISTRY = new LinkedHashSet<>();

    /*
    * Declare all keys in the mod here for ease of use.
    */
    public static KeyBinding COMMANDS_MENU = new KeyBinding(getName("commands_menu"), (int)'C', getCategory("carpet"));

    /*
     * Registers keys and categories.
     *
     * Note that not all keys need to be registered,
     * only the ones that end up the Controls menu.
     */
    public static void registerKeyBindings() {
        registerKeyBinding(COMMANDS_MENU);
        pushRegistry();
    }

    /*
    * Pushes the registry to the game code...
    *
    * Very messy and needs to be changed. Additionally, this only works in
    * dev environment cause of the field reflection.
    * */
    private static void pushRegistry() {
        GameOptions options = MinecraftClient.getInstance().options;

        List<KeyBinding> keys = new ArrayList<KeyBinding>(Arrays.asList(options.keysAll));
        keys.addAll(KEY_REGISTRY);

        try {
            //Gets the keys array.
            Field keysAll = GameOptions.class.getDeclaredField("keysAll");
            keysAll.setAccessible(true);

            //Removes final modifier.
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(keysAll, keysAll.getModifiers() & ~Modifier.FINAL);

            //Adds our key bindings to the array.
            keysAll.set(options, keys.toArray(new KeyBinding[keys.size()]));

            //Gets the categories map.
            Field categoryOrderMap = KeyBinding.class.getDeclaredField("categoryOrderMap");
            categoryOrderMap.setAccessible(true);

            //Removes it's final modifier.
            modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(keysAll, keysAll.getModifiers() & ~Modifier.FINAL);

            //Adds all custom categories one by one.
            Map<String, Integer> map = (Map<String, Integer>)categoryOrderMap.get(null);
            int lastId = map.size() + 1;

            for(String category : CATEGORY_REGISTRY) {
                map.put(category, ++lastId);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /*
     * Registers keys and their categories. Remember that everything is FIFO.
     */
    public static void registerKeyBinding(KeyBinding keyBinding) {
        if(keyBinding == null) {
            throw new NullPointerException("Cannot register a null key binding!");
        } else {
            KEY_REGISTRY.add(keyBinding);
            CATEGORY_REGISTRY.add(keyBinding.getCategory());
            System.out.println("Registering key [" + keyBinding.getName() + ", " + keyBinding.getId() + "] from category [" + keyBinding.getCategory() + "].");
        }
    }

    /*
    * Generates the category and key prefixes for the language files.
    */
    public static String getCategory(String registryName) {
        return "key.categories." + registryName;
    }

    public static String getName(String registryName) {
        return "key." + registryName.toLowerCase();
    }

}
