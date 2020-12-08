package com.caesar.rongcloudspeed.extension;

import com.caesar.rongcloudspeed.extend.CaesarRedPacketMessage;
import com.caesar.rongcloudspeed.extend.CaesarRedPacketOpenedMessage;
import com.caesar.rongcloudspeed.extend.CaesarTransferAccountsMessage;

import java.util.ArrayList;
import java.util.List;

import io.rong.eventbus.EventBus;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.sticker.StickerExtensionModule;


public class CustomerExtensionModule extends StickerExtensionModule {
    @Override
    public void onInit(String appKey) {
        RongIM.registerMessageType(CaesarRedPacketMessage.class);
        RongIM.registerMessageType(CaesarRedPacketOpenedMessage.class);
        RongIM.registerMessageType(CaesarTransferAccountsMessage.class);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onConnect(String token) {

    }

    @Override
    public void onAttachedToExtension(RongExtension extension) {

    }

    @Override
    public void onDetachedFromExtension() {

    }

    @Override
    public void onReceivedMessage(Message message) {

    }

    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModules = new ArrayList<>();
        return pluginModules;
    }

    @Override
    public List<IEmoticonTab> getEmoticonTabs() {
        return null;
    }

    @Override
    public void onDisconnect() {

    }
}
