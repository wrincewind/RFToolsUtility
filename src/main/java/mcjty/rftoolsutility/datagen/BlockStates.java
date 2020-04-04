package mcjty.rftoolsutility.datagen;

import mcjty.lib.datagen.BaseBlockStateProvider;
import mcjty.rftoolsutility.RFToolsUtility;
import mcjty.rftoolsutility.modules.crafter.CrafterSetup;
import mcjty.rftoolsutility.modules.screen.ScreenSetup;
import mcjty.rftoolsutility.modules.teleporter.TeleporterSetup;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

import static net.minecraftforge.client.model.generators.ModelProvider.BLOCK_FOLDER;

public class BlockStates extends BaseBlockStateProvider {

    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, RFToolsUtility.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        orientedBlock(CrafterSetup.CRAFTER1.get(), frontBasedModel("crafter1", modLoc("block/machinecrafter1")));
        orientedBlock(CrafterSetup.CRAFTER2.get(), frontBasedModel("crafter2", modLoc("block/machinecrafter2")));
        orientedBlock(CrafterSetup.CRAFTER3.get(), frontBasedModel("crafter3", modLoc("block/machinecrafter3")));

        orientedBlock(TeleporterSetup.DESTINATION_ANALYZER.get(), frontBasedModel("destination_analyzer", modLoc("block/machinedestinationanalyzer")));
        orientedBlock(TeleporterSetup.DIALING_DEVICE.get(), frontBasedModel("dialing_device", modLoc("block/machinedialingdevice")));
        orientedBlock(TeleporterSetup.MATTER_BOOSTER.get(), frontBasedModel("matter_booster", modLoc("block/machinematterbooster")));
        simpleBlock(TeleporterSetup.MATTER_RECEIVER.get(), topBasedModel("matter_receiver", modLoc("block/machinereceiver")));
        simpleBlock(TeleporterSetup.MATTER_TRANSMITTER.get(), topBasedModel("matter_transmitter", modLoc("block/machinetransmitter")));
        logicSlabBlock(TeleporterSetup.SIMPLE_DIALER.get(), "simple_dialer", modLoc("block/machinesimpledialer"));

        ModelFile screen = screenModel("screen", modLoc("block/screenframe_icon"));
        orientedBlock(ScreenSetup.SCREEN.get(), screen);
        orientedBlock(ScreenSetup.SCREEN_HIT.get(), screen);
        orientedBlock(ScreenSetup.CREATIVE_SCREEN.get(), screenModel("creative_screen", modLoc("block/creative_screenframe_icon")));
        orientedBlock(ScreenSetup.SCREEN_CONTROLLER.get(), frontBasedModel("screen_controller", modLoc("block/machinescreencontroller")));

    }

    public ModelFile screenModel(String modelName, ResourceLocation texture) {
        BlockModelBuilder model = models().getBuilder(BLOCK_FOLDER + "/" + modelName)
                .parent(models().getExistingFile(mcLoc("block")));
        model.element().from(0, 0, 13).to(16, 16, 16)
                .face(Direction.DOWN).cullface(Direction.DOWN).texture("side").end()
                .face(Direction.UP).cullface(Direction.UP).texture("side").end()
                .face(Direction.EAST).cullface(Direction.EAST).texture("side").end()
                .face(Direction.WEST).cullface(Direction.WEST).texture("side").end()
                .face(Direction.NORTH).texture("front").end()
                .face(Direction.SOUTH).cullface(Direction.SOUTH).texture("side").end()
                .end()
                .texture("side", new ResourceLocation("rftoolsbase", "block/base/machineside"))
                .texture("front", texture);
        return model;
    }


}