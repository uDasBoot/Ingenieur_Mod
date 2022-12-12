package com.mike.ingenieur.screen;

import com.mike.ingenieur.Ingenieur;
import com.mike.ingenieur.screen.renderer.EnergyInfoArea;
import com.mike.ingenieur.util.MouseUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

public class AsteroidMinerScreen extends AbstractContainerScreen<AsteroidMinerMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Ingenieur.MODID,
            "textures/gui/asteroid_miner.png");

    public EnergyInfoArea energyInfoArea;

    public AsteroidMinerScreen(AsteroidMinerMenu menu, Inventory inv, Component component) {
        super(menu, inv, component);
    }

    @Override
    protected void init() {
        super.init();
        renderEnergyStorage();
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(pPoseStack, x, y);
        energyInfoArea.draw(pPoseStack);
    }

    private void renderProgressArrow(PoseStack pPoseStack, int x, int y) {
        if(menu.isMining()) {
            blit(pPoseStack, x + 35, y + 35, 176, 14, menu.getScaledProgress(), 17);
        }
    }

    private void renderEnergyStorage(){
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        energyInfoArea = new EnergyInfoArea(x + 152, y + 6, menu.blockEntity.getEnergyStorage(), 16, 72);
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderEnergyToolTips(pPoseStack, pMouseX, pMouseY, x, y);
        super.renderLabels(pPoseStack, pMouseX, pMouseY);
    }

    private void renderEnergyToolTips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 151, 5, 16, 73)){
            renderTooltip(pPoseStack, energyInfoArea.getTooltips(), Optional.empty(), pMouseX - x,
                    pMouseY - y);
        }
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int areaX, int areaY, int areaWidth, int areaHeight) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + areaX, y + areaY, areaWidth, areaHeight);
    }
}
