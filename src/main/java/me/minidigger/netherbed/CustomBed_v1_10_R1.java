package me.minidigger.netherbed;

import net.minecraft.server.v1_10_R1.Biomes;
import net.minecraft.server.v1_10_R1.Block;
import net.minecraft.server.v1_10_R1.BlockBed;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.Blocks;
import net.minecraft.server.v1_10_R1.ChatMessage;
import net.minecraft.server.v1_10_R1.Entity;
import net.minecraft.server.v1_10_R1.EntityHuman;
import net.minecraft.server.v1_10_R1.EnumDirection;
import net.minecraft.server.v1_10_R1.EnumHand;
import net.minecraft.server.v1_10_R1.IBlockData;
import net.minecraft.server.v1_10_R1.Item;
import net.minecraft.server.v1_10_R1.ItemStack;
import net.minecraft.server.v1_10_R1.MinecraftKey;
import net.minecraft.server.v1_10_R1.SoundEffectType;
import net.minecraft.server.v1_10_R1.World;

import java.util.Iterator;

import javax.annotation.Nullable;

import static me.minidigger.netherbed.ReflectionUtil.invokeMethod;
import static me.minidigger.netherbed.ReflectionUtil.setFinalStatic;

/**
 * Created by Martin on 05.10.2016.
 */
public class CustomBed_v1_10_R1 extends BlockBed {

    @Override
    public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumHand var5, @Nullable ItemStack var6, EnumDirection var7, float var8, float var9, float var10) {
        if (var1.isClientSide) {
            return true;
        } else {
            System.out.println("clicked custom bed!");
            if (var3.get(PART) != BlockBed.EnumBedPart.HEAD) {
                var2 = var2.shift((EnumDirection) var3.get(FACING));
                var3 = var1.getType(var2);
                if (var3.getBlock() != this) {
                    return true;
                }
            }

            if ((var1.worldProvider.e() && var1.getBiome(var2) != Biomes.j) || true) { // NetherBed - added true
                if (((Boolean) var3.get(OCCUPIED)).booleanValue()) {
                    EntityHuman var12 = this.c(var1, var2);
                    if (var12 != null) {
                        var4.b(new ChatMessage("tile.bed.occupied", new Object[0]));
                        return true;
                    }

                    var3 = var3.set(OCCUPIED, Boolean.valueOf(false));
                    var1.setTypeAndData(var2, var3, 4);
                }

                EntityHuman.EnumBedResult var13 = var4.a(var2);
                if (var13 == EntityHuman.EnumBedResult.OK) {
                    var3 = var3.set(OCCUPIED, Boolean.valueOf(true));
                    var1.setTypeAndData(var2, var3, 4);
                    return true;
                } else {
                    if (var13 == EntityHuman.EnumBedResult.NOT_POSSIBLE_NOW) {
                        var4.b(new ChatMessage("tile.bed.noSleep", new Object[0]));
                    } else if (var13 == EntityHuman.EnumBedResult.NOT_SAFE) {
                        var4.b(new ChatMessage("tile.bed.notSafe", new Object[0]));
                    }

                    return true;
                }
            } else {
                var1.setAir(var2);
                BlockPosition var11 = var2.shift(((EnumDirection) var3.get(FACING)).opposite());
                if (var1.getType(var11).getBlock() == this) {
                    var1.setAir(var11);
                }

                var1.createExplosion((Entity) null, (double) var2.getX() + 0.5D, (double) var2.getY() + 0.5D, (double) var2.getZ() + 0.5D, 5.0F, true, true);
                return true;
            }
        }
    }

    // don't complain about beein private, silly
    private EntityHuman c(World var1, BlockPosition var2) {
        Iterator var3 = var1.players.iterator();

        EntityHuman var4;
        do {
            if (!var3.hasNext()) {
                return null;
            }

            var4 = (EntityHuman) var3.next();
        } while (!var4.isSleeping() || !var4.bedPosition.equals(var2));

        return var4;
    }

    // https://www.spigotmc.org/threads/custom-modified-block-class-register-mc-1-7.11633/#post-614490
    public static void register() {
        CustomBed_v1_10_R1 block = new CustomBed_v1_10_R1();
        block.a(SoundEffectType.a);
        block.c(0.2F);
        block.c("bed");
        block.q();
        Block.REGISTRY.a(26, new MinecraftKey("bed"), block);//Update block register
        setFinalStatic(Blocks.class, "BED", Block.REGISTRY.get(new MinecraftKey("bed"))); //Update block reference
        int i = Block.REGISTRY.a(Blocks.BED) << 4 | Blocks.BED.toLegacyData(Blocks.BED.getBlockData());
        Block.REGISTRY_ID.a(Blocks.BED.getBlockData(), i); //Update correct key
        invokeMethod(Item.class, "b", new Class[]{Block.class}, Blocks.BED); //Update item register
    }
}
