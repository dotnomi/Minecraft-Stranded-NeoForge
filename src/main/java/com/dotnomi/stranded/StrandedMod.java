package com.dotnomi.stranded;

import com.dotnomi.stranded.commands.ResetPlayerCommand;
import com.dotnomi.stranded.event.VoiceoverEvent;
import com.dotnomi.stranded.event.handler.PlayerLoggedInHandler;
import com.dotnomi.stranded.networking.ModPayloads;
import com.dotnomi.stranded.networking.packet.PlayVoiceoverS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(StrandedMod.MOD_ID)
public class StrandedMod
{
    public static final String MOD_ID = "stranded";
    private static final Logger LOGGER = LogUtils.getLogger();

    public StrandedMod(IEventBus modEventBus, ModContainer modContainer)
    {
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);
        NeoForge.EVENT_BUS.addListener(this::registerCommands);

        modEventBus.addListener(ModPayloads::register);

        NeoForge.EVENT_BUS.register(new PlayerLoggedInHandler());

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.debug("HELLO FROM COMMON SETUP");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    private void registerCommands(RegisterCommandsEvent event) {
        ResetPlayerCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onVoiceoverEvent(VoiceoverEvent event) {
        LOGGER.debug("HELLO FROM VOICEOVER EVENT {}", event.getVoiceoverId());
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            LOGGER.debug(serverPlayer.getUUID() + " joined the server");
            PacketDistributor.sendToPlayer(serverPlayer, new PlayVoiceoverS2CPacket("Test"));
        }
    }
}
