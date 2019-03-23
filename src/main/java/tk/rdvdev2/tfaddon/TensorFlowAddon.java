package tk.rdvdev2.tfaddon;

import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TensorFlowAddon.MODID)
public class TensorFlowAddon {
    public static final String MODID = "tensorflowaddon";
    private final static Logger LOGGER = LogManager.getLogger();

    public TensorFlowAddon() {
        LOGGER.info("TensorFlow addon is loaded");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(ITensorFlowCapability.class, new ITensorFlowCapability.Storage(), ITensorFlowCapability.Impl::new);
    }
}
