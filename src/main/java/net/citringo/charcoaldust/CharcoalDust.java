package net.citringo.charcoaldust;

import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod(modid = CharcoalDust.MODID, version = CharcoalDust.VERSION)
public class CharcoalDust
{
    public static final String MODID = "charcoaldust";
    public static final String VERSION = "1.10.2-1.0";
    public static Item itemCharcoalDust;
    public Logger logger;
    @EventHandler
    public void preInit(FMLPreInitializationEvent e)
    {
        logger = e.getModLog();
        logger.info("Initializing CharcoalDust Mod...");

        itemCharcoalDust = new Item()
                .setCreativeTab(CreativeTabs.MATERIALS)
                .setUnlocalizedName("charcoalDust")
                .setMaxStackSize(64);
        GameRegistry.register(itemCharcoalDust, new ResourceLocation(MODID, "charcoalDust"));
        logger.info("Registered Charcoal Dust Item.");

        if (e.getSide().isClient())
            ModelLoader.setCustomModelResourceLocation(itemCharcoalDust, 0, new ModelResourceLocation(itemCharcoalDust.getRegistryName(), "inventory"));
        logger.info("Registered Charcoal Dust Model and texture.");

        OreDictionary.registerOre("dustCoal", new ItemStack(itemCharcoalDust, 1, 0));
        OreDictionary.registerOre("dustCharcoal", new ItemStack(itemCharcoalDust, 1, 0));
        logger.info("Registered Charcoal Dust to OreDictionary");

        logger.info("Initializing has successfully finished!");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e)
    {
        if (Loader.isModLoaded("IC2"))
        {
            logger.info("Loading IC2...");
            try
            {
                List<ItemStack> charcoals = OreDictionary.getOres("itemCharcoal");
                for (ItemStack charcoal : charcoals)
                {
                    // Charcoal => Charcoal Dust
                    RecipeInputItemStack i = new RecipeInputItemStack(charcoal, 1);
                    NBTTagCompound metadata = new NBTTagCompound();
                    metadata.setInteger("macerator", 2000);
                    ItemStack o = new ItemStack(itemCharcoalDust, 1,0);
                    Recipes.macerator.addRecipe(i, metadata, false, o);
                }

                List<ItemStack> charcoalBlocks = OreDictionary.getOres("blockCharcoal");
                for (ItemStack charcoalBlock : charcoalBlocks)
                {
                    // Charcoal Block => Charcoal Dust * 9
                    RecipeInputItemStack i = new RecipeInputItemStack(charcoalBlock, 1);
                    NBTTagCompound metadata = new NBTTagCompound();
                    metadata.setInteger("macerator", 2000);
                    ItemStack o = new ItemStack(itemCharcoalDust, 9, 0);
                    Recipes.macerator.addRecipe(i, metadata, false, o);
                }
                logger.info("Loading IC2 has successfully finished!");
            }
            catch (Exception ex)
            {
                logger.error("Failed to load IC2.");
                ex.printStackTrace();
            }
        }
        else
            logger.info("IC2 hasn't been loaded because there is no IC2.");
    }
}
