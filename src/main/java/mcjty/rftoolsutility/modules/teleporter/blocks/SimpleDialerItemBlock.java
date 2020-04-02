package mcjty.rftoolsutility.modules.teleporter.blocks;

import mcjty.lib.blocks.BaseBlockItem;
import mcjty.lib.varia.GlobalCoordinate;
import mcjty.lib.varia.Logging;
import mcjty.rftoolsutility.RFToolsUtility;
import mcjty.rftoolsutility.modules.teleporter.data.TeleportDestination;
import mcjty.rftoolsutility.modules.teleporter.data.TeleportDestinations;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class SimpleDialerItemBlock extends BaseBlockItem {
    public SimpleDialerItemBlock(Block block) {
        super(block, new Properties().group(RFToolsUtility.setup.getTab()));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack stack = context.getItem();
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        PlayerEntity player = context.getPlayer();
        TileEntity te = world.getTileEntity(pos);

        if (!world.isRemote) {
            CompoundNBT tagCompound = stack.getOrCreateTag();
            CompoundNBT compoundnbt = stack.getOrCreateChildTag("BlockEntityTag");
            CompoundNBT info = compoundnbt.getCompound("Info");

            if (te instanceof MatterTransmitterTileEntity) {
                MatterTransmitterTileEntity matterTransmitterTileEntity = (MatterTransmitterTileEntity) te;

                if (!matterTransmitterTileEntity.checkAccess(player.getName().getFormattedText())) {    // @todo 1.14
                    Logging.message(player, TextFormatting.RED + "You have no access to this matter transmitter!");
                    return ActionResultType.FAIL;
                }

                info.putInt("transX", matterTransmitterTileEntity.getPos().getX());
                info.putInt("transY", matterTransmitterTileEntity.getPos().getY());
                info.putInt("transZ", matterTransmitterTileEntity.getPos().getZ());
                info.putString("transDim", world.getDimension().getType().getRegistryName().toString());

                if (matterTransmitterTileEntity.isDialed()) {
                    Integer id = matterTransmitterTileEntity.getTeleportId();
                    boolean access = checkReceiverAccess(player, world, id);
                    if (!access) {
                        Logging.message(player, TextFormatting.RED + "You have no access to the matter receiver!");
                        return ActionResultType.FAIL;
                    }

                    info.putInt("receiver", id);
                    Logging.message(player, TextFormatting.YELLOW + "Receiver set!");
                }

                Logging.message(player, TextFormatting.YELLOW + "Transmitter set!");
            } else if (te instanceof MatterReceiverTileEntity) {
                MatterReceiverTileEntity matterReceiverTileEntity = (MatterReceiverTileEntity) te;

                Integer id = matterReceiverTileEntity.getOrCalculateID();
                boolean access = checkReceiverAccess(player, world, id);
                if (!access) {
                    Logging.message(player, TextFormatting.RED + "You have no access to this matter receiver!");
                    return ActionResultType.FAIL;
                }

                info.putInt("receiver", id);
                Logging.message(player, TextFormatting.YELLOW + "Receiver set!");
            } else {
                return super.onItemUse(context);
            }

            compoundnbt.put("Info", info);  // Make sure it's actually set
            return ActionResultType.SUCCESS;
        } else {
            return super.onItemUse(context);
        }
    }

    private boolean checkReceiverAccess(PlayerEntity player, World world, Integer id) {
        boolean access = true;
        TeleportDestinations destinations = TeleportDestinations.get(world);
        GlobalCoordinate coordinate = destinations.getCoordinateForId(id);
        if (coordinate != null) {
            TeleportDestination destination = destinations.getDestination(coordinate);
            if (destination != null) {
                World worldForDimension = mcjty.lib.varia.TeleportationTools.getWorldForDimension(destination.getDimension());
                if (worldForDimension != null) {
                    TileEntity recTe = worldForDimension.getTileEntity(destination.getCoordinate());
                    if (recTe instanceof MatterReceiverTileEntity) {
                        MatterReceiverTileEntity matterReceiverTileEntity = (MatterReceiverTileEntity) recTe;
                        if (!matterReceiverTileEntity.checkAccess(player.getUniqueID())) {
                            access = false;
                        }
                    }
                }
            }
        }
        return access;
    }
}
