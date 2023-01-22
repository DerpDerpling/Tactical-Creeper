package derp.tacticalcreeper.sound;

import derp.tacticalcreeper.TacticalCreeper;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSounds {
    public static SoundEvent TACTICALCREEPER_ATTACC = registerSoundEvent("atacc");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id= new Identifier(TacticalCreeper.MOD_ID, name);
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
    }
}
