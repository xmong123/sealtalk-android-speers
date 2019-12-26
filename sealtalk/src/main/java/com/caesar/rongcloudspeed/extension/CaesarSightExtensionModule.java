package com.caesar.rongcloudspeed.extension;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import com.caesar.rongcloudspeed.extend.CaesarSightMessageItemProvider;

import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.widget.provider.SightMessageItemProvider;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.message.SightMessage;

import java.util.ArrayList;
import java.util.List;

public class CaesarSightExtensionModule implements IExtensionModule {
    public CaesarSightExtensionModule() {
    }

    public void onInit(String appKey) {
        RongIM.registerMessageType(SightMessage.class);
        RongIM.registerMessageTemplate(new CaesarSightMessageItemProvider());
    }

    public void onConnect(String token) {
    }

    public void onAttachedToExtension(RongExtension extension) {
    }

    public void onDetachedFromExtension() {
    }

    public void onReceivedMessage(Message message) {
    }

    public List<IPluginModule> getPluginModules(ConversationType conversationType) {
        List<IPluginModule> pluginModules = new ArrayList();
        CaesarRedGroupEnvelopePlugin.CaesarSightPlugin sightPlugin = new CaesarRedGroupEnvelopePlugin.CaesarSightPlugin();
        pluginModules.add(sightPlugin);
        return pluginModules;
    }

    public List<IEmoticonTab> getEmoticonTabs() {
        return null;
    }

    public void onDisconnect() {
    }
}

